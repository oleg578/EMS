package ems;

import java.util.Objects;


public abstract sealed class Employee permits Developer, Manager {

    private final String name;
    private final double salary;

    protected Employee(String name, double salary) {
        EmployeeValidator.validateName(name);
        EmployeeValidator.isFiniteSalary(salary);
        double roundedSalary = EmployeeUtils.roundSalary(salary);
        EmployeeValidator.validateSalary(roundedSalary);
        this.name = name;
        this.salary = roundedSalary;
    }

    public abstract String getRole();

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }
    /**
     * Returns a copy of this employee with the salary raised by the given percentage, e.g., 10 means +10%.
     * This employee is not changed.
     *
     * @throws IllegalArgumentException if the percent is invalid or the new salary exceeds the limit
     */
    public Employee withRaise(double percent) {
        if (!Double.isFinite(percent) || percent <= 0) {
            throw new IllegalArgumentException("raise percent must be a positive finite number, got: " + percent);
        }
        // The constructor rounds to cents and validates the limit
        return withSalary(salary * (1 + percent / 100));
    }
    /** Creates an employee of the same role and name with the given salary. */
    protected abstract Employee withSalary(double salary);

    @Override
    public String toString() {
        return getRole() + "{name=" + name + ", salary=" + salary + "}";
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        // safely cast cause we checked the class above
        Employee oCasted = (Employee) o;
        return name.equals(oCasted.name)
                && Double.compare(salary, oCasted.salary) == 0;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getClass(), name, salary);
    }
}
