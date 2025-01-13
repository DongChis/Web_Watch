<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Watch Store</title>
<link href="css/home.css" rel="stylesheet">
</head>
<body>
	<!-- Header -->
	<jsp:include page="Header.jsp" />

	<main class="container">
			<!-- Slideshow Section -->
			<section class="slideshow-container">
				<div class="slides">
					<img src="img/sl1.png" alt="Watch Image 1"> 
						
				</div>
			</section>


		<section class="flash-sale-banner">
			<img src="img/flashsale.png" alt="FLASH SALE TẾT RỘN RÀNG"
				class="flash-sale-image">
		</section>
		<section class="product-slider">
			<h2 class="slider-title">Mẫu Đẹp Hot Xu Hướng 2025</h2>
			<div class="slider-container">
				<button class="slider-arrow prev-btn"
					onclick="moveSlide('.highlight-slides', -3)">&#10094;</button>
				<div class="slider-track highlight-slides">
					<c:forEach var="product" items="${listAllProduct}">
						<article class="slider-item">
							<div class="product-card">
								<img
									src="${product.imageURL != null ? product.imageURL : 'img/placeholder.png'}"
									alt="${product.name}" class="product-image">
								<div class="product-info">
									<h3 class="product-name">${product.name}</h3>
									<p class="product-price">
										<span class="current-price">${product.price}đ</span>
									</p>
									<div class="rating">
										<i class="fa fa-star"></i><i class="fa fa-star"></i><i
											class="fa fa-star"></i><i class="fa fa-star"></i><i
											class="fa fa-star"></i>
									</div>
								</div>
								<div class="card-btn">
									<a href="detail?pid=${product.productID}" class="btn-view">Xem
										Chi Tiết</a>
									<button class="btn btn-add"
										onclick="addToCart(${product.productID}, 1)">Thêm vào
										Giỏ</button>
								</div>
							</div>
						</article>
					</c:forEach>
				</div>
				<button class="slider-arrow next-btn"
					onclick="moveSlide('.highlight-slides', 1)">&#10095;</button>
			</div>
		</section>

		<section class="product-slider">
			<h2 class="slider-title">Đồng Hồ Cao Cấp</h2>
			<div class="slider-container">
				<button class="slider-arrow prev-btn"
					onclick="moveSlide('.premium-watch-slides', -1)">&#10094;</button>
				<div class="slider-track premium-watch-slides">
					<c:forEach var="product" items="${listAllProduct}">
						<article class="slider-item">
							<div class="product-card">
								<img
									src="${product.imageURL != null ? product.imageURL : 'img/placeholder.png'}"
									alt="${product.name}" class="product-image">
								<div class="product-info">
									<h3 class="product-name">${product.name}</h3>
									<p class="product-description">${product.description}</p>
									<p class="product-price">
										<span class="current-price">${product.price} VND</span> <span
											class="original-price">9.250.000đ</span> <span
											class="discount">-32%</span>
									</p>
									<div class="rating">
										<i class="fa fa-star"></i><i class="fa fa-star"></i><i
											class="fa fa-star"></i><i class="fa fa-star"></i><i
											class="fa fa-star"></i>
									</div>
									<div class="card-btn">
										<a href="detail?pid=${product.productID}" class="btn-view">Xem
											Chi Tiết</a>
										<button class="btn btn-add"
											onclick="addToCart(${product.productID}, 1)">Thêm
											vào Giỏ</button>
									</div>
								</div>
							</div>
						</article>
					</c:forEach>
				</div>
				<button class="slider-arrow next-btn"
					onclick="moveSlide('.premium-watch-slides', 1)">&#10095;</button>
			</div>
		</section>

	</main>

	<!-- Footer -->
	<jsp:include page="Footer.jsp" />

	<!-- Scripts -->
	<script src="js/slide.js"></script>
	<script src="js/cart.js"></script>
	<script src="js/slidersp.js"></script>
</body>
</html>
