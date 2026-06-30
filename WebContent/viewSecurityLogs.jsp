<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="java.util.*, com.aegis.security.SecurityLog" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Security Logs - Aegis Admin Panel</title>
    <style>
        body {
            font-family: "Poppins", sans-serif;
            background: linear-gradient(135deg, #283c86, #45a247);
            color: white;
            margin: 0;
            padding: 0;
            text-align: center;
        }

        h2 {
            margin-top: 40px;
            color: #fff;
        }

        table {
            width: 85%;
            margin: 30px auto;
            border-collapse: collapse;
            border-radius: 12px;
            overflow: hidden;
            background: rgba(255, 255, 255, 0.1);
        }

        th, td {
            padding: 12px 15px;
            text-align: center;
            border-bottom: 1px solid rgba(255, 255, 255, 0.2);
        }

        th {
            background-color: rgba(0, 0, 0, 0.3);
        }

        tr:nth-child(even) {
            background-color: rgba(255, 255, 255, 0.05);
        }

        tr:hover {
            background-color: rgba(255, 255, 255, 0.15);
        }

        .btn {
            background: linear-gradient(90deg, #36d1dc, #5b86e5);
            border: none;
            padding: 10px 20px;
            color: white;
            border-radius: 6px;
            font-size: 15px;
            cursor: pointer;
            transition: 0.3s;
        }

        .btn:hover {
            transform: scale(1.05);
        }

        .footer {
            font-size: 13px;
            opacity: 0.7;
            margin-top: 25px;
        }
    </style>
</head>
<body>
    <%
        HttpSession session1 = request.getSession(false);
        String role = null;
        if (session1 != null) {
            role = (String) session1.getAttribute("role");
        }

        if (role == null || !"ADMIN".equalsIgnoreCase(role)) {
            response.sendRedirect("dashboard.jsp");
            return;
        }

        List<SecurityLog> logs = (List<SecurityLog>) request.getAttribute("logs");
    %>

    <h2> Security Activity Logs</h2>

    <%
        if (logs == null || logs.isEmpty()) {
    %>
        <p>No security logs found.</p>
        <form action="dashboard.jsp">
            <button class="btn" type="submit"> Back to Dashboard</button>
        </form>
    <%
        } else {
    %>
        <table>
            <tr>
                <th>ID</th>
                <th>Username</th>
                <th>Action</th>
                <th>Timestamp</th>
            </tr>
            <%
                for (SecurityLog log : logs) {
            %>
            <tr>
                <td><%= log.getId() %></td>
                <td><%= log.getUsername() %></td>
                <td><%= log.getAction() %></td>
                <td><%= log.getTimestamp() %></td>
            </tr>
            <%
                }
            %>
        </table>

        <form action="dashboard.jsp">
            <button class="btn" type="submit"> Back to Dashboard</button>
        </form>
    <%
        }
    %>

    <p class="footer">© 2025 Aegis Security Gateway | Admin Monitoring Panel</p>
</body>
</html>
