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

	<div class="containerr" style="max-width: 1200px; height: 400px; margin: 0 auto 20px;"
	>
		<table>
			<thead>
				<tr>
					<th>ID</th>
					<th>Khách hàng</th>
					<th>Số lượng</th>
					<th>Giá trị đơn hàng</th>
					<th>Ngày đặt hàng</th>
					<th>Thông tin đơn hàng</th>

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

					</tr>
				</c:forEach>
				<!-- Add more products as needed -->
			</tbody>
		</table>
	</div>
	<jsp:include page="Footer.jsp" />
</body>
</html>