<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liên Hệ</title>
    <link rel="stylesheet" href="css/contact.css">
</head>
<body>
<jsp:include page="Header.jsp" />

    <div class="contact-container">
    <nav class="breadcrumb">
			<a href="home">Trang chủ</a> | <span>Liên hệ</span>
		</nav>
        <h1>Liên Hệ Với Chúng Tôi</h1>
		
        <form action="/sendContact" method="post">
            <div class="form-group">
                <label for="name">Họ và Tên:</label>
                <input type="text" id="name" name="name" required placeholder="Nhập họ và tên của bạn">
            </div>

            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required placeholder="Nhập email của bạn">
            </div>

            <div class="form-group">
                <label for="message">Lời nhắn:</label>
                <textarea id="message" name="message" rows="5" required placeholder="Nhập lời nhắn của bạn"></textarea>
            </div>

            <button type="submit" class="submit-btn">Gửi Liên Hệ</button>
        </form>

        <!-- Thêm thông tin liên hệ -->
        <div class="contact-info">
            <h2>Thông Tin Liên Hệ</h2>
            <p><strong>Địa chỉ:</strong> 123 Đường Đồng Hồ, Thành phố Hà Nội, Việt Nam</p>
            <p><strong>Số điện thoại:</strong> +84 123 456 789</p>
            <p><strong>Email:</strong> <a href="mailto:support@dongho.com">support@dongho.com</a></p>
            <p><strong>Giờ làm việc:</strong> Thứ Hai - Thứ Sáu: 9:00 AM - 6:00 PM</p>
        </div>
    </div>

   <jsp:include page="Footer.jsp" />
</body>
</html>
