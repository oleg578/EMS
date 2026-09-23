package ems;

public class Main {

    public static void main(String[] args) {
        EmployeeService service = new EmployeeService(new InMemoryEmployeeRepository());
        PayrollPrinter printer = new PayrollPrinter(System.out);
        service.addEmployee(new Developer("Alice", 5000.575));
        service.addEmployee(new Developer("Bob", 4500.00));
        service.addEmployee(new Manager("Carol", 7000.00));
        service.addEmployee(new Manager("Dave", 6500.00));

        System.out.println("=== Payroll ===");
        printer.print(service.getPayroll());

        service.giveRaise("Alice", 10.00);
        service.giveRaiseToAll(5.00);

        System.out.println();
        System.out.println("=== Payroll after raises (Alice +10%, everyone +5%) ===");
        printer.print(service.getPayroll());

        System.out.println();
        System.out.println("=== Managers ===");
        printer.print(service.getManagersPayroll());
    }
}
