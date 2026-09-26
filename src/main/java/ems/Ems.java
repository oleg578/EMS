package ems;

import java.util.List;
import java.util.Objects;

public final class Ems {
    private final EmployeeService service;
    private final PayrollReporter printer;

    public Ems(EmployeeService service, PayrollReporter printer) {
        this.service = Objects.requireNonNull(service, "service must not be null");
        this.printer = Objects.requireNonNull(printer, "printer must not be null");
    }

    /** Adds all employees or none; fails if any name is already loaded or repeats in the list. */
    public Ems loadStaff(List<Employee> employeeList) {
        service.addEmployees(employeeList);
        return this;
    }

    private void raise() {
        service.giveRaiseToAll(5.00);
    }

    public void run() {
        printer.print(service.getPayroll(), "\n=== Payroll ===");
        service.giveRaise("Alice", 10.00);
        printer.print(
                service.getPayroll(),
                "\n=== Payroll after raises Alice +10% ===");

        raise();

        printer.print(
                service.getPayroll(),
                "\n=== Payroll after raises everyone +5% ===");

        printer.print(service.getPayrollByRole(Manager.class), "\n=== Managers ===");

        printer.print(service.getPayrollByRole(Developer.class), "\n=== Developers ===");
    }

}
