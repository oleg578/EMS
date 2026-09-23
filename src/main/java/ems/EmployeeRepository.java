package ems;

import java.util.List;
import java.util.Optional;

/** Stores employees. The name is a unique key. */
public interface EmployeeRepository {

    /** @throws IllegalArgumentException if an employee with the same name already exists */
    void add(Employee employee);

    /**
     * Persists changes of an existing employee.
     *
     * @throws IllegalArgumentException if no employee with this name exists or the role differs
     */
    void update(Employee employee);

    /**
     * Persists changes of several existing employees atomically: either all are saved or none.
     *
     * @throws IllegalArgumentException if any employee is not found or its role differs; nothing is saved then
     */
    void updateAll(List<Employee> employees);

    /** Returns an unmodifiable snapshot of all employees. */
    List<Employee> findAll();

    Optional<Employee> findByName(String name);
}
