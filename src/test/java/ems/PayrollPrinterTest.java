package ems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;

class PayrollPrinterTest {

    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    private final PayrollPrinter printer =
            new PayrollPrinter(new PrintStream(buffer, true, StandardCharsets.UTF_8));

    private static final String TITLE = "=== Payroll ===";

    private String printed() {
        return buffer.toString(StandardCharsets.UTF_8);
    }

    // Output without the title line, so table-only assertions are not affected by it
    private String printedTable() {
        return printed().substring((TITLE + System.lineSeparator()).length());
    }

    @Test
    void shouldPrintEveryEmployeeAndTotalWhenPrintingPayroll() {
        printer.print(new Payroll(List.of(new Developer("Alice", 1000), new Manager("Carol", 2000.5))), TITLE);
        String payroll = printed();
        assertTrue(payroll.contains("Developer"));
        assertTrue(payroll.contains("Alice"));
        assertTrue(payroll.contains("Carol"));
        assertTrue(payroll.contains("TOTAL"));
        assertTrue(payroll.contains("3000.50"));
    }

    @Test
    void shouldPrintZeroTotalWhenListIsEmpty() {
        printer.print(new Payroll(List.of()), TITLE);
        assertTrue(printedTable().startsWith("TOTAL"));
        assertTrue(printed().contains("0.00"));
    }

    @Test
    void shouldAlignAmountColumnWhenSalaryIsNearLimit() {
        printer.print(new Payroll(List.of(new Developer("Alice", 1000), new Manager("Rich", 9_999_999_999.99))), TITLE);
        List<Integer> lineLengths = printedTable().lines().map(String::length).distinct().toList();
        assertEquals(1, lineLengths.size(), "all payroll lines must have the same width");
    }

    @Test
    void shouldAlignAmountColumnWhenNameIsLongerThanColumn() {
        printer.print(new Payroll(List.of(new Developer("Al", 1000), new Manager("B".repeat(50), 2000))), TITLE);
        List<Integer> lineLengths = printedTable().lines().map(String::length).distinct().toList();
        assertEquals(1, lineLengths.size(), "all payroll lines must have the same width");
    }

    @Test
    void shouldTruncateNameWithEllipsisWhenNameIsLongerThanColumn() {
        printer.print(new Payroll(List.of(new Developer("Maximilian Alexander Schmidt", 1000))), TITLE);
        assertTrue(printed().contains("Maximilian Alexan... "));
    }

    @Test
    void shouldPrintFullNameWhenNameFitsColumn() {
        printer.print(new Payroll(List.of(new Developer("A".repeat(20), 1000))), TITLE);
        assertTrue(printed().contains("A".repeat(20) + " "));
        assertFalse(printed().contains("..."));
    }

    @Test
    void shouldPrintTitleBeforeTableWhenTitleIsGiven() {
        printer.print(new Payroll(List.of(new Developer("Alice", 1000))), TITLE);
        assertTrue(printed().startsWith(TITLE + System.lineSeparator()));
    }

    @Test
    void shouldFailWhenPayrollIsNull() {
        assertThrows(NullPointerException.class, () -> printer.print(null, TITLE));
    }

    @Test
    void shouldFailWhenOutputIsNull() {
        assertThrows(NullPointerException.class, () -> new PayrollPrinter(null));
    }
}
