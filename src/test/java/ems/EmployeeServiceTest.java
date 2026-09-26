package ems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmployeeServiceTest {

    private EmployeeService service;

    @BeforeEach
    void setUp() {
        service = new EmployeeService(new InMemoryEmployeeRepository());
        service.addEmployee(new Developer("Alice", 1000));
        service.addEmployee(new Manager("Carol", 2000));
        service.addEmployee(new Developer("Bob", 500));
    }

    @Test
    void shouldReturnOnlyManagersWhenFilteringByManagerRole() {
        List<Manager> managers = service.getEmployeesByRole(Manager.class);
        assertEquals(1, managers.size());
        assertEquals("Carol", managers.getFirst().getName());
    }

    @Test
    void shouldReturnOnlyDevelopersInInsertionOrderWhenFilteringByDeveloperRole() {
        List<Developer> developers = service.getEmployeesByRole(Developer.class);
        assertEquals(List.of("Alice", "Bob"), developers.stream().map(Employee::getName).toList());
    }

    @Test
    void shouldReturnEveryoneWhenFilteringByBaseRole() {
        assertEquals(service.getEmployees(), service.getEmployeesByRole(Employee.class));
    }

    @Test
    void shouldFailWhenRoleIsNull() {
        assertThrows(NullPointerException.class, () -> service.getEmployeesByRole(null));
    }

    @Test
    void shouldSumAllSalariesWhenCalculatingPayroll() {
        assertEquals(3500.0, service.getPayroll().total(), 1e-9);
    }

    @Test
    void shouldRaiseOnlyNamedEmployeeWhenGivingIndividualRaise() {
        service.giveRaise("Alice", 10);
        assertEquals(3600.0, service.getPayroll().total(), 1e-9);
    }

    @Test
    void shouldRaiseEveryoneWhenGivingRaiseToAll() {
        service.giveRaiseToAll(10);
        assertEquals(3850.0, service.getPayroll().total(), 1e-9);
    }

    @Test
    void shouldKeepRoleOfEveryoneWhenGivingRaiseToAll() {
        service.giveRaiseToAll(10);
        assertEquals(List.of("Carol"), service.getEmployeesByRole(Manager.class).stream().map(Employee::getName).toList());
        assertEquals(List.of(Developer.class, Manager.class, Developer.class),
                service.getEmployees().stream().map(Object::getClass).toList());
    }

    @Test
    void shouldFailWhenRaisingUnknownEmployee() {
        assertThrows(IllegalArgumentException.class,
                () -> service.giveRaise("Nobody", 10));
    }

    @Test
    void shouldFailWhenAddingEmployeeWithDuplicateName() {
        assertThrows(IllegalArgumentException.class,
                () -> service.addEmployee(new Manager("Alice", 3000)));
    }

    @Test
    void shouldFailWhenRepositoryIsNull() {
        assertThrows(NullPointerException.class, () -> new EmployeeService(null));
    }

    @Test
    void shouldNotAllowExternalModificationWhenReadingEmployees() {
        assertThrows(UnsupportedOperationException.class,
                () -> service.getEmployees().add(new Developer("Eve", 1)));
    }

    @Test
    void shouldNotAllowExternalModificationWhenFilteringByRole() {
        assertThrows(UnsupportedOperationException.class,
                () -> service.getEmployeesByRole(Manager.class).add(new Manager("Eve", 1)));
    }

    @Test
    void shouldRaiseNobodyWhenRaiseToAllExceedsLimitForOne() {
        service.addEmployee(new Manager("Rich", 9_000_000_000.00));
        double totalBefore = service.getPayroll().total();
        assertThrows(IllegalArgumentException.class, () -> service.giveRaiseToAll(50));
        assertEquals(totalBefore, service.getPayroll().total(), 1e-9);
    }

    @Test
    void shouldAddNobodyWhenAddEmployeesContainsExistingName() {
        List<Employee> before = service.getEmployeesByRole(Employee.class);
        assertThrows(IllegalArgumentException.class,
                () -> service.addEmployees(List.of(new Developer("Dave", 100), new Manager("Alice", 100))));
        assertEquals(before, service.getEmployeesByRole(Employee.class));
    }

    @Test
    void shouldIncludeOnlyGivenRoleWhenBuildingPayrollByRole() {
        Payroll payroll = service.getPayrollByRole(Developer.class);
        assertEquals(List.of("Alice", "Bob"), payroll.employees().stream().map(Employee::getName).toList());
        assertEquals(1500.0, payroll.total(), 1e-9);
    }

    @Test
    void shouldRejectNullRoleWhenBuildingPayrollByRole() {
        assertThrows(NullPointerException.class, () -> service.getPayrollByRole(null));
    }
}
