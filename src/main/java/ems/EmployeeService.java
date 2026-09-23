package ems;

import java.util.ArrayList;
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

    public List<Manager> getManagers() {
        List<Manager> managers = new ArrayList<>();
        for (Employee employee : repository.findAll()) {
            if (employee instanceof Manager manager) {
                managers.add(manager);
            }
        }
        return List.copyOf(managers);
    }

    public Payroll getPayroll() {
        return new Payroll(repository.findAll());
    }

    public Payroll getManagersPayroll() {
        return new Payroll(List.copyOf(getManagers()));
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
