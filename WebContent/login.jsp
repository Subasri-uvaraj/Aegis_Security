<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Aegis Security Gateway - Login</title>

<style>
    body {
        margin: 0;
        height: 100vh;
        display: flex;
        justify-content: center;
        align-items: center;
        background: linear-gradient(135deg, #2b5876, #4e4376);
        font-family: "Poppins", sans-serif;
    }

    .login-container {
        background: rgba(255, 255, 255, 0.15);
        border-radius: 15px;
        box-shadow: 0 4px 30px rgba(0, 0, 0, 0.3);
        backdrop-filter: blur(8px);
        -webkit-backdrop-filter: blur(8px);
        padding: 40px;
        width: 350px;
        text-align: center;
        color: #fff;
    }

    h2 {
        margin-bottom: 25px;
        letter-spacing: 1px;
    }

    input[type="text"], input[type="password"] {
        width: 90%;
        padding: 12px;
        margin: 10px 0;
        border: none;
        border-radius: 8px;
        outline: none;
        background: rgba(255, 255, 255, 0.2);
        color: #fff;
        font-size: 15px;
    }

    input::placeholder {
        color: rgba(255, 255, 255, 0.8);
    }

    .btn {
        margin-top: 15px;
        width: 95%;
        padding: 12px;
        border: none;
        border-radius: 8px;
        background: linear-gradient(90deg, #36d1dc, #5b86e5);
        color: white;
        font-size: 16px;
        font-weight: 600;
        cursor: pointer;
        transition: 0.3s;
    }

    .btn:hover {
        background: linear-gradient(90deg, #5b86e5, #36d1dc);
        transform: scale(1.05);
    }

    .error {
        color: #ffbaba;
        background: rgba(255, 0, 0, 0.2);
        padding: 8px;
        border-radius: 6px;
        font-size: 14px;
        margin-bottom: 10px;
    }

    .footer {
        margin-top: 15px;
        font-size: 13px;
        opacity: 0.8;
    }
</style>

<script>
function validateLogin() {
    var user = document.getElementById("username").value.trim();
    var pass = document.getElementById("password").value.trim();

    if (user === "" || pass === "") {
        alert("Username and password cannot be empty.");
        return false;
    }

    var invalidChars = /[<>{}]/g;
    if (invalidChars.test(user) || invalidChars.test(pass)) {
        alert("Invalid characters detected. Please remove <, >, {, } symbols.");
        return false;
    }

    return true;
}
</script>
</head>

<body>
<div class="login-container">
    <h2>Aegis Security Gateway</h2>

    <%
        String error = (String) request.getAttribute("error");
        if (error != null) {
    %>
        <div class="error"><%= error %></div>
    <% } %>

    <form action="LoginServlet" method="post" onsubmit="return validateLogin();">
        <input type="text" id="username" name="username" placeholder="Username" required><br>
        <input type="password" id="password" name="password" placeholder="Password" required><br>
        <button type="submit" class="btn">Login</button>
    </form>

    <div class="footer">
        © 2025 Aegis Security Gateway
    </div>
</div>
</body>
</html>
