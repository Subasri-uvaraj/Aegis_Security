<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Password Updated Successfully</title>
    <style>
        body {
            background: linear-gradient(135deg, #36d1dc, #5b86e5);
            color: white;
            font-family: "Poppins", sans-serif;
            text-align: center;
            margin-top: 150px;
        }
        .box {
            display: inline-block;
            background: rgba(255, 255, 255, 0.15);
            padding: 40px 60px;
            border-radius: 15px;
            box-shadow: 0 4px 25px rgba(0, 0, 0, 0.3);
            backdrop-filter: blur(8px);
            -webkit-backdrop-filter: blur(8px);
            animation: fadeIn 0.8s ease-in-out;
        }
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(15px); }
            to { opacity: 1; transform: translateY(0); }
        }
        h2 {
            color: #ffffff;
            margin-bottom: 15px;
        }
        p {
            color: #f3f3f3;
            font-size: 15px;
            margin-bottom: 25px;
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
        .note {
            font-size: 14px;
            opacity: 0.8;
            margin-top: 15px;
        }
    </style>
    <!-- Auto-redirect after 3 seconds -->
    <meta http-equiv="refresh" content="3;URL=dashboard.jsp">
</head>
<body>
    <div class="box">
        <h2> Password Updated Successfully!</h2>
        <p>Your new credentials have been securely stored with AES encryption.</p>
        <form action="dashboard.jsp">
            <button type="submit" class="btn"> Back to Dashboard</button>
        </form>
        <p class="note">You’ll be redirected automatically in a few seconds...</p>
    </div>
</body>
</html>
