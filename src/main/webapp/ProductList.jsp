<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="css/productList.css" rel="stylesheet">
<title>Danh sách sản phẩm</title>
</head>
<body>

	<jsp:include page="Header.jsp" />

	<div class="container">
		<!-- Breadcrumb -->
		<nav class="breadcrumb">
			<a href="home">Trang chủ</a> | <span>Danh sách sản phẩm</span>
		</nav>

		<div class="row">
			<!-- Bộ lọc sản phẩm -->
			<aside class="filter-section">
				<h4>Bộ lọc sản phẩm</h4>
				<form action="productList" method="GET">

					<!-- Tìm kiếm theo tên -->
					<div class="filter-group">
						<h5>Tìm kiếm sản phẩm</h5>
						<input type="text" name="search"
							placeholder="Nhập tên sản phẩm..." value="${param.searchQuery}">
					</div>

					<!-- Lọc theo giới tính -->
					<div class="filter-group">
						<h5>Giới tính</h5>
						<div class="filter-item">
							<input type="radio" name="gender" value="Nam" id="gender-male"
								${param.gender == 'Nam' ? 'checked' : ''}> <label
								for="gender-male">Nam</label>
						</div>
						<div class="filter-item">
							<input type="radio" name="gender" value="Nữ" id="gender-female"
								${param.gender == 'Nữ' ? 'checked' : ''}> <label
								for="gender-female">Nữ</label>
						</div>
					</div>
					<!-- Lọc theo giá -->
					<div class="filter-group">
						<h5>Khoảng giá</h5>
						<div class="filter-item">
							<input type="radio" name="priceRange" value="0-1000000"
								id="price-1"> <label for="price-1">0 - 1,000,000
								VND</label>
						</div>
						<div class="filter-item">
							<input type="radio" name="priceRange" value="1000000-5000000"
								id="price-2"> <label for="price-2">1,000,000 -
								5,000,000 VND</label>
						</div>
						<div class="filter-item">
							<input type="radio" name="priceRange" value="5000000-20000000"
								id="price-3"> <label for="price-3">5,000,000 -
								20,000,000 VND</label>
						</div>
						<div class="filter-item">
							<input type="radio" name="priceRange" value="20000000-"
								id="price-4"> <label for="price-4">Trên
								20,000,000 VND</label>
						</div>
					</div>



					<!-- Nút áp dụng bộ lọc -->
					<button type="submit" class="btn-apply-filter">Áp dụng</button>
				</form>
			</aside>

			<!-- Danh sách sản phẩm -->
			<main class="product-list">
				<c:forEach var="product" items="${listAllProduct}">
					<div class="card">
						<img class="card-img-top" src="${product.imageURL}"
							alt="${product.name}">
						<div class="card-body">
							<h5 class="card-title">${product.name}</h5>
							<p class="card-text">${product.description}</p>
							<p class="product-price">
								<strong>${product.price} VND</strong>
							</p>
							<div class="card-btn">
								<a href="detail?pid=${product.productID}" class="btn-view">Xem</a>
								<button class="btn btn-add"
									onclick="addToCart(${product.productID}, 1)">Thêm</button>
							</div>
						</div>
					</div>
				</c:forEach>
			</main>
		</div>

		<!-- Phân trang -->
		<c:if test="${totalPages > 1}">
			<div class="pagination">
				<c:forEach var="i" begin="1" end="${totalPages}">
					<a href="productList?page=${i}"
						class="${i == currentPage ? 'active' : ''}"> ${i} </a>
				</c:forEach>
			</div>
		</c:if>
	</div>

	<jsp:include page="Footer.jsp" />

</body>
</html>
