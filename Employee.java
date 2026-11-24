import java.io.Serializable;

public class Employee implements Serializable {
    private int id;
    private String name;
    private String password;
    private String schedule;
    private double salary;
    private int attendance;
    private String details;

    public Employee(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.schedule = "Not Assigned";
        this.salary = 0;
        this.attendance = 0;
        this.details = "";
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public void setName(String name) { this.name = name; }

    public void setSchedule(String schedule) { this.schedule = schedule; }
    public String getSchedule() { return schedule; }

    public void setSalary(double salary) { this.salary = salary; }
    public double getSalary() { return salary; }

    public void markAttendance() { attendance++; }
    public int getAttendance() { return attendance; }
    public void setAttendance(int attendance) { this.attendance = Math.max(0, attendance); }
    public void resetAttendance() { this.attendance = 0; }

    public void updateDetails(String details) { this.details = details; }
    public String getDetails() { return details; }

    // Simple salary calculation: base salary proportional to attendance days out of 30.
    public double calculateSalary() {
        if (salary <= 0 || attendance <= 0) return 0;
        return (salary / 30.0) * attendance;
    }

    @Override
    public String toString() {
        return id + "," + name + "," + password + "," + schedule + "," + salary + "," + attendance + "," + details;
    }

    public static Employee fromString(String line) {
        String[] data = line.split(",");
        Employee e = new Employee(Integer.parseInt(data[0]), data[1], data[2]);
        if (data.length > 3) e.schedule = data[3];
        if (data.length > 4) e.salary = Double.parseDouble(data[4]);
        if (data.length > 5) e.attendance = Integer.parseInt(data[5]);
        if (data.length > 6) e.details = data[6];
        return e;
    }
}