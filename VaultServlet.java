import java.io.IOException;
import java.security.MessageDigest;

@WebServlet("/vault")
public class VaultServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        // For future security, check session here
        request.getRequestDispatcher("vault.html").forward(request, response);
    }
    private String hashPassword(String password) {
    try {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}

}
