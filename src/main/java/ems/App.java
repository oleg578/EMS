package ems;

import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) {
        System.out.println("Starting Employee Management System...");
        // Loading staff...
        List<Employee> staff = new ArrayList<>(List.of(
                new Developer("Alice", 5000.00089),
                new Developer("Bob", 4000.00),
                new Manager("Carol", 7000.00),
                new Manager("Dave", 6000.00)
        ));
        //create service and printer
        EmployeeService service = new EmployeeService(new InMemoryEmployeeRepository());
        PayrollPrinter printer = new PayrollPrinter(System.out);
        // Initializing Employee Management System...
        Ems ems = new Ems(service, printer);
        // Running Employee Management System...
        ems.loadStaff(staff).run();
    }
}
