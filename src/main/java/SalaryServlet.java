import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Locale;

@WebServlet(urlPatterns = "/admin/salaries")
public class SalaryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        ArrayList<Employee> employees = FileHandler.load();
        String message = request.getParameter("message");

        out.println(UiTemplate.head("Salaries"));
        out.println("<div class='card'>");
        out.println("<h2>Salaries (Set and Calculate)</h2>");
        if (message != null && !message.isBlank()) {
            out.println("<div class='success'>" + escapeHtml(message) + "</div>");
        }

        out.println("<table>");
        out.println("<tr><th>ID</th><th>Name</th><th>Base Salary</th><th>Attendance</th><th>Calculated Salary</th><th>Actions</th></tr>");
        for (Employee e : employees) {
            double calculated = e.calculateSalary();
            out.println("<tr>");
            out.println("<td>" + e.getId() + "</td>");
            out.println("<td>" + escapeHtml(e.getName()) + "</td>");
            out.println("<td>" + money(e.getSalary()) + "</td>");
            out.println("<td>" + e.getAttendance() + "</td>");
            out.println("<td>" + money(calculated) + "</td>");
            out.println("<td>");
            out.println("<form style='display:inline-block;' method='post' action='" + request.getContextPath() + "/admin/salaries'>");
            out.println("<input type='hidden' name='id' value='" + e.getId() + "'>");
            out.println("<div class='row'>");
            out.println("<input style='width:170px' type='number' name='salary' step='0.01' min='0' required>");
            out.println("<button class='btn' type='submit'>Save Salary</button>");
            out.println("</div>");
            out.println("</form>");
            out.println("</td>");
            out.println("</tr>");
        }
        out.println("</table>");

        out.println("<p style='margin-top:14px;'><a href='" + request.getContextPath() + "/admin/dashboard'>Back to Dashboard</a></p>");
        out.println("</div>");
        out.println(UiTemplate.footer());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = parseInt(request.getParameter("id"));
        double salary = parseDouble(request.getParameter("salary"));

        ArrayList<Employee> employees = FileHandler.load();
        Admin admin = new Admin(employees);
        admin.setSalary(id, salary);
        response.sendRedirect(request.getContextPath() + "/admin/salaries?message=" + urlEncode("Salary updated"));
    }

    private static int parseInt(String v) {
        try {
            return Integer.parseInt(v);
        } catch (Exception ex) {
            return -1;
        }
    }

    private static double parseDouble(String v) {
        try {
            return Double.parseDouble(v);
        } catch (Exception ex) {
            return 0;
        }
    }

    private static String money(double v) {
        return String.format(Locale.US, "%.2f", v);
    }

    private static String urlEncode(String value) {
        return java.net.URLEncoder.encode(value, java.nio.charset.StandardCharsets.UTF_8);
    }

    private static String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}

