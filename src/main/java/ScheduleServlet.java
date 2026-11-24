import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(urlPatterns = "/admin/schedules")
public class ScheduleServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        ArrayList<Employee> employees = FileHandler.load();
        String message = request.getParameter("message");

        out.println(UiTemplate.head("Work Schedules"));
        out.println("<div class='card'>");
        out.println("<h2>Work Schedules (Add / Edit / Delete)</h2>");
        if (message != null && !message.isBlank()) {
            out.println("<div class='success'>" + escapeHtml(message) + "</div>");
        }

        out.println("<table><tr><th>ID</th><th>Name</th><th>Current Schedule</th><th>Actions</th></tr>");
        for (Employee e : employees) {
            out.println("<tr>");
            out.println("<td>" + e.getId() + "</td>");
            out.println("<td>" + escapeHtml(e.getName()) + "</td>");
            out.println("<td>" + escapeHtml(e.getSchedule()) + "</td>");
            out.println("<td>");

            out.println("<form style='display:inline-block; margin-right:8px;' method='post' action='" + request.getContextPath() + "/admin/schedules'>");
            out.println("<input type='hidden' name='action' value='set'>");
            out.println("<input type='hidden' name='id' value='" + e.getId() + "'>");
            out.println("<div class='row'>");
            out.println("<input style='width:220px' name='schedule' placeholder='New schedule' required>");
            out.println("<button class='btn' type='submit'>Save</button>");
            out.println("</div>");
            out.println("</form>");

            out.println("<form style='display:inline-block;' method='post' action='" + request.getContextPath() + "/admin/schedules'>");
            out.println("<input type='hidden' name='action' value='delete'>");
            out.println("<input type='hidden' name='id' value='" + e.getId() + "'>");
            out.println("<button class='btn danger' type='submit'>Delete Schedule</button>");
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
        String action = request.getParameter("action");
        int id = parseInt(request.getParameter("id"));
        String schedule = request.getParameter("schedule");

        ArrayList<Employee> employees = FileHandler.load();
        Admin admin = new Admin(employees);
        String msg = "No action";

        if ("set".equals(action)) {
            admin.assignSchedule(id, schedule);
            msg = "Schedule saved";
        } else if ("delete".equals(action)) {
            admin.clearSchedule(id);
            msg = "Schedule deleted";
        }

        response.sendRedirect(request.getContextPath() + "/admin/schedules?message=" + urlEncode(msg));
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

