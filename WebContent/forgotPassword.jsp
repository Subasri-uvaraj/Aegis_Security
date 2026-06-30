<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Forgot Password - Aegis Security Gateway</title>
    <style>
        body {
            background: linear-gradient(135deg, #232526, #414345);
            font-family: "Poppins", sans-serif;
            color: white;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .card {
            background: rgba(255, 255, 255, 0.1);
            padding: 40px;
            border-radius: 12px;
            text-align: center;
            width: 350px;
            box-shadow: 0 4px 25px rgba(0, 0, 0, 0.3);
        }

        input {
            width: 90%;
            padding: 10px;
            margin: 8px 0;
            border-radius: 6px;
            border: none;
            outline: none;
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
        }
    </style>
</head>
<body>

    <div class="card">
        <h2> Forgot Password</h2>
        <form action="ForgotPasswordServlet" method="post">
            <input type="text" name="username" placeholder="Enter your username" required><br>
            <input type="email" name="email" placeholder="Enter your registered email" required><br>
            <button type="submit">Send OTP</button>
        </form>
        <p><a href="login.jsp" style="color:#ddd;">Back to Login</a></p>
    </div>

</body>
</html>
