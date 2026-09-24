package ems;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        EmployeeService service = new EmployeeService(new InMemoryEmployeeRepository());
        PayrollPrinter printer = new PayrollPrinter(System.out);

        loadStaff(service);

        System.out.println("=== Payroll ===");
        printer.print(service.getPayroll());

        service.giveRaise("Alice", 10.00);
        service.giveRaiseToAll(5.00);

        System.out.println();
        System.out.println("=== Payroll after raises (Alice +10%, everyone +5%) ===");
        printer.print(service.getPayroll());

        System.out.println();
        System.out.println("=== Managers ===");
        printer.print(new Payroll(List.copyOf(service.getEmployeesByRole(Manager.class))));

        System.out.println();
        System.out.println("=== Developers ===");
        printer.print(new Payroll(List.copyOf(service.getEmployeesByRole(Developer.class))));
    }

    public static void loadStaff(EmployeeService srv) {
        srv.addEmployee(new Developer("Alice", 5000.575));
        srv.addEmployee(new Developer("Bob", 4500.00));
        srv.addEmployee(new Manager("Carol", 7000.00));
        srv.addEmployee(new Manager("Dave", 6500.00));
    }
}
