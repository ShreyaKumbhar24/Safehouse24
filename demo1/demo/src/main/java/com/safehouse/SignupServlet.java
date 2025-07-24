package com.safehouse;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SignupServlet extends HttpServlet {
    public static Map<String, String> users = new HashMap<>();
    private static final String USER_DATA_PATH = "C:\\Users\\shrey\\OneDrive\\Desktop\\userdata\\userdata.txt";

    private Properties smtpProperties;
    private String smtpUsername;
    private String smtpPassword;

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
                    if (parts.length >= 5) {
                        // username, hashedPassword, vaultName, email, phone, verified
                        users.put(parts[0], parts[1] + "," + parts[2] + "," + parts[3] + "," + parts[4] + "," + (parts.length > 5 ? parts[5] : "false"));
                    }
                }
            } catch (IOException e) {
                throw new ServletException("Failed to load user data", e);
            }
        }

        // Load SMTP config
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("smtp.properties")) {
            smtpProperties = new Properties();
            if (input == null) {
                throw new ServletException("Unable to find smtp.properties");
            }
            smtpProperties.load(input);
            smtpUsername = smtpProperties.getProperty("smtp.username");
            smtpPassword = smtpProperties.getProperty("smtp.password");
        } catch (IOException e) {
            throw new ServletException("Failed to load SMTP configuration", e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:5500");
        response.setContentType("text/plain");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirm = request.getParameter("confirm");
        String vaultname = request.getParameter("vaultname");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        PrintWriter out = response.getWriter();

        if (confirm == null || !password.equals(confirm)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("Password confirmation does not match");
            return;
        }

        if (users.containsKey(username)) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            out.print("Username already exists");
            return;
        }

        if (vaultname == null || vaultname.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("Vault name is required");
            return;
        }

        if ((email == null || email.trim().isEmpty()) && (phone == null || phone.trim().isEmpty())) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("Signup failed: Either email or phone number is required for OTP");
            return;
        }

        // Hash the password before storing
        String hashedPassword = hashPassword(password);
        // Store as "hashedPassword,vaultName,email,phone,verified"
        users.put(username, hashedPassword + "," + vaultname + "," + (email != null ? email : "") + "," + (phone != null ? phone : "") + ",false");

        // Append new user to file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_DATA_PATH, true))) {
            bw.write(username + "," + hashedPassword + "," + vaultname + "," + (email != null ? email : "") + "," + (phone != null ? phone : "") + ",false");
            bw.newLine();
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("Failed to save user data");
            return;
        }

        // Generate OTP (6-digit random number)
        String otp = String.format("%06d", (int)(Math.random() * 1000000));

        // Store OTP for user
        VerifyOtpServlet.addOtp(username, otp);

        // Send OTP email if email is provided
        if (email != null && !email.trim().isEmpty()) {
            try {
                sendEmail(email, "Your SafeHouse24 OTP", "Your OTP is: " + otp);
                System.out.println("Sent OTP email to " + email);
            } catch (Exception e) {
                System.err.println("Failed to send OTP email: " + e.getMessage());
            }
        } else {
            // For now, just print OTP to console if no email provided
            System.out.println("OTP for user " + username + ": " + otp);
        }

        response.setStatus(HttpServletResponse.SC_OK);
        out.print("Signup successful. Please verify your account with the OTP sent.");
    }

    private void sendEmail(String to, String subject, String body) throws MessagingException {
        Session session = Session.getInstance(smtpProperties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUsername, smtpPassword);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(smtpUsername));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
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
