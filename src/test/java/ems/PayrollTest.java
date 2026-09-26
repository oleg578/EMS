package ems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class PayrollTest {

    @Test
    void shouldSumSalariesOfRowsWhenCalculatingTotal() {
        Payroll payroll = new Payroll(List.of(new Developer("Alice", 1000), new Manager("Carol", 2000.5)));
        assertEquals(3000.5, payroll.total(), 1e-9);
    }

    @Test
    void shouldReturnZeroTotalWhenPayrollIsEmpty() {
        assertEquals(0.0, new Payroll(List.of()).total(), 1e-9);
    }

    @Test
    void shouldKeepSnapshotWhenSourceListChanges() {
        List<Employee> source = new ArrayList<>(List.of(new Developer("Alice", 1000)));
        Payroll payroll = new Payroll(source);
        source.add(new Manager("Carol", 2000));
        assertEquals(1, payroll.employees().size());
        assertEquals(1000.0, payroll.total(), 1e-9);
    }

    @Test
    void shouldFailWhenListOrElementIsNull() {
        assertThrows(NullPointerException.class, () -> new Payroll(null));
        assertThrows(NullPointerException.class, () -> new Payroll(Arrays.asList(new Developer("Alice", 1), null)));
    }
}
