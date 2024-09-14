
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="css/header.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
     <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
      
    <title>Watch Store</title>
</head>
<body>
      <!-- Header Top Bar -->
    <div class="header__top">
        <div class="left-info">
            <span><i class="fas fa-envelope"></i> watchstore@gmail.com</span>
            <span><i class="fas fa-car"></i> Free ship trên toàn quốc</span>
        </div>
        <div class="right-info">
            <a href="#"><i class="fas fa-heart"></i></a>
            <a href="#"><i class="fas fa-twitter"></i></a>
            <a href="#"><i class="fas fa-star"></i></a>
            <c:if test="${not empty sessionScope.accSession}">
            <h3>Xin chào ${sessionScope.accSession.username}<h3>
               <button class="btn btn-primary" onclick="redirectToServlet()" >
                    <i class="fas fa-user"></i> Đăng Xuất
                </button>
            </c:if>
            <c:if test="${empty sessionScope.accSession}">
                <button class="btn btn-primary" onclick="redirectToLoginPage()">
                    <i class="fas fa-user"> </i> Đăng nhập
                </button>
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
                <li><a href="contact.jsp">Liên hệ</a></li>
            </ul>
        </div>
        <div class="header-cart">
            <a href="cart.jsp"><i class="fas fa-shopping-cart"></i><span>2</span></a>
        </div>
    </div>

    <!-- Categories and Search Bar -->
    <div class="heros__categories_container">
		<div class="heros__categories" onclick="">
			<i class="menu-icon">&#9776; </i>
			 	Danh sách sản phẩm
		</div>

		<ul id="categoryList" style="display:none ;">
			<li>Đồng hồ Seiko</li>
			<li>Đồng hồ KOI</li>
			<!-- Thêm các mục khác -->
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
    <script>
  
    function redirectToLoginPage() {
        window.location.href = 'Login.jsp'; 
    }
    function redirectToServlet() {
        window.location.href = 'logout';
    }
    /* function toggleCategory() {
        var categoryList = document.getElementById('categoryList');
        if (categoryList.style.display === 'none') {
            categoryList.style.display = 'block';
        } else {
            categoryList.style.display = 'none';
        }
    } */
</script>
</body>
</html>
