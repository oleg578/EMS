package ems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryEmployeeRepositoryTest {

    private InMemoryEmployeeRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryEmployeeRepository();
    }

    @Test
    void shouldReturnEmployeesInInsertionOrderWhenFindingAll() {
        repository.add(new Developer("Bob", 10));
        repository.add(new Manager("Alice", 10));
        List<String> names = repository.findAll().stream().map(Employee::getName).toList();
        assertEquals(List.of("Bob", "Alice"), names);
    }

    @Test
    void shouldFindEmployeeWhenNameExists() {
        Employee alice = new Developer("Alice", 10);
        repository.add(alice);
        assertEquals(alice, repository.findByName("Alice").orElseThrow());
    }

    @Test
    void shouldReturnEmptyWhenNameUnknown() {
        assertTrue(repository.findByName("Nobody").isEmpty());
    }

    @Test
    void shouldRejectAddWhenNameAlreadyExists() {
        repository.add(new Developer("Alice", 10));
        assertThrows(IllegalArgumentException.class,
                () -> repository.add(new Manager("Alice", 1)));
    }

    @Test
    void shouldRejectUpdateWhenEmployeeNotStored() {
        assertThrows(IllegalArgumentException.class,
                () -> repository.update(new Developer("Ghost", 10)));
    }

    @Test
    void shouldSaveAllWhenUpdatingAllExistingEmployees() {
        repository.add(new Developer("Alice", 10));
        repository.add(new Manager("Bob", 20));
        Employee newAlice = new Developer("Alice", 11);
        Employee newBob = new Manager("Bob", 22);
        repository.updateAll(List.of(newAlice, newBob));
        assertEquals(List.of(newAlice, newBob), repository.findAll());
    }

    @Test
    void shouldSaveNothingWhenUpdateAllContainsUnknownEmployee() {
        Employee alice = new Developer("Alice", 10);
        repository.add(alice);
        assertThrows(IllegalArgumentException.class,
                () -> repository.updateAll(List.of(new Developer("Alice", 99), new Developer("Ghost", 10))));
        assertEquals(alice, repository.findByName("Alice").orElseThrow());
    }

    @Test
    void shouldRejectUpdateWhenRoleChanges() {
        Employee alice = new Developer("Alice", 10);
        repository.add(alice);
        assertThrows(IllegalArgumentException.class,
                () -> repository.update(new Manager("Alice", 10)));
        assertEquals(alice, repository.findByName("Alice").orElseThrow());
    }

    @Test
    void shouldSaveNothingWhenUpdateAllChangesRole() {
        Employee alice = new Developer("Alice", 10);
        Employee bob = new Manager("Bob", 20);
        repository.add(alice);
        repository.add(bob);
        assertThrows(IllegalArgumentException.class,
                () -> repository.updateAll(List.of(new Developer("Alice", 11), new Developer("Bob", 22))));
        assertEquals(List.of(alice, bob), repository.findAll());
    }

    @Test
    void shouldSaveAllInOrderWhenAddingAllNewEmployees() {
        Employee alice = new Developer("Alice", 10);
        Employee bob = new Manager("Bob", 20);
        repository.addAll(List.of(alice, bob));
        assertEquals(List.of(alice, bob), repository.findAll());
    }

    @Test
    void shouldSaveNothingWhenAddAllContainsDuplicateNames() {
        assertThrows(IllegalArgumentException.class,
                () -> repository.addAll(List.of(
                        new Developer("Alice", 10), new Manager("Bob", 20), new Developer("Alice", 30))));
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void shouldSaveNothingWhenAddAllContainsStoredName() {
        Employee alice = new Developer("Alice", 10);
        repository.add(alice);
        assertThrows(IllegalArgumentException.class,
                () -> repository.addAll(List.of(new Manager("Bob", 20), new Manager("Alice", 30))));
        assertEquals(List.of(alice), repository.findAll());
    }

    @Test
    void shouldSaveNothingWhenAddAllContainsNullEmployee() {
        // List.of rejects nulls, so Arrays.asList is used to build the input
        List<Employee> employees = Arrays.asList(new Developer("Alice", 10), null);
        assertThrows(NullPointerException.class, () -> repository.addAll(employees));
        assertTrue(repository.findAll().isEmpty());
    }
}
