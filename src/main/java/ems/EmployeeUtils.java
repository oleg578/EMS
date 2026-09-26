package ems;

public class EmployeeUtils {
    public static Double roundSalary(double salaryValue) {
        return Math.round(salaryValue * 100.0) / 100.0;
    }
}
