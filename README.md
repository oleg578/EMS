# **Employee Management System**

## Requirements

- Abstract class: `Employee` (fields: name, salary).
- Subclasses: `Developer`, `Manager`.
- Store employees in a `List<Employee>`.
- Features: print payroll, give raises, list managers separately.

## Run

```bash
mvn test                                              # 50 unit tests
mvn package -DskipTests && java -cp target/classes ems.Main   # demo
```
