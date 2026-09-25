package ems;

import java.util.Objects;

public abstract sealed class Employee permits Developer, Manager {

    private static final double MAX_SALARY = 10_000_000_000.00; // not for Venezuela! :)
    private final String name;
    private final double salary;

    protected Employee(String name, double salary) {
        Objects.requireNonNull(name, "name must not be null");
        if (!Double.isFinite(salary)) {
            throw new IllegalArgumentException("salary must be a finite number, got: " + salary);
        }
        double roundedSalary = Math.round(salary * 100.00) / 100.00;
        validateSalary(roundedSalary);
        if (name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        this.name = name;
        this.salary = roundedSalary;
    }

    private static void validateSalary(double salaryValue) {
        if (salaryValue <= 0) {
            throw new IllegalArgumentException("salary must be positive after rounding to cents, got: " + salaryValue);
        }
        if (salaryValue >= MAX_SALARY) {
            throw new IllegalArgumentException("salary must be less than " + MAX_SALARY + ", got: " + salaryValue);
        }
    }

    public abstract String getRole();

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }
    // TODO: move to EmployeeUtils
    /**
     * Returns a copy of this employee with the salary raised by the given percentage, e.g. 10 means +10%.
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
    // TODO: move to EmployeeUtils
    /** Creates an employee of the same role and name with the given salary. */
    protected abstract Employee withSalary(double salary);

    @Override
    public String toString() {
        return getRole() + "{name=" + name + ", salary=" + salary + "}";
    }
}
