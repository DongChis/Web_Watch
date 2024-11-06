<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="css/editOrder.css">
<title>Insert title here</title>
</head>
<body>
	<div id="edit-form" class="edit-form">
		<form action="orderDetail?orderID=${order.orderID}" method="post" class="form-order">
			<!-- Order Information Section -->
			<div class="form-section">
				<h2>Edit Order Information</h2>
				<p>Đơn hàng : #${order.orderID}</p>
				<label for="orderDate">Order Date:</label> <input class="input-order"type="text"
					id="orderDate" name="orderDate" value="${order.orderDate}" />
			</div>

			<!-- Customer Information Section -->
			<div class="form-section">
				<h2>Edit Customer Information</h2>
				<label for="customerName">Customer Name:</label> <input class="input-order" type="text"
					id="customerName" name="customerName" value="${order.customerName}" />

				<label for="customerPhone">Phone Number:</label> <input class="input-order" type="text"
					id="customerPhone" name="customerPhone"
					value="${order.customerPhone}" /> <label for="customerEmail">Email:</label>
				<input  class="input-order"type="text" id="customerEmail" name="customerEmail"
					value="${order.customerEmail}" /> <label for="customerAddress">Address:</label>
				<input  class="input-order" type="text" id="customerAddress" name="customerAddress"
					value="${order.customerAddress}" />
			</div>

			<!-- Payment Information Section -->
			<div class="form-section">
				<h2>Edit Payment Information</h2>
				<label for="paymentMethod">Payment Method:</label> 
				<select
					id="paymentMethod" name="paymentMethod">
					<option value="credit-card"
						${order.paymentMethod == 'credit-card' ? 'selected="selected"' : ''}>Thẻ
						Tín Dụng</option>
					<option value="bank-transfer"
						${order.paymentMethod == 'bank-transfer' ? 'selected="selected"' : ''}>Chuyển
						Khoản Ngân Hàng</option>
					<option value="cash-on-delivery"
						${order.paymentMethod == 'cash-on-delivery' ? 'selected="selected"' : ''}>Thanh
						Toán Khi Nhận Hàng</option>
				</select>
			</div>
			
			<div class="form-section">
			<h2>Edit Product Information</h2>
			<div class="product">
			<table class="product-table">
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
                        
                    </c:forEach>
                    </table>
                    </div>
			</div>
			<!-- Submit Button -->
			<button class="btn-order" type="submit">Update Order</button>
		</form>
	</div>
</body>
</html>