package com.safehouse;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {
    public static Map<String, String> users = new HashMap<>();
    private static final String USER_DATA_PATH = "C:\\Users\\shrey\\OneDrive\\Desktop\\userdata\\userdata.txt";

    @Override
    public void init() throws ServletException {
        super.init();
        // Load existing users from file
        File userFile = new File(USER_DATA_PATH);
        if (userFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 2) {
                        users.put(parts[0], parts[1]);
                    }
                }
            } catch (IOException e) {
                throw new ServletException("Failed to load user data", e);
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setHeader("Access-Control-Allow-Origin", "*");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirm = request.getParameter("confirm");

        if (!password.equals(confirm)) {
            response.sendRedirect("signup.html?error=confirm");
            return;
        }

        if (users.containsKey(username)) {
            response.sendRedirect("signup.html?error=exists");
            return;
        }

        // Hash the password before storing
        String hashedPassword = hashPassword(password);
        users.put(username, hashedPassword);

        // Append new user to file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_DATA_PATH, true))) {
            bw.write(username + "," + hashedPassword);
            bw.newLine();
        } catch (IOException e) {
            throw new ServletException("Failed to save user data", e);
        }

        response.sendRedirect("login.html?signup=success");
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
