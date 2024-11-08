<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link href="css/cart.css" rel="stylesheet">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
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
                          <button class="remove-item" onclick="removeFromCart(${item.product.productID})">
                        <i class="fas fa-trash fa-lg"></i>  
                          </button>
                        </div>
                    </div>
                </c:forEach>
				

				
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
                <button class="checkout-btn" onclick="location.href='CheckOut.jsp';">Thanh toán</button>
            </div>
        </div>
    </div>
    <jsp:include page="Footer.jsp" />
   
  	<script src="js/cart.js" ></script>
</body>
</html>