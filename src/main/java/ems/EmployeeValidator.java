package ems;

import java.util.Objects;

public class EmployeeValidator {
    /**
     * Validates that the given name is not null and not blank.
     * Throws an IllegalArgumentException if the provided name is null or blank.
     *
     * @param name the name to be validated
     * @throws IllegalArgumentException if the specified name is null or blank
     */
    public static void validateName(String name) {
        Objects.requireNonNull(name, "name must not be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
    }

    /**
     * Validates that the given salary value is within the specified range.
     * Throws an IllegalArgumentException if the provided salary value is not within the range.
     *
     * @param salaryValue the salary value to be validated
     * @param maxValue the maximum allowed salary value
     * @return the validated salary value
     * @throws IllegalArgumentException if the specified salaryValue is not within the range
     */
    public static Double validateSalary(double salaryValue, double maxValue) {
        if (salaryValue <= 0) {
            throw new IllegalArgumentException("salary must be positive after rounding to cents, got: " + salaryValue);
        }
        if (salaryValue >= maxValue) {
            throw new IllegalArgumentException("salary must be less than " + maxValue + ", got: " + salaryValue);
        }
        return salaryValue;
    }

    /**
     * Validates that the given salary value is a finite number.
     * Throws an IllegalArgumentException if the provided value is not finite.
     *
     * @param salaryValue the salary value to be validated
     * @throws IllegalArgumentException if the specified salaryValue is not a finite number
     */
    public static void isFiniteSalary(double salaryValue) {
        if (!Double.isFinite(salaryValue)) {
            throw new IllegalArgumentException("salary must be a finite number, got: " + salaryValue);
        }
    }
}
