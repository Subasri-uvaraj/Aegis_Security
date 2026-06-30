<%@ page import="java.sql.*, com.aegis.security.DBConnection, com.aegis.security.PasswordEncryption" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Group Chat</title>
<style>
body {
    margin: 0;
    padding: 0;
    background: linear-gradient(135deg, #2b5876, #4e4376);
    font-family: "Poppins", sans-serif;
    color: white;
    text-align: center;
}

.container {
    width: 80%;
    margin: 50px auto;
    background: rgba(255,255,255,0.1);
    border-radius: 15px;
    box-shadow: 0 4px 20px rgba(0,0,0,0.3);
    padding: 20px 40px;
}

h2 {
    margin-bottom: 15px;
}

textarea {
    width: 90%;
    height: 100px;
    border: none;
    border-radius: 10px;
    padding: 10px;
    font-size: 15px;
    resize: none;
    margin-top: 10px;
}

button {
    margin-top: 10px;
    padding: 10px 25px;
    border: none;
    border-radius: 8px;
    background: linear-gradient(90deg, #36d1dc, #5b86e5);
    color: white;
    font-weight: 600;
    cursor: pointer;
    transition: 0.3s;
}
button:hover {
    transform: scale(1.05);
    background: linear-gradient(90deg, #5b86e5, #36d1dc);
}

.message-box {
    background: rgba(255,255,255,0.1);
    border-radius: 10px;
    margin-top: 25px;
    padding: 15px;
    max-height: 400px;
    overflow-y: auto;
    text-align: left;
}

.message {
    background: rgba(255,255,255,0.15);
    padding: 10px;
    border-radius: 10px;
    margin-bottom: 10px;
}

.sender {
    font-weight: bold;
    color: #a3d8ff;
}

.time {
    font-size: 12px;
    opacity: 0.8;
}
</style>
</head>
<body>

<%
String username = (String) session.getAttribute("username");
if (username == null) {
    response.sendRedirect("login.jsp");
    return;
}
%>

<div class="container">
    <h2>Group Chat Room</h2>

    <form action="GroupChatServlet" method="post">
        <textarea name="message" placeholder="Type your message here..." required></textarea><br>
        <button type="submit">Send to All</button>
    </form>

    <div class="message-box">
        <%
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "SELECT sender, encrypted_message, timestamp FROM messages WHERE receiver='ALL' ORDER BY timestamp DESC"
            );
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String sender = rs.getString("sender");
                String decrypted = PasswordEncryption.decrypt(rs.getString("encrypted_message"));
                Timestamp time = rs.getTimestamp("timestamp");
        %>
                <div class="message">
                    <span class="sender"><%= sender %>:</span> <%= decrypted %><br>
                    <span class="time"><%= time %></span>
                </div>
        <%
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            out.println("<p style='color:red;'>Error loading messages: " + e.getMessage() + "</p>");
        }
        %>
    </div>

    <form action="dashboard.jsp" method="get">
        <button type="submit"> Back to Dashboard</button>
    </form>
</div>

</body>
</html>
