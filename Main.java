import java.util.*;

public class Main {

    static final String ADMIN_USER = System.getenv("EMPLOYEE_ADMIN_USER");
    static final String ADMIN_PASS = System.getenv("EMPLOYEE_ADMIN_PASSWORD");

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        ArrayList<Employee> employees = FileHandler.load();
        Admin admin = new Admin(employees);

        while (true) {
            System.out.println("\n1.Admin Login\n2.Employee Login\n3.Exit");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    adminLogin(admin, sc);
                    break;
                case 2:
                    employeeLogin(employees, sc);
                    break;
                case 3:
                    System.exit(0);
            }
        }
    }

    // ADMIN LOGIN
    static void adminLogin(Admin admin, Scanner sc) {
        if (!isAdminConfigured()) {
            System.out.println("Admin login is unavailable. Set EMPLOYEE_ADMIN_USER and EMPLOYEE_ADMIN_PASSWORD.");
            return;
        }

        System.out.print("Username: ");
        String user = sc.next();
        System.out.print("Password: ");
        String pass = sc.next();

        if (isAdminCredentials(user, pass)) {
            adminMenu(admin, sc);
        } else {
            System.out.println("Invalid credentials");
        }
    }

    static boolean isAdminConfigured() {
        return ADMIN_USER != null && !ADMIN_USER.isBlank()
                && ADMIN_PASS != null && !ADMIN_PASS.isBlank();
    }

    static boolean isAdminCredentials(String user, String pass) {
        return isAdminConfigured() && ADMIN_USER.equals(user) && ADMIN_PASS.equals(pass);
    }

    static void adminMenu(Admin admin, Scanner sc) {
        while (true) {
            System.out.println("\n1.Add 2.Delete 3.Schedule 4.Salary 5.View 6.Back");
            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    System.out.print("ID: ");
                    int id = sc.nextInt();
                    System.out.print("Name: ");
                    String name = sc.next();
                    System.out.print("Password: ");
                    String pass = sc.next();
                    admin.addEmployee(id, name, pass);
                    break;

                case 2:
                    admin.deleteEmployee(sc.nextInt());
                    break;

                case 3:
                    System.out.print("ID: ");
                    int sid = sc.nextInt();
                    System.out.print("Schedule: ");
                    admin.assignSchedule(sid, sc.next());
                    break;

                case 4:
                    System.out.print("ID: ");
                    int eid = sc.nextInt();
                    System.out.print("Salary: ");
                    admin.setSalary(eid, sc.nextDouble());
                    break;

                case 5:
                    admin.viewEmployees();
                    break;

                case 6:
                    return;
            }
        }
    }

    // EMPLOYEE LOGIN
    static void employeeLogin(ArrayList<Employee> list, Scanner sc) {
        System.out.print("Enter ID: ");
        int id = sc.nextInt();
        System.out.print("Enter Password: ");
        String pass = sc.next();

        for (Employee e : list) {
            if (e.getId() == id && e.getPassword().equals(pass)) {
                employeeMenu(e, list);
                return;
            }
        }
        System.out.println("Invalid login");
    }

    static void employeeMenu(Employee emp, ArrayList<Employee> list) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n1.Schedule 2.Attendance 3.Update 4.Salary 5.Mark 6.Back");
            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    System.out.println(emp.getSchedule());
                    break;
                case 2:
                    System.out.println(emp.getAttendance());
                    break;
                case 3:
                    System.out.print("Details: ");
                    emp.updateDetails(sc.next());
                    FileHandler.save(list);
                    break;
                case 4:
                    System.out.println(emp.getSalary());
                    break;
                case 5:
                    emp.markAttendance();
                    FileHandler.save(list);
                    break;
                case 6:
                    return;
            }
        }
    }
}