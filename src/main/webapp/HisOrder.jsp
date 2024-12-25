<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="css/productListAdmin.css" rel="stylesheet">
<title>Insert title here</title>
</head>
<body>
	<jsp:include page="Header.jsp" />

	<div class="containerr"
		style="max-width: 1200px; height: auto; margin: 0 auto 20px;">
		<div class="product-list-ad">
			<c:choose>
				<c:when test="${not empty orderDetail}">
					<h1
						style="background-color: blueviolet; border-radius: 8px; color: white; padding: 10px; margin: 10px 0px 0px 0px;">Chi
						tiết đơn hàng</h1>
					<div class="button-container">
						<a href="hisOrder" class="button"
							style="margin-right: 20px;">back</a>
					</div>
					<jsp:include page="OrderDetail.jsp" />
				</c:when>


				<c:otherwise>
					<h1
						style="background-color: blueviolet; border-radius: 8px; color: white; padding: 10px; margin: 10px 0px 0px 0px;">
						Lịch sử đơn hàng</h1>


					<table>
						<thead>
							<tr>
								<th>ID</th>
								<th>Khách hàng</th>
								<th>Số lượng</th>
								<th>Giá trị đơn hàng</th>
								<th>Ngày đặt hàng</th>
								<th>Thông tin đơn hàng</th>
								<th>Chỉnh sửa</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="order" items="${hisOrder}">
								<tr>
									<td>${order.orderID}</td>
									<td>${order.customerName}</td>
									<td>${order.cartItems.size()}</td>
									<td><c:set var="totalOrderPrice" value="0" /> <c:forEach
											var="item" items="${order.cartItems}">
											<c:set var="totalOrderPrice"
												value="${totalOrderPrice + item.totalPrice}" />
										</c:forEach> ${totalOrderPrice} VNĐ</td>
									<td>${order.orderDate}</td>
									<td><a href="orderDetail?orderID=${order.orderID}">Xem
											chi tiết</a></td>
									<td><a href="editOrderByUser?orderID=${order.orderID}">
											Chỉnh sửa</a></td>

								</tr>
							</c:forEach>
							<!-- Add more products as needed -->
						</tbody>
					</table>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
	<jsp:include page="Footer.jsp" />
</body>
</html>