<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="css/authOrder.css">
</head>
<body>
	<div class="order-confirmation">
		<h1 class="title">Xác Nhận Đơn Hàng</h1>

		<!-- Thông tin đơn hàng -->
		<section class="order-info">
			
	<div class="card-container">
    <div class="card order-detail">
        <!-- Edit Button as a Link -->
        
        <h2>Order Information</h2>
        <table>
            <tr>
                <th>Đơn hàng</th>
                <td id="orderID">${order.orderID}</td>
            </tr>
            <tr>
                <th>Thời gian đặt hàng</th>
                <td id="quantity">${order.orderDate}</td>
            </tr>
        </table>
    </div>

    <div class="card payment-info">
        <!-- Edit Button as a Link -->
        
        <h2>Payment Information</h2>
        <table>
            <tr>
                <th>Payment Method</th>
                <td id="paymentMethod">${order.paymentMethod}</td>
            </tr>
            
        </table>
    </div>

    <div class="card customer-info">
        <!-- Edit Button as a Link -->
        
        <h2>Customer Information</h2>
        <table>
            <tr>
                <th>Tên khách hàng</th>
                <td id="customerName">${order.customerName}</td>
            </tr>
            <tr>
                <th>Số điện thoại</th>
                <td id="phoneNumber">${order.customerPhone}</td>
            </tr>
            <tr>
                <th>Email</th>
                <td id="email">${order.customerEmail}</td>
            </tr>
            <tr>
                <th>Địa chỉ nhận hàng</th>
                <td id="address">${order.customerAddress}</td>
            </tr>
        </table>
    </div>

    <div class="card product-info">
        <!-- Edit Button as a Link -->
        
        <h2>Thông tin sản phẩm</h2>
        <div class="product">
            <table class="product-table">
                <thead>
                    <tr>
                        <th>Hình ảnh sản phẩm</th>
                        <th>Chi tiết sản phẩm</th>
                    </tr>
                </thead>
                <tbody>
                    <c:set var="totalPrice" value="0" />
                    <c:forEach var="cartItem" items="${order.cartItems}">
                        <tr>
                            <td><img src="${cartItem.product.imageURL}" alt="Product Image" class="product-image" /></td>
                            <td class="product-details">
                                <p class="product-name">Tên sản phẩm: ${cartItem.product.name}</p>
                                <p class="product-price">Giá: ${cartItem.product.price} VNĐ</p>
                                <p class="product-quantity">Số lượng: ${cartItem.quantity}</p>
                                <c:set var="itemTotalPrice" value="${cartItem.product.price * cartItem.quantity}" />
                            </td>
                        </tr>
                        <c:set var="totalPrice" value="${totalPrice + itemTotalPrice}" />
                    </c:forEach>
                </tbody>
            </table>
            <h3 class="total-price">Tổng tiền đơn hàng: ${totalPrice} VNĐ</h3>
        </div>
    </div>
</div>
			<a href="" class="btn">Kí đơn hàng</a>
		</section>

		<!-- Thông tin khóa -->
		<section class="license-info">
			<h2>Thông Tin Khóa</h2>
			<p>
				<strong>Public Key:</strong> abc123xyz456
			</p>
			<p>
				<strong>Hạn sử dụng:</strong> 31/12/2025
			</p>
		</section>

		

		<!-- Mã hash -->
		<section class="hash-info">
			<h2>Mã Hash</h2>
			<p class="hash">e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855</p>
		</section>
	</div>
</body>
</html>