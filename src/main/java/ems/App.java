package ems;

import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) {
        List<Employee> staff = new ArrayList<>(List.of(
                new Developer("Alice", 5000.575),
                new Developer("Bob", 4500.00),
                new Manager("Carol", 7000.00),
                new Manager("Dave", 6500.00)
        ));

        Ems ems = new Ems();
        ems.loadStaff(staff).run();
    }
}
