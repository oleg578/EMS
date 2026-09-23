package ems;

import java.util.List;

/**
 * Payroll rows with their total. The total is always derived from these rows,
 * so a report cannot show a total that belongs to a different list.
 */
public record Payroll(List<Employee> employees) {

    public Payroll {
        // List.copyOf rejects null lists and null elements and makes the snapshot immutable
        employees = List.copyOf(employees);
    }

    public double total() {
        return employees.stream()
                .mapToDouble(Employee::getSalary)
                .sum();
    }
}
