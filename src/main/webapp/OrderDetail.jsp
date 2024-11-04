<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Chi tiết đơn hàng</title>
<link rel="stylesheet" href="css/orderDetail.css">
</head>
<body>

	<div class="card-container">
		<div class="card order-detail">
			<h2>Order Information</h2>
			<table>
				<tr>
					<th>Đơn hàng</th>
					<td id="orderID"> ${order.orderID}</td>
				</tr>
				 <tr>
                <th>Thời gian đặt hàng</th>
                <td id="quantity">${order.orderDate}</td>
            </tr>
            <%-- <tr>
                <th>Tổng giá</th>
                <td id="total-price">${order.totalPrice}</td>
            </tr>  --%> 
			</table>
		</div>

		<div class="card payment-info">
			<h2>Payment Information</h2>
			<table>
				<tr>
					<th>Payment Method</th>
					<td id="paymentMethod">${order.paymentMethod}</td>
				</tr>
				<tr>
					<th>Transaction ID</th>
					<td id="transactionID">${paymentInfo.transactionID}</td>
				</tr>
				<tr>
					<th>Status</th>
					<td id="status">${paymentInfo.status}</td>
				</tr>
			</table>
		</div>

		<div class="card customer-info">
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
								<td><img src="${cartItem.product.imageURL}"
									alt="Product Image" class="product-image" /></td>
								<td class="product-details">
									<p class="product-name">Tên sản phẩm:
										${cartItem.product.name}</p>
									<p class="product-price">Giá: ${cartItem.product.price} VNĐ</p>
									<p class="product-quantity">Số lượng: ${cartItem.quantity}</p>
									<c:set var="itemTotalPrice"
										value="${cartItem.product.price * cartItem.quantity}" />
								</td>
							</tr>
							<c:set var="totalPrice" value="${totalPrice + itemTotalPrice}" />
						</c:forEach>
					</tbody>
				</table>
				<h3 class="total-price">Tổng tiền đơn hàng :
					${totalPrice} VNĐ</h3>
			</div>

		</div>


	</div>


</body>
</html>