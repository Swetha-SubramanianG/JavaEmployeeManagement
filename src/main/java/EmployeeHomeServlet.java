import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;

@WebServlet(urlPatterns = "/employee/home")
public class EmployeeHomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("employeeId") == null) {
            response.sendRedirect(request.getContextPath() + "/employee/login");
            return;
        }

        int employeeId = (int) session.getAttribute("employeeId");
        ArrayList<Employee> employees = FileHandler.load();
        Optional<Employee> found = employees.stream().filter(e -> e.getId() == employeeId).findFirst();

        if (found.isEmpty()) {
            if (session != null) session.invalidate();
            response.sendRedirect(request.getContextPath() + "/employee/login");
            return;
        }

        Employee employee = found.get();
        String message = request.getParameter("message");

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println(UiTemplate.head("Employee Dashboard"));
        out.println("<div class='card'>");
        out.println("<h2>Welcome, " + escapeHtml(employee.getName()) + "</h2>");
        out.println("<p>View your schedule, attendance, salary, and update your details from one place.</p>");
        if (message != null && !message.isBlank()) {
            out.println("<div class='success'>" + escapeHtml(message) + "</div>");
        }

        out.println("<div style='margin-bottom:16px;'>");
        out.println("<p><strong>Schedule:</strong> " + escapeHtml(employee.getSchedule()) + "</p>");
        out.println("<p><strong>Attendance:</strong> " + employee.getAttendance() + " days</p>");
        out.println("<p><strong>Base Salary:</strong> " + money(employee.getSalary()) + "</p>");
        out.println("<p><strong>Calculated Salary:</strong> " + money(employee.calculateSalary()) + "</p>");
        out.println("<p><strong>Details:</strong> " + escapeHtml(employee.getDetails()) + "</p>");
        out.println("</div>");

        out.println("<form method='post' action='" + request.getContextPath() + "/employee/home'>");
        out.println("<input type='hidden' name='action' value='update'>");
        out.println("<label>Update your details</label>");
        out.println("<textarea name='details' rows='4' style='width:100%; margin-top:6px;'>" + escapeHtml(employee.getDetails()) + "</textarea><br><br>");
        out.println("<button class='btn' type='submit'>Save Details</button>");
        out.println("</form>");

        out.println("<form method='post' action='" + request.getContextPath() + "/employee/home' style='margin-top:14px;'>");
        out.println("<input type='hidden' name='action' value='mark'>");
        out.println("<button class='btn' type='submit'>Mark Attendance</button>");
        out.println("</form>");

        out.println("<div style='margin-top:18px;'><a class='btn secondary' href='" + request.getContextPath() + "/employee/logout'>Logout</a></div>");
        out.println("</div>");
        out.println(UiTemplate.footer());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("employeeId") == null) {
            response.sendRedirect(request.getContextPath() + "/employee/login");
            return;
        }

        int employeeId = (int) session.getAttribute("employeeId");
        ArrayList<Employee> employees = FileHandler.load();
        Optional<Employee> found = employees.stream().filter(e -> e.getId() == employeeId).findFirst();

        if (found.isEmpty()) {
            if (session != null) session.invalidate();
            response.sendRedirect(request.getContextPath() + "/employee/login");
            return;
        }

        Employee employee = found.get();
        String action = request.getParameter("action");
        String message = "Updated";

        if ("update".equals(action)) {
            String details = request.getParameter("details");
            employee.updateDetails(details == null ? "" : details);
            FileHandler.save(employees);
            message = "Your details have been saved.";
        } else if ("mark".equals(action)) {
            employee.markAttendance();
            FileHandler.save(employees);
            message = "Attendance marked.";
        } else {
            message = "No action taken.";
        }

        response.sendRedirect(request.getContextPath() + "/employee/home?message=" + urlEncode(message));
    }

    private static String money(double value) {
        return String.format("%.2f", value);
    }

    private static int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception ex) {
            return -1;
        }
    }

    private static String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private static String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
