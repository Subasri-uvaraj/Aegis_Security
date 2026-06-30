<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Added Successfully</title>
    <style>
        body {
            background: linear-gradient(135deg, #36d1dc, #5b86e5);
            color: white;
            font-family: "Poppins", sans-serif;
            text-align: center;
            margin-top: 150px;
        }
        .btn {
            background-color: rgba(255, 255, 255, 0.2);
            border: none;
            padding: 12px 25px;
            color: white;
            border-radius: 8px;
            font-size: 16px;
            cursor: pointer;
            transition: 0.3s;
        }
        .btn:hover {
            background-color: rgba(255, 255, 255, 0.4);
        }
    </style>
</head>
<body>
    <h2> New User Added Successfully!</h2>
    <form action="dashboard.jsp">
        <button type="submit" class="btn"> Back to Dashboard</button>
    </form>
</body>
</html>
