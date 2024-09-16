<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet"
	href="https://use.fontawesome.com/releases/v6.4.2/css/all.css">
<link href="css/home.css" rel="stylesheet" type="text/css">
<title>Watch Store</title>
</head>
<body>
	<div class="container">

		<jsp:include page="Header.jsp">
			<jsp:param name="pageTitle" value="Watch Shop - Home" />
		</jsp:include>

		<div class="slideshow-container">
			<div class="slides">
				<img src="img/image1.png" alt="Ảnh 1"> <img src="img/image2.jpg"
					alt="Ảnh 2"> <img src="img/image3.png" alt="Ảnh 3">
			</div>		
		</div>

		<div class="container-body">
			<div class="product-1">
				<div class="title">
					<h2>Sản phẩm mới ra mắt</h2>
				</div>
				<div>LOADING...</div>
			</div>
			<div class="product-2">
				<div class="title">
					<h2>Sản phẩm bán chạy</h2>
				</div>
				<div>LOADING...</div>
			</div>
			<div class="product-3">
				<div class="title">
					<h2>Sản phẩm vừa xem</h2>
				</div>
				<div>LOADING...</div>
			</div>
			<!-- Form Bình Luận -->
			<div class="review-form">
				<h2>Gửi Đánh Giá Của Bạn</h2>
				<form action="#" method="post">
					<label for="name">Tên:</label> <input type="text" id="name"
						name="name" required> <label for="rating">Đánh Giá
						(1-5):</label> <select id="rating" name="rating" required>
						<option value="5">5 Sao</option>
						<option value="4">4 Sao</option>
						<option value="3">3 Sao</option>
						<option value="2">2 Sao</option>
						<option value="1">1 Sao</option>
					</select> <label for="comment">Nhận Xét:</label>
					<textarea id="comment" name="comment" rows="4" required></textarea>

					<button type="submit">Gửi Đánh Giá</button>
				</form>
			</div>

			<!-- Phần Đánh Giá Đã Được Gửi -->
			<div class="reviews">
				<h2>Đánh Giá Khách Hàng</h2>
				<div class="review-item">
					<h3>Nguyễn Văn A</h3>
					<p>
						<strong>Đánh Giá:</strong> 5 Sao
					</p>
					<p>Đồng hồ rất đẹp và chất lượng tuyệt vời!</p>
				</div>
				<div class="review-item">
					<h3>Trần Thị B</h3>
					<p>
						<strong>Đánh Giá:</strong> 4 Sao
					</p>
					<p>Đồng hồ hoạt động tốt, nhưng dây đeo hơi chật.</p>
				</div>
			</div>

		</div>

		<jsp:include page="Footer.jsp" />

	</div>
	<script src="js/slide.js"></script>
</body>
</html>