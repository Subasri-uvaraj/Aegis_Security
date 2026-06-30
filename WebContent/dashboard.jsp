<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Aegis Security Dashboard</title>
    <style>
        body {
            margin: 0;
            height: 100vh;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            background: linear-gradient(135deg, #2b5876, #4e4376);
            font-family: "Poppins", sans-serif;
            color: white;
        }

        .container {
            background: rgba(255, 255, 255, 0.12);
            border-radius: 15px;
            padding: 40px 60px;
            text-align: center;
            box-shadow: 0 4px 25px rgba(0, 0, 0, 0.3);
            backdrop-filter: blur(8px);
            -webkit-backdrop-filter: blur(8px);
            width: 400px;
        }

        h2 {
            margin-bottom: 10px;
            font-weight: 600;
        }

        h4 {
            margin-bottom: 30px;
            font-weight: 400;
            opacity: 0.85;
        }

        .btn {
            display: block;
            width: 250px;
            padding: 12px;
            margin: 10px auto;
            border: none;
            border-radius: 8px;
            background: linear-gradient(90deg, #36d1dc, #5b86e5);
            color: white;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease-in-out;
        }

        .btn:hover {
            transform: scale(1.07);
            background: linear-gradient(90deg, #5b86e5, #36d1dc);
        }

        .admin-section {
            margin-top: 25px;
            border-top: 1px solid rgba(255, 255, 255, 0.3);
            padding-top: 20px;
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
    // Session verification
    HttpSession session1 = request.getSession(false);
    String username = null;
    String role = null;

    if (session1 != null) {
        username = (String) session1.getAttribute("username");
        role = (String) session1.getAttribute("role");
    }

    if (username == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<div class="container">
    <h2>Welcome, <%= username %></h2>
    <h4>Your Role: <%= role.toUpperCase() %></h4>

    <!-- Universal Access: Send / View / Group Chat / Change Password -->
    <form action="sendMessage.jsp" method="get">
        <button class="btn" type="submit">Send Message</button>
    </form>

    <form action="viewMessages.jsp" method="get">
        <button class="btn" type="submit">View Messages</button>
    </form>

    <form action="groupChat.jsp" method="get">
        <button class="btn" type="submit">Group Chat</button>
    </form>

    <form action="changePassword.jsp" method="get">
        <button class="btn" type="submit">Change Password</button>
    </form>

    <!-- Logout -->
    <form action="LogoutServlet" method="post">
        <button class="btn" type="submit">Logout</button>
    </form>

    <!-- Admin Only -->
    <% if ("ADMIN".equalsIgnoreCase(role)) { %>
    <div class="admin-section">
        <h3>Admin Controls</h3>
        <form action="addUser.jsp" method="get">
            <button class="btn" type="submit">Add User</button>
        </form>

        <form action="removeUser.jsp" method="get">
            <button class="btn" type="submit">Remove User</button>
        </form>
    </div>
    <% } %>

    <p class="footer">© 2025 Aegis Security Gateway | Confidential Access</p>
</div>

</body>
</html>
