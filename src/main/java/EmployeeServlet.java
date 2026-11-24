import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Optional;

@WebServlet(urlPatterns = "/admin/employees")
public class EmployeeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        ArrayList<Employee> employees = FileHandler.load();

        String message = request.getParameter("message");

        out.println(UiTemplate.head("Employees"));
        out.println("<div class='card'>");
        out.println("<h2>Employees (Add / Edit / Delete)</h2>");
        if (message != null && !message.isBlank()) {
            out.println("<div class='success'>" + escapeHtml(message) + "</div>");
        }

        out.println("<h3>Add Employee</h3>");
        out.println("<form method='post' action='" + request.getContextPath() + "/admin/employees'>");
        out.println("<input type='hidden' name='action' value='add'>");
        out.println("<div class='grid-3'>");
        out.println("<div><label>ID</label><input type='number' name='id' required></div>");
        out.println("<div><label>Name</label><input name='name' required></div>");
        out.println("<div><label>Password</label><input name='password' required></div>");
        out.println("</div><br>");
        out.println("<button class='btn' type='submit'>Add Employee</button>");
        out.println("</form>");

        out.println("<h3>Employee List</h3>");
        out.println("<table><tr><th>ID</th><th>Name</th><th>Actions</th></tr>");
        for (Employee e : employees) {
            out.println("<tr>");
            out.println("<td>" + e.getId() + "</td>");
            out.println("<td>" + escapeHtml(e.getName()) + "</td>");
            out.println("<td>");
            out.println("<form style='display:inline-block; margin-right:8px;' method='post' action='" + request.getContextPath() + "/admin/employees'>");
            out.println("<input type='hidden' name='action' value='delete'>");
            out.println("<input type='hidden' name='id' value='" + e.getId() + "'>");
            out.println("<button class='btn danger' type='submit'>Delete</button>");
            out.println("</form>");
            out.println("<form style='display:inline-block;' method='post' action='" + request.getContextPath() + "/admin/employees'>");
            out.println("<input type='hidden' name='action' value='edit'>");
            out.println("<input type='hidden' name='id' value='" + e.getId() + "'>");
            out.println("<div class='row'>");
            out.println("<input style='width:180px' name='name' value='" + escapeHtml(e.getName()) + "' required>");
            out.println("<input style='width:180px' name='password' value='" + escapeHtml(e.getPassword()) + "' required>");
            out.println("<button class='btn secondary' type='submit'>Update</button>");
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
        String action = request.getParameter("action");
        ArrayList<Employee> employees = FileHandler.load();
        Admin admin = new Admin(employees);
        String msg = "No action";

        if ("add".equals(action)) {
            int id = parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            String password = request.getParameter("password");
            admin.addEmployee(id, name, password);
            msg = "Employee added";
        } else if ("delete".equals(action)) {
            int id = parseInt(request.getParameter("id"));
            admin.deleteEmployee(id);
            msg = "Employee deleted";
        } else if ("edit".equals(action)) {
            int id = parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            String password = request.getParameter("password");
            Optional<Employee> found = employees.stream().filter(e -> e.getId() == id).findFirst();
            if (found.isPresent()) {
                found.get().setName(name);
                found.get().setPassword(password);
                FileHandler.save(employees);
                msg = "Employee updated";
            } else {
                msg = "Employee not found";
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/employees?message=" + urlEncode(msg));
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

