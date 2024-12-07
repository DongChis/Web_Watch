<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Xác Thực Người Dùng</title>
    <link rel="stylesheet" href="css/authUser.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>
<body>
<div class="container">
    <h1>Xác Minh Người Mua - Chữ Ký Điện Tử</h1>
    <form action="/submitVerification" method="POST" id="verificationForm">
        <div class="form-group">
            <label for="fullName">Họ và tên:</label>
            <input type="text" id="fullName" name="fullName" required placeholder="Nhập họ và tên của bạn">
        </div>

        <div class="form-group">
            <label for="email">Email:</label>
            <input type="email" id="email" name="email" required placeholder="Nhập email của bạn">
        </div>

        <div class="form-group">
            <label for="phone">Số điện thoại:</label>
            <input type="text" id="phone" name="phone" required placeholder="Nhập số điện thoại của bạn">
        </div>

        <div class="form-group">
            <label for="identityFile">Tải lên giấy tờ tùy thân (CMND/CCCD):</label>
            <input type="file" id="identityFile" name="identityFile" required accept=".jpg, .jpeg, .png, .pdf">
        </div>

        <div class="form-group">
            <label for="signature">Chữ ký điện tử của bạn:</label>
            <canvas id="signature" class="signature-pad"></canvas>
            <button type="button" class="clear-btn" onclick="clearSignature()">Xóa chữ ký</button>
        </div>

        <button type="submit">Xác Minh</button>
    </form>

    <div class="footer">
        <p>&copy; 2024 Xác minh người mua - Chữ ký điện tử</p>
    </div>
</div>

<script>
    // Script for signature pad
    const canvas = document.getElementById("signature");
    const ctx = canvas.getContext("2d");

    canvas.width = canvas.offsetWidth;
    canvas.height = canvas.offsetHeight;

    let drawing = false;

    canvas.addEventListener("mousedown", startDrawing);
    canvas.addEventListener("mousemove", draw);
    canvas.addEventListener("mouseup", stopDrawing);
    canvas.addEventListener("mouseout", stopDrawing);

    function startDrawing(event) {
        drawing = true;
        draw(event);
    }

    function draw(event) {
        if (!drawing) return;
        ctx.lineWidth = 3;
        ctx.lineCap = "round";
        ctx.strokeStyle = "#000000";

        ctx.lineTo(event.clientX - canvas.offsetLeft, event.clientY - canvas.offsetTop);
        ctx.stroke();
    }

    function stopDrawing() {
        drawing = false;
        ctx.beginPath();
    }

    function clearSignature() {
        ctx.clearRect(0, 0, canvas.width, canvas.height);
    }
</script>
    
</body>
</html>



