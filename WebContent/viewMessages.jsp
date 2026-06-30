<%@ page import="java.sql.*, com.aegis.security.DBConnection, com.aegis.security.PasswordEncryption" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>View Messages</title>
<style>
body {
    background: linear-gradient(135deg, #2b5876, #4e4376);
    font-family: "Poppins", sans-serif;
    color: white;
    text-align: center;
    padding-top: 40px;
}

table {
    width: 85%;
    margin: auto;
    border-collapse: collapse;
    background: rgba(255,255,255,0.1);
    border-radius: 10px;
    overflow: hidden;
    box-shadow: 0 4px 20px rgba(0,0,0,0.3);
}

th, td {
    padding: 12px;
    border-bottom: 1px solid rgba(255,255,255,0.3);
}

th {
    background-color: rgba(255,255,255,0.2);
}

tr:hover {
    background: rgba(255,255,255,0.15);
}

h2 {
    margin-bottom: 25px;
}
</style>
</head>
<body>
<h2>Your Messages</h2>

<%
String username = (String) session.getAttribute("username");
if (username == null) {
    response.sendRedirect("login.jsp");
    return;
}

Connection conn = null;
PreparedStatement ps = null;
ResultSet rs = null;

try {
    conn = DBConnection.getConnection();

    // ✅ Fetch personal + group messages (receiver='ALL' visible to everyone)
    String sql = "SELECT * FROM messages WHERE sender=? OR receiver=? OR receiver='ALL' ORDER BY timestamp DESC";
    ps = conn.prepareStatement(sql);
    ps.setString(1, username);
    ps.setString(2, username);
    rs = ps.executeQuery();
%>

<table>
<tr>
    <th>From</th>
    <th>To</th>
    <th>Decrypted Message</th>
    <th>Time</th>
</tr>

<%
    boolean hasMessages = false;

    while (rs.next()) {
        hasMessages = true;
        String sender = rs.getString("sender");
        String receiver = rs.getString("receiver");
        String decrypted = PasswordEncryption.decrypt(rs.getString("encrypted_message"));
        Timestamp time = rs.getTimestamp("timestamp");
%>
<tr>
    <td><%= sender %></td>
    <td><%= receiver.equals("ALL") ? "Group (All Users)" : receiver %></td>
    <td><%= decrypted %></td>
    <td><%= time %></td>
</tr>
<%
    }

    if (!hasMessages) {
%>
<tr>
    <td colspan="4">No messages found.</td>
</tr>
<%
    }
} catch (Exception e) {
    out.println("<tr><td colspan='4' style='color:red;'>Error: " + e.getMessage() + "</td></tr>");
} finally {
    if (rs != null) rs.close();
    if (ps != null) ps.close();
    if (conn != null) conn.close();
}
%>
</table>

<br>
<form action="dashboard.jsp" method="get">
    <button type="submit"> Back to Dashboard</button>
</form>

</body>
</html>
