import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println(UiTemplate.head("Admin Dashboard"));
        out.println("<div class='card'>");
        out.println("<h2>Admin Module</h2>");
        out.println("<p>Manage employees, schedules, salaries, and complete system data from one place.</p>");
        out.println("<div class='grid-3'>");
        out.println("<a class='menu-link' href='" + request.getContextPath() + "/admin/employees'><strong>Employees</strong><br><span class='muted'>Add / Edit / Delete employee records</span></a>");
        out.println("<a class='menu-link' href='" + request.getContextPath() + "/admin/schedules'><strong>Work Schedules</strong><br><span class='muted'>Add / Edit / Delete shift plans</span></a>");
        out.println("<a class='menu-link' href='" + request.getContextPath() + "/admin/attendance'><strong>Attendance</strong><br><span class='muted'>Mark, set, and reset attendance</span></a>");
        out.println("<a class='menu-link' href='" + request.getContextPath() + "/admin/salaries'><strong>Salaries</strong><br><span class='muted'>Set salary and view calculated pay</span></a>");
        out.println("</div>");
        out.println("<div style='margin-top:16px;'><a class='btn secondary' href='" + request.getContextPath() + "/admin/logout'>Logout</a></div>");
        out.println("</div>");
        out.println(UiTemplate.footer());
    }
}

