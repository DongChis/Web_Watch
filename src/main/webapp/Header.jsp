<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="css/topHome.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
<title>Watch Store</title>
</head>
<body>
	<!-- Header Top Bar -->
	<div class="header__top">
		<div class="left-info">
			<span><i class="fas fa-envelope"></i> watchstore@gmail.com</span> <span><i
				class="fas fa-car"></i> Free ship trên toàn quốc</span>
		</div>
		<div class="right-info">
			<a href="#"><i class="fas fa-heart"></i></a> <a href="hisOrder"><i
				class="fas fa-twitter"></i></a> <a href="#"><i class="fas fa-star"></i></a>
				<a href="keyControl" id="keyButton" > <i class="fas fa-key fa-lg"
					style="color: blue;"></i>
				</a>


			<!-- Hiển thị thông tin session -->
			<c:if test="${not empty sessionScope.accSession}">
				<h3>Xin chào ${sessionScope.accSession.username}</h3>
				<a href="logout" style="text-decoration: none;">
					<button class="btn btn-primary">
						<i class="fas fa-user"></i> Đăng Xuất
					</button>
				</a>
			</c:if>
			<c:if test="${empty sessionScope.accSession}">
				<a href="login" style="text-decoration: none;">
					<button class="btn btn-primary">
						<i class="fas fa-user"></i> Đăng nhập
					</button>
				</a>
			</c:if>
		</div>
	</div>

	<!-- Main Header -->
	<div class="main-header">
		<div class="logo">
			<h1>WATCH STORE</h1>
		</div>
		<div class="nav-menu">
			<ul>
				<li><a href="home">Trang chủ</a></li>
				<li><a href="about.jsp">Giới thiệu</a></li>
				<li><a href="productList">Sản phẩm</a></li>
				<li><a href="blogs.jsp">Bài viết</a></li>
				<li><a href="detail">Liên hệ</a></li>
			</ul>
		</div>
		<div class="header-cart">
			<a href="add-to-cart"> <i class="fas fa-shopping-cart"></i> <span
				id="cart-count"> ${sessionScope.totalQuantity != null ? sessionScope.totalQuantity : 0}
			</span>
			</a>
		</div>
	</div>

	<!-- Categories and Search Bar -->
	<div class="heros__categories_container">
		<div class="heros__categories" onclick="toggleCategory()">
			<i class="fa-solid fa-bars"></i> Danh sách sản phẩm
		</div>
		<ul id="categoryList" style="display: none;">
			<li>Đồng hồ Seiko</li>
			<li>Đồng hồ KOI</li>
		</ul>

		<div class="hero__search_form">
			<form action="search.jsp" method="get">
				<input type="text" placeholder="Bạn đang cần gì?" name="searchTerm">
				<button type="submit">Tìm kiếm</button>
			</form>
		</div>
		<div class="hero__search_phone">
			<div class="hero__search_phone_icon">
				<i class="fas fa-phone"></i>
			</div>
			<div class="hero__search_phone_text">
				<p>0123.456.789</p>
				<span>Hỗ trợ 24/7</span>
			</div>
		</div>
	</div>

	<!-- Scripts -->
	<script src="js/login.js"></script>
	<script src="js/cart.js"></script>
	<script>
    
    const isLoggedIn = ${not empty sessionScope.accSession}; // Assuming this logic is available in your JSP

    // Adding the click event for the key button
    document.getElementById("keyButton").addEventListener("click", function(event) {
        if (!isLoggedIn) {
            // Prevent the default behavior (redirect to Function.jsp)
            event.preventDefault();
            // Redirect the user to the login page if not logged in
            window.location.href = 'login';
        }
     
    });
  
</script>



</body>
</html>
