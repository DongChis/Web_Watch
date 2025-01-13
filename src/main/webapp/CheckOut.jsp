<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link href="css/checkout.css" rel="stylesheet">
<meta charset="UTF-8">
<title>Thanh Toán</title>
</head>
<body>

	<jsp:include page="Header.jsp" />
	<div class="container-checkout">
		<form action="checkout" method="POST">

			<!-- Card 1: Thông tin sản phẩm và phương thức thanh toán -->
			<div class="card">
				<h2>Thông Tin Sản Phẩm & Thanh Toán</h2>

				<c:forEach var="item" items="${sessionScope.cart}">
					<div class="cart-item">
						<div class="product-image">
							<img src="${item.product.imageURL}" alt="${item.product.name}">
						</div>
						<div class="product-details">
							<h2 class="product-name">${item.product.name}</h2>
							<p class="product-description">${item.product.description}</p>
							<div class="quantity">
								<span>${item.quantity}</span>
							</div>
						</div>
						<div class="product-price">
							<span>${item.product.price * item.quantity} VND</span>
							<button type="button" class="remove-item"
								onclick="removeFromCart(${item.product.productID})">
								<i class="fas fa-trash fa-lg"></i>
							</button>
						</div>
					</div>
				</c:forEach>

				<div class="form-group">
					<label for="payment-method">Phương Thức Thanh Toán:</label> <select
						id="payment-method" name="payment-method" required>
						<option value="credit-card">Thẻ Tín Dụng</option>
						<option value="bank-transfer">Chuyển Khoản Ngân Hàng</option>
						<option value="cash-on-delivery">Thanh Toán Khi Nhận Hàng</option>
					</select>
				</div>

				<!-- Hiển thị tổng tiền -->
				<div class="total-amount">
					<h3>
						Tổng Tiền: <span> <c:set var="total" value="0" /> <c:forEach
								var="item" items="${sessionScope.cart}">
								<c:set var="total"
									value="${total + (item.product.price * item.quantity)}" />
							</c:forEach> ${total} VND
						</span>
					</h3>
				</div>
			</div>

			<!-- Card 2: Thông tin người nhận hàng -->
			<div class="card" style="height: 400px;">
				<h2>Thông Tin Người Nhận Hàng</h2>

				<div class="form-group">
					<label for="customer-name">Tên Người Nhận:</label> <input
						type="text" id="customer-name" name="customer-name" required>
				</div>

				<div class="form-group">
					<label for="customer-email">Email:</label> <input type="text"
						id="customer-email" name="customer-email" required>
				</div>

				<div class="form-group">
					<label for="customer-phone">Số Điện Thoại:</label> <input
						type="tel" id="customer-phone" name="customer-phone" required>
				</div>

				<div class="form-group">
					<label for="customer-address">Địa Chỉ:</label> <input type="text"
						id="customer-address" name="customer-address" required>
				</div>
				
			</div>

			<div class="card" style="height: 100px;">
				<button type="submit">Đặt Hàng</button>

			</div>

		</form>
	</div>

	<jsp:include page="Footer.jsp" />

</body>

</html>
