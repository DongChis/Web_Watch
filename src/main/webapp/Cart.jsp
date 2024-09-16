<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link href="css/cart.css" rel="stylesheet">
<meta charset="UTF-8">
<title>Gio hang</title>
</head>
<body>
	<jsp:include page="Header.jsp" />
	<div class="container">
		<div class="cart-detail">
			<h1 class="page-title">Shopping Cart</h1>

			<div class="cart-items">
				<!-- Sản phẩm trong giỏ hàng -->
				<div class="cart-item">
					<div class="product-image">
						<img src="img/image1.png" alt="Product Image">
					</div>
					<div class="product-details">
						<h2 class="product-name">Product 1</h2>
						<p class="product-description">High quality, comfortable fit.</p>
						<div class="quantity">
							<label for="quantity1">Quantity:</label> <input type="number"
								id="quantity1" value="1" min="1">
						</div>
					</div>
					<div class="product-price">
						<span>$49.99</span>
						<button class="remove-item">Remove</button>
					</div>
				</div>

				<div class="cart-item">
					<div class="product-image">
						<img src="img/image3.png" alt="Product Image">
					</div>
					<div class="product-details">
						<h2 class="product-name">Product 2</h2>
						<p class="product-description">Stylish and durable material.</p>
						<div class="quantity">
							<label for="quantity2">Quantity:</label> <input type="number"
								id="quantity2" value="2" min="1">
						</div>
					</div>
					<div class="product-price">
						<span>$89.99</span>
						<button class="remove-item">Remove</button>
					</div>
				</div>
			</div>

			<!-- Tổng thanh toán -->
			<div class="cart-summary">
				<h2>Order Summary</h2>
				<div class="summary-item">
					<span>Subtotal</span> <span>$229.97</span>
				</div>
				<div class="summary-item">
					<span>Shipping</span> <span>Free</span>
				</div>
				<div class="summary-item">
					<span>Total</span> <span class="total-price">$229.97</span>
				</div>
				<button class="checkout-btn">Proceed to Checkout</button>
			</div>
		</div>
	</div>


	<jsp:include page="Footer.jsp" />

</body>
</html>