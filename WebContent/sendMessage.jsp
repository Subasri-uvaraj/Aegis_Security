<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Send Secure Message</title>
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
.container {
    background: rgba(255, 255, 255, 0.15);
    padding: 40px;
    border-radius: 15px;
    box-shadow: 0 4px 25px rgba(0,0,0,0.3);
    width: 400px;
    text-align: center;
    color: #fff;
}
select, textarea, button {
    width: 90%;
    margin: 10px 0;
    padding: 10px;
    border-radius: 8px;
    border: none;
    font-size: 15px;
}
button {
    background: linear-gradient(90deg, #36d1dc, #5b86e5);
    color: white;
    font-weight: 600;
    cursor: pointer;
}
button:hover {
    transform: scale(1.05);
    background: linear-gradient(90deg, #5b86e5, #36d1dc);
}
</style>
</head>
<body>

<div class="container">
    <h2>Send Secure Message</h2>

    <form action="MessageController" method="post">
        <label for="receiver">Select Receiver:</label><br>
        <select name="receiver" required>
            <option value="">-- Select User --</option>
            <%
                java.sql.Connection conn = com.aegis.security.DBConnection.getConnection();
                java.sql.Statement stmt = conn.createStatement();
                String currentUser = (String) session.getAttribute("username");
                java.sql.ResultSet rs = stmt.executeQuery("SELECT username FROM users WHERE username != '" + currentUser + "'");
                boolean found = false;
                while (rs.next()) {
                    found = true;
            %>
                    <option value="<%= rs.getString("username") %>"><%= rs.getString("username") %></option>
            <%
                }
                if (!found) {
            %>
                    <option>No other users found</option>
            <%
                }
                rs.close();
                stmt.close();
                conn.close();
            %>
        </select><br>

        <textarea name="message" placeholder="Enter your message here..." required></textarea><br>

        <button type="submit">Encrypt & Send</button>
    </form>

    <form action="dashboard.jsp" method="get">
        <button type="submit"> Back to Dashboard</button>
    </form>
</div>

</body>
</html>
