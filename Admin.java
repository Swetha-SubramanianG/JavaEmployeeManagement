import java.util.*;

public class Admin {
    private ArrayList<Employee> employees;

    public Admin(ArrayList<Employee> employees) {
        this.employees = employees;
    }

    public void addEmployee(int id, String name, String password) {
        employees.add(new Employee(id, name, password));
        FileHandler.save(employees);
        System.out.println("Employee added.");
    }

    public void deleteEmployee(int id) {
        employees.removeIf(e -> e.getId() == id);
        FileHandler.save(employees);
        System.out.println("Deleted.");
    }

    public void assignSchedule(int id, String schedule) {
        for (Employee e : employees) {
            if (e.getId() == id) {
                e.setSchedule(schedule);
            }
        }
        FileHandler.save(employees);
    }

    public void clearSchedule(int id) {
        for (Employee e : employees) {
            if (e.getId() == id) {
                e.setSchedule("Not Assigned");
            }
        }
        FileHandler.save(employees);
    }

    public void setSalary(int id, double salary) {
        for (Employee e : employees) {
            if (e.getId() == id) {
                e.setSalary(salary);
            }
        }
        FileHandler.save(employees);
    }

    public void viewEmployees() {
        for (Employee e : employees) {
            System.out.println(e.getId() + " " + e.getName());
        }
    }

    public ArrayList<Employee> getEmployees() {
        return employees;
    }
}