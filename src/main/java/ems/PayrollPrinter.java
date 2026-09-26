package ems;

import java.io.PrintStream;
import java.util.Locale;
import java.util.Objects;

/**
 * Prints employees as a payroll table followed by a total line.
 */
public class PayrollPrinter implements PayrollReporter {

    private static final int ROLE_WIDTH = 20;
    private static final int NAME_WIDTH = 20;
    private static final String ELLIPSIS = "...";
    private static final int AMOUNT_WIDTH = 16;
    private static final String ROW_FORMAT =
            "%-" + ROLE_WIDTH + "s %-" + NAME_WIDTH + "s %" + AMOUNT_WIDTH + ".2f%n";
    // The TOTAL label spans the role and name columns, so the amounts stay aligned
    private static final String TOTAL_FORMAT =
            "%-" + (ROLE_WIDTH + 1 + NAME_WIDTH) + "s %" + AMOUNT_WIDTH + ".2f%n";

    private final PrintStream out;

    public PayrollPrinter(PrintStream out) {
        this.out = Objects.requireNonNull(out, "out must not be null");
    }

    @Override
    public void print(Payroll payroll, String title) {
        Objects.requireNonNull(payroll, "payroll must not be null");
        out.println(title);
        for (Employee employee : payroll.employees()) {
            out.printf(Locale.ROOT, ROW_FORMAT, employee.getRole(), fitName(employee.getName()), employee.getSalary());
        }
        out.printf(Locale.ROOT, TOTAL_FORMAT, "TOTAL", payroll.total());
    }

    // Long names are cut only in the report, so the amount column stays aligned; stored data is untouched
    private static String fitName(String name) {
        if (name.length() <= NAME_WIDTH) {
            return name;
        }
        return name.substring(0, NAME_WIDTH - ELLIPSIS.length()) + ELLIPSIS;
    }
}
