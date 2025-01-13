<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<link href="css/cart.css" rel="stylesheet">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
<meta charset="UTF-8">
<title>Giỏ hàng</title>
</head>
<body>
	<jsp:include page="Header.jsp" />

	<div class="container">
		<nav class="breadcrumb">
			<a href="home">Trang chủ</a> | <span>Giỏ hàng</span>
		</nav>
		<div class="cart-detail">
			<h1 class="page-title">Giỏ hàng</h1>

			<div class="cart-items">
				<!-- Kiểm tra xem giỏ hàng có rỗng không -->
				<c:if test="${empty sessionScope.cart}">
					<div class="empty-cart-message">
						<p>Không có sản phẩm nào trong giỏ hàng</p>
						<a href="home">Về trang chủ</a>

					</div>
				</c:if>

				<!-- Hiển thị sản phẩm nếu giỏ hàng không trống -->
				<c:if test="${not empty sessionScope.cart}">
					<c:forEach var="item" items="${sessionScope.cart}">
						<div class="cart-item" data-id="${item.product.productID}">
							<div class="product-image">
								<img src="${item.product.imageURL}" alt="${item.product.name}">
							</div>
							<div class="product-details">
								<h2 class="product-name">${item.product.name}</h2>
								<p class="product-description">${item.product.description}</p>
								<div class="quantity">
									<!-- Nút trừ, số lượng, nút cộng -->
									<button class="quantity-btn"
										onclick="updateQuantity(${item.product.productID},-1)">-</button>
									<span class="quantity-display">${item.quantity}</span>
									<button class="quantity-btn"
										onclick="updateQuantity(${item.product.productID},1)">+</button>
								</div>
							</div>
							<div class="product-price">
								<strong> <fmt:formatNumber
										value="${item.product.price * item.quantity}" pattern="#,###" />
									VND
								</strong>

								<button class="remove-item"
									onclick="removeFromCart(${item.product.productID})">
									<i class="fas fa-trash fa-lg"></i>
								</button>
							</div>
						</div>
					</c:forEach>
				</c:if>
			</div>

			<!-- Tổng thanh toán nếu giỏ hàng không trống -->
			<c:if test="${not empty sessionScope.cart}">
				<div class="cart-summary">
					<h2 class="summary-title">Tổng thanh toán</h2>

					<!-- Tính tổng giá trị giỏ hàng -->
					<c:set var="totalPrice" value="0" />
					<c:forEach var="item" items="${sessionScope.cart}">
						<c:set var="totalPrice"
							value="${totalPrice + item.product.price * item.quantity}" />
					</c:forEach>

					<div class="summary-item">
						<span class="summary-label">Tổng:</span> <span
							class="summary-value"> <strong> <fmt:formatNumber
									value="${totalPrice}" pattern="#,###" /> VND
						</strong>
						</span>
					</div>

					<button class="checkout-btn" onclick="location.href='checkout';">Tiếp
						Tục</button>
				</div>
			</c:if>
		</div>
	</div>

	<jsp:include page="Footer.jsp" />

	<script src="js/cart.js"></script>
</body>
</html>
