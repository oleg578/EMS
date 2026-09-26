package ems;

import java.util.List;
import java.util.Objects;

public final class Ems {
    private final EmployeeService service;
    private final PayrollPrinter printer;

    public Ems(EmployeeService service, PayrollPrinter printer) {
        this.service = Objects.requireNonNull(service, "service must not be null");
        this.printer = Objects.requireNonNull(printer, "printer must not be null");
    }

    public Ems loadStaff(List<Employee> employeeList) {
        employeeList.forEach(service::addEmployee);
        return this;
    }

    private void raise() {
        service.giveRaise("Alice", 10.00);
        service.giveRaiseToAll(5.00);
    }

    public void run() {
        printer.print(service.getPayroll(), "\n=== Payroll ===");
        raise();

        printer.print(
                service.getPayroll(),
                "\n=== Payroll after raises (Alice +10%, everyone +5%) ===");

        printer.print(
                new Payroll(
                        List.copyOf(service.getEmployeesByRole(Manager.class))),
                "\n=== Managers ===");

        printer.print(
                new Payroll(List.copyOf(service.getEmployeesByRole(Developer.class))),
                "\n=== Developers ===");
    }

}
