<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Change Password - Aegis Security</title>
    <style>
        body {
            margin: 0;
            height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            background: linear-gradient(135deg, #141e30, #243b55);
            font-family: "Poppins", sans-serif;
            color: white;
        }

        .container {
            background: rgba(255, 255, 255, 0.12);
            border-radius: 15px;
            padding: 40px 50px;
            text-align: center;
            box-shadow: 0 4px 25px rgba(0, 0, 0, 0.3);
            backdrop-filter: blur(8px);
        }

        input[type="password"] {
            width: 250px;
            padding: 10px;
            margin: 10px 0;
            border-radius: 8px;
            border: none;
            outline: none;
            text-align: center;
        }

        .btn {
            background: linear-gradient(90deg, #36d1dc, #5b86e5);
            border: none;
            padding: 10px 20px;
            color: white;
            border-radius: 8px;
            font-size: 15px;
            cursor: pointer;
            transition: 0.3s;
        }

        .btn:hover {
            transform: scale(1.05);
            background: linear-gradient(90deg, #5b86e5, #36d1dc);
        }

        .back {
            margin-top: 10px;
            color: #ccc;
            font-size: 14px;
        }

        .back a {
            color: #36d1dc;
            text-decoration: none;
        }
    </style>
</head>
<body>
    <%
        HttpSession session1 = request.getSession(false);
        String username = null;

        if (session1 != null) {
            username = (String) session1.getAttribute("username");
        }

        if (username == null) {
            response.sendRedirect("login.jsp");
            return;
        }
    %>

    <div class="container">
        <h2>Change Password</h2>
        <form action="ChangePasswordServlet" method="post">
            <input type="password" name="oldPassword" placeholder="Enter Old Password" required><br>
            <input type="password" name="newPassword" placeholder="Enter New Password" required><br>
            <input type="password" name="confirmPassword" placeholder="Confirm New Password" required><br>
            <button type="submit" class="btn">Update Password</button>
        </form>

        <div class="back">
            <a href="dashboard.jsp"> Back to Dashboard</a>
        </div>
    </div>
</body>
</html>
