package ems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class EmployeeTest {

    @Test
    void shouldIncreaseSalaryWhenRaiseApplied() {
        Employee dev = new Developer("Alice", 1000);
        assertEquals(1100.0, dev.withRaise(10).getSalary(), 1e-9);
    }

    @Test
    void shouldRejectRaiseWhenPercentNotPositive() {
        Employee dev = new Developer("Alice", 1000);
        assertThrows(IllegalArgumentException.class, () -> dev.withRaise(0));
        assertThrows(IllegalArgumentException.class, () -> dev.withRaise(-5));
    }

    @Test
    void shouldRejectRaiseWhenPercentNotFinite() {
        Employee dev = new Developer("Alice", 1000);
        assertThrows(IllegalArgumentException.class, () -> dev.withRaise(Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> dev.withRaise(Double.POSITIVE_INFINITY));
    }

    @Test
    void shouldRoundToCentsWhenPercentIsFractional() {
        Employee dev = new Developer("Alice", 1000);
        assertEquals(1333.33, dev.withRaise(33.333).getSalary(), 1e-9);
    }

    @Test
    void shouldRejectCreationWhenSalaryNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> new Manager("Carol", -1));
    }

    @Test
    void shouldRejectCreationWhenSalaryZero() {
        assertThrows(IllegalArgumentException.class, () -> new Developer("Alice", 0));
    }

    @Test
    void shouldRoundSalaryToCentsWhenCreated() {
        assertEquals(4.35, new Developer("Alice", 4.35).getSalary(), 1e-9);
        assertEquals(5000.58, new Developer("Bob", 5000.575).getSalary(), 1e-9);
    }

    @Test
    void shouldRejectCreationWhenSalaryRoundsToZero() {
        assertThrows(IllegalArgumentException.class, () -> new Developer("Alice", 0.004));
    }

    @Test
    void shouldRejectCreationWhenSalaryNotFinite() {
        assertThrows(IllegalArgumentException.class, () -> new Developer("Alice", Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> new Developer("Alice", Double.POSITIVE_INFINITY));
    }

    @Test
    void shouldRejectCreationWhenSalaryExceedsLimit() {
        assertThrows(IllegalArgumentException.class, () -> new Developer("Alice", 10_000_000_000.00));
        assertThrows(IllegalArgumentException.class, () -> new Developer("Alice", 1e20));
    }

    @Test
    void shouldKeepSalaryWhenRaiseExceedsLimit() {
        Employee dev = new Developer("Alice", 9_000_000_000.00);
        assertThrows(IllegalArgumentException.class, () -> dev.withRaise(50));
        assertEquals(9_000_000_000.00, dev.getSalary(), 1e-9);
    }

    @Test
    void shouldNotChangeOriginalWhenRaising() {
        Employee dev = new Developer("Alice", 1000);
        dev.withRaise(10);
        assertEquals(1000.0, dev.getSalary(), 1e-9);
    }

    @Test
    void shouldKeepRoleAndNameWhenRaising() {
        Employee raised = new Manager("Carol", 1000).withRaise(10);
        assertInstanceOf(Manager.class, raised);
        assertEquals("Carol", raised.getName());
    }

    @Test
    void shouldRejectCreationWhenNameBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> new Developer("  ", 1000));
    }

    @Test
    void shouldKeepFullNameWhenNameIsLong() {
        String name = "Maximilian Alexander Schmidt";
        assertEquals(name, new Developer(name, 1000).getName());
    }
}
