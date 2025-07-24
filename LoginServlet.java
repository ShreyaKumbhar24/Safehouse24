package com.safehouse;

import java.io.*;
import java.security.MessageDigest;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final String USER_DATA_PATH = "C:\\Users\\shrey\\OneDrive\\Desktop\\userdata\\userdata.txt";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setHeader("Access-Control-Allow-Origin", "*");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("Missing username or password");
            return;
        }

        // Hash the input password for comparison
        String hashedInputPassword = hashPassword(password);

        File userFile = new File(USER_DATA_PATH);
        if (!userFile.exists()) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("User data file not found");
            return;
        }

        boolean isValid = false;
        try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(hashedInputPassword)) {
                    isValid = true;
                    break;
                }
            }
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("Error reading user data");
            e.printStackTrace();
            return;
        }

        if (isValid) {
            out.print("Login successful");
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.print("Login failed");
        }
    }

    // Reusable hashing method
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
