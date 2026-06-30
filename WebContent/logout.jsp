<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Logout Successful</title>
    <style>
        body {
            margin: 0;
            height: 100vh;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            background: linear-gradient(135deg, #2b5876, #4e4376);
            color: white;
            font-family: "Poppins", sans-serif;
        }

        .message {
            background: rgba(255, 255, 255, 0.15);
            padding: 40px 60px;
            border-radius: 15px;
            text-align: center;
            box-shadow: 0 4px 25px rgba(0, 0, 0, 0.3);
            backdrop-filter: blur(8px);
            -webkit-backdrop-filter: blur(8px);
        }

        h2 {
            margin-bottom: 15px;
        }

        p {
            opacity: 0.9;
        }
    </style>
    <meta http-equiv="refresh" content="3;URL=login.jsp">
</head>
<body>
    <div class="message">
        <h2> You’ve been securely logged out.</h2>
        <p>Redirecting to Login page in 3 seconds...</p>
    </div>
</body>
</html>
