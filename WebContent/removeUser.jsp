<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, com.aegis.security.DBConnection" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Remove User - Aegis Security Gateway</title>
    <style>
        body {
            font-family: "Poppins", sans-serif;
            background: linear-gradient(135deg, #1a2980, #26d0ce);
            color: white;
            text-align: center;
            margin: 0;
            padding: 0;
        }
        table {
            width: 70%;
            margin: 50px auto;
            border-collapse: collapse;
            background: rgba(255, 255, 255, 0.1);
            border-radius: 8px;
        }
        th, td {
            padding: 12px;
            text-align: center;
            color: #fff;
        }
        th {
            background-color: rgba(0, 0, 0, 0.3);
        }
        tr:nth-child(even) {
            background-color: rgba(255, 255, 255, 0.05);
        }
        .btn {
            background-color: #e74c3c;
            border: none;
            padding: 10px 18px;
            color: white;
            border-radius: 6px;
            cursor: pointer;
        }
        .btn:hover {
            background-color: #c0392b;
        }
        .back {
            background-color: rgba(255,255,255,0.2);
            margin-top: 20px;
        }
    </style>
</head>
<body>
    <h2> Remove User</h2>

    <table border="1">
        <tr>
            <th>Username</th>
            <th>Email</th>
            <th>Role</th>
            <th>Action</th>
        </tr>

        <%
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "SELECT username, email, role FROM users WHERE role != 'ADMIN'";
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String username = rs.getString("username");
                    String email = rs.getString("email");
                    String role = rs.getString("role");
        %>
                    <tr>
                        <td><%= username %></td>
                        <td><%= email %></td>
                        <td><%= role %></td>
                        <td>
                            <form action="RemoveUserServlet" method="post">
                                <input type="hidden" name="username" value="<%= username %>">
                                <button type="submit" class="btn">Delete</button>
                            </form>
                        </td>
                    </tr>
        <%
                }
                rs.close();
                ps.close();
            } catch (Exception e) {
                out.println("<p style='color:red;'>Error loading users: " + e.getMessage() + "</p>");
            }
        %>
    </table>

    <form action="dashboard.jsp">
        <button type="submit" class="btn back"> Back to Dashboard</button>
    </form>
</body>
</html>
