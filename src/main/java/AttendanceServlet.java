import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Optional;

@WebServlet(urlPatterns = "/admin/attendance")
public class AttendanceServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        ArrayList<Employee> employees = FileHandler.load();
        String message = request.getParameter("message");

        out.println(UiTemplate.head("Attendance"));
        out.println("<div class='card'>");
        out.println("<h2>Attendance Management</h2>");
        out.println("<p>Mark attendance, set count directly, or reset for each employee.</p>");
        if (message != null && !message.isBlank()) {
            out.println("<div class='success'>" + escapeHtml(message) + "</div>");
        }

        out.println("<table><tr><th>ID</th><th>Name</th><th>Attendance Days</th><th>Actions</th></tr>");
        for (Employee e : employees) {
            out.println("<tr>");
            out.println("<td>" + e.getId() + "</td>");
            out.println("<td>" + escapeHtml(e.getName()) + "</td>");
            out.println("<td>" + e.getAttendance() + "</td>");
            out.println("<td>");

            out.println("<form style='display:inline-block; margin-right:8px;' method='post' action='" + request.getContextPath() + "/admin/attendance'>");
            out.println("<input type='hidden' name='action' value='mark'>");
            out.println("<input type='hidden' name='id' value='" + e.getId() + "'>");
            out.println("<button class='btn' type='submit'>Mark Present</button>");
            out.println("</form>");

            out.println("<form style='display:inline-block; margin-right:8px;' method='post' action='" + request.getContextPath() + "/admin/attendance'>");
            out.println("<input type='hidden' name='action' value='set'>");
            out.println("<input type='hidden' name='id' value='" + e.getId() + "'>");
            out.println("<div class='row'>");
            out.println("<input style='width:130px' type='number' min='0' name='attendance' value='" + e.getAttendance() + "' required>");
            out.println("<button class='btn secondary' type='submit'>Set</button>");
            out.println("</div>");
            out.println("</form>");

            out.println("<form style='display:inline-block;' method='post' action='" + request.getContextPath() + "/admin/attendance'>");
            out.println("<input type='hidden' name='action' value='reset'>");
            out.println("<input type='hidden' name='id' value='" + e.getId() + "'>");
            out.println("<button class='btn danger' type='submit'>Reset</button>");
            out.println("</form>");

            out.println("</td></tr>");
        }
        out.println("</table>");
        out.println("<p style='margin-top:14px;'><a href='" + request.getContextPath() + "/admin/dashboard'>Back to Dashboard</a></p>");
        out.println("</div>");
        out.println(UiTemplate.footer());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");
        int id = parseInt(request.getParameter("id"));
        ArrayList<Employee> employees = FileHandler.load();
        String msg = "No action";

        Optional<Employee> found = employees.stream().filter(e -> e.getId() == id).findFirst();
        if (found.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/attendance?message=" + urlEncode("Employee not found"));
            return;
        }

        Employee employee = found.get();
        if ("mark".equals(action)) {
            employee.markAttendance();
            msg = "Attendance marked";
        } else if ("set".equals(action)) {
            int attendance = parseInt(request.getParameter("attendance"));
            employee.setAttendance(attendance);
            msg = "Attendance updated";
        } else if ("reset".equals(action)) {
            employee.resetAttendance();
            msg = "Attendance reset";
        }

        FileHandler.save(employees);
        response.sendRedirect(request.getContextPath() + "/admin/attendance?message=" + urlEncode(msg));
    }

    private static int parseInt(String v) {
        try {
            return Integer.parseInt(v);
        } catch (Exception ex) {
            return -1;
        }
    }

    private static String urlEncode(String value) {
        return java.net.URLEncoder.encode(value, java.nio.charset.StandardCharsets.UTF_8);
    }

    private static String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}

