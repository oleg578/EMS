package ems;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class InMemoryEmployeeRepository implements EmployeeRepository {

    // LinkedHashMap keeps insertion order
    private final Map<String, Employee> employeesByName = new LinkedHashMap<>();

    @Override
    public void add(Employee employee) {
        Objects.requireNonNull(employee, "employee must not be null");
        if (employeesByName.putIfAbsent(employee.getName(), employee) != null) {
            throw new IllegalArgumentException("employee already exists: " + employee.getName());
        }
    }

    @Override
    public void addAll(List<Employee> employees) {
        Objects.requireNonNull(employees, "employees must not be null");
        // Validate everything first, so a failure leaves the map untouched
        Set<String> newNames = new HashSet<>();
        for (Employee employee : employees) {
            Objects.requireNonNull(employee, "employee must not be null");
            String name = employee.getName();
            if (employeesByName.containsKey(name) || !newNames.add(name)) {
                throw new IllegalArgumentException("employee already exists: " + name);
            }
        }
        for (Employee employee : employees) {
            employeesByName.put(employee.getName(), employee);
        }
    }

    @Override
    public void update(Employee employee) {
        requireStoredWithSameRole(employee);
        employeesByName.put(employee.getName(), employee);
    }

    @Override
    public void updateAll(List<Employee> employees) {
        Objects.requireNonNull(employees, "employees must not be null");
        // Validate everything first, so a failure leaves the map untouched
        for (Employee employee : employees) {
            requireStoredWithSameRole(employee);
        }
        for (Employee employee : employees) {
            employeesByName.put(employee.getName(), employee);
        }
    }

    // An update must not silently turn e.g. a Developer into a Manager
    private void requireStoredWithSameRole(Employee employee) {
        Objects.requireNonNull(employee, "employee must not be null");
        Employee stored = employeesByName.get(employee.getName());
        if (stored == null) {
            throw new IllegalArgumentException("employee not found: " + employee.getName());
        }
        if (stored.getClass() != employee.getClass()) {
            throw new IllegalArgumentException("cannot change role of " + employee.getName()
                    + " from " + stored.getRole() + " to " + employee.getRole());
        }
    }

    @Override
    public List<Employee> findAll() {
        return List.copyOf(employeesByName.values());
    }

    @Override
    public Optional<Employee> findByName(String name) {
        return Optional.ofNullable(employeesByName.get(name));
    }
}
