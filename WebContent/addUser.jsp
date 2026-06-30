<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Add New User - Aegis Security Gateway</title>

<style>
    /* page background and layout */
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
        background: rgba(255, 255, 255, 0.1);
        padding: 40px;
        border-radius: 15px;
        width: 400px;
        box-shadow: 0 4px 25px rgba(0, 0, 0, 0.3);
        text-align: center;
    }

    h2 {
        margin-bottom: 20px;
        color: #fff;
    }

    input, select {
        width: 90%;
        padding: 10px;
        margin: 10px 0;
        border-radius: 6px;
        border: none;
        outline: none;
        font-size: 15px;
    }

    button {
        background: linear-gradient(90deg, #36d1dc, #5b86e5);
        border: none;
        padding: 10px 20px;
        color: white;
        font-size: 16px;
        border-radius: 6px;
        cursor: pointer;
        transition: 0.3s;
    }

    button:hover {
        transform: scale(1.05);
        background: linear-gradient(90deg, #5b86e5, #36d1dc);
    }

    a {
        color: #ddd;
        text-decoration: none;
        display: inline-block;
        margin-top: 10px;
    }

    a:hover { color: white; }
</style>

<script>
/*
   Simple front-end validation
   Helps to block unsafe input (like script tags) before submission.
*/
function validateUserForm() {
    let uname = document.getElementById("username").value.trim();
    let email = document.getElementById("email").value.trim();
    let pwd = document.getElementById("password").value.trim();
    let role = document.getElementById("role").value;

    // pattern to block risky characters like < > { }
    const badChars = /[<>{}]/g;

    if (uname === "" || email === "" || pwd === "") {
        alert("All fields are required!");
        return false;
    }

    if (badChars.test(uname) || badChars.test(email) || badChars.test(pwd)) {
        alert("Invalid characters detected! Avoid using < > { } symbols.");
        return false;
    }

    // basic email check
    const emailPattern = /^[^@\s]+@[^@\s]+\.[^@\s]+$/;
    if (!emailPattern.test(email)) {
        alert("Please enter a valid email address!");
        return false;
    }

    if (role === "") {
        alert("Please select a user role.");
        return false;
    }

    return true;
}
</script>
</head>

<body>
<%
    // verify admin session before allowing access
    HttpSession session1 = request.getSession(false);
    String username = null;
    String role = null;

    if (session1 != null) {
        username = (String) session1.getAttribute("username");
        role = (String) session1.getAttribute("role");
    }

    if (username == null || !"ADMIN".equalsIgnoreCase(role)) {
        response.sendRedirect("dashboard.jsp");
        return;
    }
%>

<div class="container">
    <h2>Add New User</h2>

    <!-- Added JS validation -->
    <form action="AddUserServlet" method="post" onsubmit="return validateUserForm();">
        <input type="text" id="username" name="username" placeholder="Enter username" required><br>
        <input type="email" id="email" name="email" placeholder="Enter email address" required><br>
        <input type="password" id="password" name="password" placeholder="Enter password" required><br>

        <select name="role" id="role" required>
            <option value="">Select Role</option>
            <option value="ADMIN">ADMIN</option>
            <option value="USER">USER</option>
            <option value="VISITOR">VISITOR</option>
        </select><br>

        <button type="submit">Add User</button>
    </form>

    <a href="dashboard.jsp">⬅ Back to Dashboard</a>
</div>
</body>
</html>
