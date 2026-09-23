package ems;

public final class Manager extends Employee {

    public Manager(String name, double salary) {
        super(name, salary);
    }

    @Override
    protected Manager withSalary(double salary) {
        return new Manager(getName(), salary);
    }

    @Override
    public String getRole() {
        return "Manager";
    }
}
