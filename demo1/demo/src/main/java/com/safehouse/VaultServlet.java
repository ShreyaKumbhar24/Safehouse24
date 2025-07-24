package com.safehouse;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

public class VaultServlet extends HttpServlet {

    private static final String VAULT_DATA_DIR = "C:\\Users\\shrey\\OneDrive\\Desktop\\userdata\\vaults";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get vault name from request parameter
        String vaultName = request.getParameter("vaultName");
        if (vaultName == null || vaultName.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print("Missing vault name");
            return;
        }

        File vaultFile = new File(VAULT_DATA_DIR, vaultName + ".txt");
        if (!vaultFile.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().print("Vault not found");
            return;
        }

        // Read vault data and return as JSON (simple format)
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(vaultFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().print("Error reading vault data");
            return;
        }

        response.setContentType("application/json");
        response.getWriter().print("[");
        for (int i = 0; i < lines.size(); i++) {
            response.getWriter().print("\"" + lines.get(i).replace("\"", "\\\"") + "\"");
            if (i < lines.size() - 1) {
                response.getWriter().print(",");
            }
        }
        response.getWriter().print("]");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Add or update vault data
        String vaultName = request.getParameter("vaultName");
        String data = request.getParameter("data"); // JSON string or plain text

        if (vaultName == null || vaultName.trim().isEmpty() || data == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print("Missing vault name or data");
            return;
        }

        File vaultDir = new File(VAULT_DATA_DIR);
        if (!vaultDir.exists()) {
            vaultDir.mkdirs();
        }

        File vaultFile = new File(vaultDir, vaultName + ".txt");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(vaultFile, false))) {
            bw.write(data);
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().print("Error saving vault data");
            return;
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print("Vault data saved");
    }
}
