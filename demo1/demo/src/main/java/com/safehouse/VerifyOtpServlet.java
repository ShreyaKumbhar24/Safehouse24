package com.safehouse;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class VerifyOtpServlet extends HttpServlet {

    // In-memory OTP store for demo purposes: username -> OTP
    private static Map<String, String> otpStore = new HashMap<>();

    private static final String USER_DATA_PATH = "C:\\Users\\shrey\\OneDrive\\Desktop\\userdata\\userdata.txt";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("text/plain");

        String username = request.getParameter("username");
        String otp = request.getParameter("otp");

        PrintWriter out = response.getWriter();

        if (username == null || otp == null || username.isEmpty() || otp.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("Missing username or OTP");
            return;
        }

        String expectedOtp = otpStore.get(username);
        if (expectedOtp == null || !expectedOtp.equals(otp)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.print("Invalid OTP");
            return;
        }

        // OTP is valid, update user data to mark vault as verified
        File userFile = new File(USER_DATA_PATH);
        if (!userFile.exists()) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("User data file not found");
            return;
        }

        boolean updated = false;
        File tempFile = new File(USER_DATA_PATH + ".tmp");

        try (BufferedReader br = new BufferedReader(new FileReader(userFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[0].equals(username)) {
                    // Update verified status to true (last part)
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < parts.length - 1; i++) {
                        sb.append(parts[i]).append(",");
                    }
                    sb.append("true");
                    bw.write(sb.toString());
                    updated = true;
                } else {
                    bw.write(line);
                }
                bw.newLine();
            }
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("Error updating user data");
            return;
        }

        if (!updated) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.print("User not found");
            return;
        }

        // Replace original file with updated file
        if (!userFile.delete() || !tempFile.renameTo(userFile)) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("Failed to update user data");
            return;
        }

        // Remove OTP from store
        otpStore.remove(username);

        response.setStatus(HttpServletResponse.SC_OK);
        out.print("Verification successful");
    }

    // For demo/testing: method to add OTP for a user
    public static void addOtp(String username, String otp) {
        otpStore.put(username, otp);
    }
}
