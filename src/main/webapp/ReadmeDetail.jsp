<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hướng Dẫn Sử Dụng Chữ Ký Điện Tử</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f9f9f9;
            line-height: 1.6;
        }
        .container {
            width: 90%;
            max-width: 800px;
            margin: 20px auto;
            background: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
        }
        h1 {
            text-align: center;
            color: #333;
            margin-bottom: 20px;
        }
        .step {
            margin-bottom: 20px;
        }
        .step h2 {
            font-size: 1.2rem;
            color: #007bff;
            margin-bottom: 10px;
        }
        .step img {
            width: 100%;
            max-height: 300px;
            object-fit: cover;
            border-radius: 8px;
            border: 1px solid #ddd;
        }
    </style>
</head>
<body>
    <div class="container">
    
    <button id="backButton" class="btn"
		onclick="window.location.href='/Web_Watch/loadInfoUser';">
		<i class="fas fa-arrow-left"></i> Back
	</button>
        <h1>Hướng Dẫn Các Bước Sử Dụng Chữ Ký Điện Tử</h1>

        <div class="step">
            <h2>Bước 1: Mở công cụ chữ ký điện tử</h2>
            <img src="img\imageReadmeDetail\r1.jpg" alt="Hình minh họa bước 1">
        </div>

        <div class="step">
            <h2>Bước 2: Load file thông tin người dùng đã có</h2>
            <img src="img\imageReadmeDetail\r2.jpg" alt="Hình minh họa bước 2">
        </div>

        <div class="step">
            <h2>Bước 3: Load private key</h2>
            <img src="img\imageReadmeDetail\r3.jpg" alt="Hình minh họa bước 3">
        </div>

        <div class="step">
            <h2>Bước 4: Ký và hiển thị chữ ký điện tử</h2>
            <img src="img\imageReadmeDetail\r4.jpg" alt="Hình minh họa bước 5">
        </div>
    </div>
</body>
</html>
