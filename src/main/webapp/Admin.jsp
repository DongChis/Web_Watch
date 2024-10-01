<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="css/admin.css" rel="stylesheet">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
<title>admin</title>
</head>
<body>
	<div class="containner">
		<div class="top-element">
			<div class="logo">
				<h1>WATCH STORE</h1>
			</div>
		</div>
		<div class="body-element">
			<div class="left-element">
				<ul class="parent">
					<li><i class="fas fa-home"></i><a class="li-child" 
								href="admin"> Home</a></li>

					<li><i class="fas fa-truck"></i> Sản phẩm <i
						class="fas fa-caret-down" style="float: right;"></i>
						<ul class="child">
							<li><a class="li-child">Danh sách sản phẩm</a></li>
							<li><a class="li-child" id="addProductLink"
								href="addNewProduct">Thêm sản phẩm mới</a></li>


						</ul></li>
					<li><i class="fas fa-file-invoice"></i> Đơn hàng <i
						class="fas fa-caret-down" style="float: right;"></i>
						<ul class="child">
							<li>Danh sách đơn hàng</li>

							<li>Lịch sử đơn hàng</li>
						</ul></li>
					<li><i class="fa fa-users"></i> Khách hàng <i
						class="fas fa-caret-down" style="float: right;"></i>
						<ul class="child">
							<li>Danh sách khách hàng</li>
							<li>Chỉnh sửa thông tin</li>

						</ul></li>
					<li><i class="fas fa-user-circle"></i> Tài khoản <i
						class="fas fa-caret-down" style="float: right;"></i>
						<ul class="child">
							<li>Danh sách tài khoản</li>
							<li>Lịch sử đăng nhập</li>
						</ul></li>
					<li><i class="fas fa-trash"></i> Đã xóa gần đây <i
						class="fas fa-caret-down" style="float: right;"></i>
						<ul class="child">
							<li>Sản phẩm</li>
							<li>Đơn hàng</li>
							<li>Tài khoản</li>
						</ul></li>
					<li><i class="fas fa-store"></i> Cửa hàng</li>
					<li><i class="fas fa-hashtag"></i> App</li>
					<li><i class="fas fa-tools"></i> Tùy chỉnh</li>
				</ul>
			</div>

			<div class="main-element">
				<div class="main-element-top">
					<div class="hero__search_form">
						<form action="search.jsp" method="get">
							<input type="text" placeholder="Search" name="searchTerm">
							<button type="submit">Tìm kiếm</button>
						</form>
					</div>
					<div class="info">
						<a href="#"><i class="fas fa-bell fa-lg"
							style="margin-right: 20px; color: hotpink;"
							style="margin-right: 10px;"></i></a> <a href="#"><i
							class="fas fa-language fa-lg"
							style="margin-right: 20px; color: green;"
							style="margin-right: 10px;"></i></a>
						<c:if test="${not empty sessionScope.accSession}">
							<div style="margin-right: 10px;">Xin chào
								${sessionScope.accSession.username}</div>


							<button class="btn btn-primary" onclick="redirectToServlet()">
								<i class="fas fa-user" style="margin-right: 7px;"></i> Đăng Xuất
							</button>
						</c:if>
						<c:if test="${empty sessionScope.accSession}">
							<button class="btn btn-primary" onclick="redirectToLoginPage()">
								<i class="fas fa-user" style="margin-right: 7px;"> </i> Đăng
								nhập
							</button>
						</c:if>

						<a href="#"><i class="fas fa-cog fa-lg"></i></a>
					</div>
				</div>

				<div class="main-element-body">
					<div class="statistical-info">
						<div class="box">
							<div class="statistical-info-icon" style="color: green;">
								<i class="fas fa-dollar-sign"></i>
							</div>
							<div class="statistical-text" style="color: green;">
								Doanh thu<span>10,352,550</span>
							</div>
						</div>
						<div class="box">
							<div class="statistical-info-icon" style="color: hotpink;">
								<i class="fas fa-money-bill"></i>
							</div>
							<div class="statistical-text" style="color: hotpink;">
								Chi phí<span>3,752,350</span>
							</div>
						</div>
						<div class="box">
							<div class="statistical-info-icon" style="color: blueviolet;">
								<i class="far fa-smile"></i></i>
							</div>
							<div class="statistical-text" style="color: grebluevioleten;">
								Đánh giá<span>2,232</span>
							</div>
						</div>
						<div class="box">
							<div class="statistical-info-icon" style="color: lightskyblue;">
								<i class="fas fa-store "></i>
							</div>
							<div class="statistical-text" style="color: lightskyblue;">
								Cửa hàng<span>65</span>
							</div>
						</div>
					</div>
					<div class="date-info">
						<div class="time-control">
							<div class="nav-item active" onclick="setActive(this)">Ngày</div>
							<div class="nav-item" onclick="setActive(this)">Tuần</div>
							<div class="nav-item" onclick="setActive(this)">Tháng</div>
							<div class="nav-item" onclick="setActive(this)">Năm</div>
						</div>

						<div class="filter-bar">
							<div class="filter-item">
								<input type="date" id="dateInput" class="date-input">
							</div>
						</div>
					</div>

					<c:choose>
						<c:when
							test="${not empty addNewProduct}">
							<!-- Hiển thị form thêm sản phẩm -->
							<form action="addNewProduct" method="post"
								class="form-add-product">
								<label for="productName">Tên sản phẩm:</label> <input
									type="text" id="productName" name="productName" required>

								<label for="productPrice">Giá sản phẩm:</label> <input
									type="number" id="productPrice" name="productPrice" required>

								<label for="productDescription">Mô tả sản phẩm:</label>
								<textarea id="productDescription" name="productDescription"
									required></textarea>

								<label for="productImage">Ảnh sản phẩm:</label> <input
									type="file" id="productImage" name="productImage"
									accept="image/*" required>

								<button type="submit">Thêm sản phẩm</button>
							</form>
						</c:when>
						<c:otherwise>
							<div class="data-table">
								<div class="box-data">
									<div class="customer-info">
										Khách hàng<span>10,352</span>
									</div>
									<i class="far fa-user fa-lg" style="color: orange;"></i>
								</div>
								<div class="box-data">
									<div class="customer-info">
										Đơn hàng<span>10,352</span>
									</div>
									<i class="fas fa-shopping-cart fa-lg"
										style="color: lightskyblue;"></i>
								</div>
								<div class="box-data">
									<div class="customer-info">
										Gián bán trung bình<span>14,035,222</span>
									</div>
									<i class="fas fa-percent fa-lg" style="color: purple;"></i>
								</div>
								<div class="box-data">
									<div class="customer-info">
										Sản phẩm bán<span>102,352</span>
									</div>
									<i class="fab fa-product-hunt fa-lg" style="color: red;"></i>
								</div>
								<div class="box-data">
									<div class="customer-info">
										Tổng doanh số<span>1,252</span>
									</div>
									<i class="fas fa-money-bill-wave-alt fa-lg"
										style="color: navy;"></i>
								</div>
								<div class="box-data">
									<div class="customer-info">
										Khách hàng ghé thăm<span>7,352</span>
									</div>
									<i class="fas fa-users fa-lg" style="color: hotpink;"></i>
								</div>
								<div class="box-data">
									<div class="customer-info">
										Tông số sản phẩm<span>300,352</span>
									</div>
									<i class="fas fa-border-all fa-lg" style="color: blueviolet;"></i>
								</div>
								<div class="box-data">
									<div class="customer-info">
										Sản phẩm bán chạy<span>352</span>
									</div>
									<i class="fas fa-thumbs-up fa-lg" style="color: green;"></i>
								</div>
								<div class="box-data">
									<div class="customer-info">
										Đại lí<span>52</span>
									</div>
									<i class="fas fa-store fa-lg" style="color: lightskyblue;"></i>
								</div>
							</div>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
	</div>

	<script src="js/admin.js"></script>
	<script src="js/login.js"></script>
	
</body>
</html>