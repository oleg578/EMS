package ems;

public final class Developer extends Employee {

    public Developer(String name, double salary) {
        super(name, salary);
    }

    @Override
    protected Developer withSalary(double salary) {
        return new Developer(getName(), salary);
    }

    @Override
    public String getRole() {
        return "Developer";
    }
}
