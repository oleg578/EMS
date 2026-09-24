package ems;

import java.util.List;
import java.util.Objects;

public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository must not be null");
    }

    public void addEmployee(Employee employee) {
        repository.add(employee);
    }

    public List<Employee> getEmployees() {
        return repository.findAll();
    }

    /** Returns employees of the given role, e.g. {@code getEmployeesByRole(Manager.class)}. */
    public <T extends Employee> List<T> getEmployeesByRole(Class<T> role) {
        Objects.requireNonNull(role, "role must not be null");
        return repository.findAll().stream()
                .filter(role::isInstance)
                .map(role::cast)
                .toList();
    }

    public Payroll getPayroll() {
        return new Payroll(repository.findAll());
    }

    /** Gives a raise to the employee with the given name. Fails if not found. */
    public void giveRaise(String name, double percent) {
        Employee employee = repository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("employee not found: " + name));
        repository.update(employee.withRaise(percent));
    }

    /**
     * Raises everyone or nobody: builds raised copies of all employees first (fails before any change
     * if the raise is invalid for anyone), then saves them in one atomic {@link EmployeeRepository#updateAll} call.
     */
    public void giveRaiseToAll(double percent) {
        List<Employee> raisedEmployees = repository.findAll().stream()
                .map(employee -> employee.withRaise(percent))
                .toList();
        repository.updateAll(raisedEmployees);
    }
}
