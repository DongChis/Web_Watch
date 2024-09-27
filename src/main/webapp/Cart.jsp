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
					 <c:forEach var="item" items="${sessionScope.cart}" >
                    <div class="cart-item">
                        <div class="product-image">
                            <img src="${item.product.imageURL}" alt="${item.product.name}">
                        </div>
                        <div class="product-details">
                            <h2 class="product-name">${item.product.name}</h2>
                            <p class="product-description">${item.product.description}</p>
                            <div class="quantity">
                                <span>Số lượng: ${item.quantity}</span>
                            </div>
                        </div>
                        <div class="product-price">
                            <span>${item.product.price * item.quantity} VND</span>
                          <button onclick="removeFromCart(${item.product.productID})">Xóa</button>
                        </div>
                    </div>
                </c:forEach>
				</div>

				
			</div>

			 <!-- Tổng thanh toán -->
            <div class="cart-summary">
                <h2>Tổng thanh toán</h2>
                <div class="summary-item">
                    <span>Tổng:</span> 
                    <span>
                        <c:set var="totalPrice" value="0" />
                        <c:forEach var="item" items="${sessionScope.cart}">
                            <c:set var="totalPrice" value="${totalPrice + (item.product.price * item.quantity)}" />
                        </c:forEach>
                        ${totalPrice} VND
                    </span>
                </div>
                <button class="checkout-btn">Thanh toán</button>
            </div>
        </div>
    </div>
    <jsp:include page="Footer.jsp" />
   
  	<script src="js/cart.js" ></script>
</body>
</html>