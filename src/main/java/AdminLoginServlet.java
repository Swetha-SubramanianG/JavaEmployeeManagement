import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/admin/login")
public class AdminLoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        String error = (String) request.getAttribute("error");
        if (error == null) error = "";

        out.println(UiTemplate.head("Admin Login"));
        out.println("<div class='card' style='max-width:520px; margin: 70px auto;'>");
        out.println("<h2>Admin Login</h2>");
        out.println("<p>Use administrator credentials to access system control.</p>");
        if (!error.isEmpty()) {
            out.println("<div class='error'>" + escapeHtml(error) + "</div>");
        }
        out.println("<form method='post' action='" + request.getContextPath() + "/admin/login'>");
        out.println("  <label>Username</label><input name='username' required><br><br>");
        out.println("  <label>Password</label><input type='password' name='password' required><br><br>");
        out.println("  <button class='btn' type='submit'>Login</button>");
        out.println("</form>");
        out.println("</div>");
        out.println(UiTemplate.footer());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (!Main.isAdminConfigured()) {
            request.setAttribute("error", "Admin login is not configured. Set EMPLOYEE_ADMIN_USER and EMPLOYEE_ADMIN_PASSWORD.");
            doGet(request, response);
            return;
        }

        if (Main.isAdminCredentials(username, password)) {
            HttpSession session = request.getSession(true);
            session.setAttribute("admin", true);
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            return;
        }

        request.setAttribute("error", "Invalid credentials");
        doGet(request, response);
    }

    private static String escapeHtml(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
