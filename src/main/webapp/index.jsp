<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>MICRO-JAVA Admin App</title>
    <style>
        body {
            margin: 0;
            min-height: 100vh;
            display: grid;
            place-items: center;
            font-family: "Segoe UI", sans-serif;
            color: #e7eef8;
            background: radial-gradient(circle at top left, #121a2b, #05070c 45%);
        }
        .card {
            width: min(560px, 92vw);
            border: 1px solid #24324a;
            border-radius: 14px;
            padding: 28px;
            background: linear-gradient(180deg, #0f1420, #151c2b);
            box-shadow: 0 16px 38px rgba(0,0,0,0.35);
        }
        h2 { margin-top: 0; }
        p { color: #a0adbf; }
        a {
            display: inline-block;
            text-decoration: none;
            border: 1px solid #4f6623;
            background: linear-gradient(180deg, #afdd46, #8fbf35);
            color: #10210a;
            font-weight: 700;
            border-radius: 10px;
            padding: 10px 14px;
        }
    </style>
</head>
<body>
<div class="card">
    <h2>MICRO-JAVA Web Application</h2>
    <p>Admin module for employees, schedules, salaries, and full system data control.</p>
    <div style="display:flex; flex-wrap:wrap; gap:12px;">
        <a href="<%=request.getContextPath()%>/admin/login">Open Admin Login</a>
        <a href="<%=request.getContextPath()%>/employee/login">Open Employee Login</a>
    </div>
</div>
</body>
</html>

