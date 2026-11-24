import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Optional;

@WebServlet(urlPatterns = "/employee/login")
public class EmployeeLoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        String error = (String) request.getAttribute("error");
        if (error == null) error = "";

        out.println(UiTemplate.head("Employee Login"));
        out.println("<div class='card' style='max-width:520px; margin: 70px auto;'>");
        out.println("<h2>Employee Login</h2>");
        out.println("<p>Sign in with your employee ID to view your schedule, attendance, details, and salary.</p>");
        if (!error.isEmpty()) {
            out.println("<div class='error'>" + escapeHtml(error) + "</div>");
        }
        out.println("<form method='post' action='" + request.getContextPath() + "/employee/login'>");
        out.println("  <label>Employee ID</label><input type='number' name='id' required><br><br>");
        out.println("  <label>Password</label><input type='password' name='password' required><br><br>");
        out.println("  <button class='btn' type='submit'>Login</button>");
        out.println("</form>");
        out.println("<p style='margin-top:14px;'><a href='" + request.getContextPath() + "/admin/login'>Switch to Admin Login</a></p>");
        out.println("</div>");
        out.println(UiTemplate.footer());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = parseInt(request.getParameter("id"));
        String password = request.getParameter("password");
        ArrayList<Employee> employees = FileHandler.load();
        Optional<Employee> employee = employees.stream()
                .filter(e -> e.getId() == id && e.getPassword().equals(password))
                .findFirst();

        if (employee.isPresent()) {
            HttpSession session = request.getSession(true);
            session.setAttribute("employeeId", employee.get().getId());
            response.sendRedirect(request.getContextPath() + "/employee/home");
            return;
        }

        request.setAttribute("error", "Invalid employee ID or password");
        doGet(request, response);
    }

    private static int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception ex) {
            return -1;
        }
    }

    private static String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
