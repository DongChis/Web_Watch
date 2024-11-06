<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="css/productListAdmin.css" rel="stylesheet">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
<title>Insert title here</title>
</head>
<body>
	<div class="product-list-ad">
		<c:choose>
			<c:when test="${not empty orderDetail}">
				<h1
					style="background-color: blueviolet; border-radius: 8px; color: white; padding: 10px; margin: 10px 0px 0px 0px;">Chi
					tiết đơn hàng</h1>
				<div class="button-container">
					<a href="orderListAdmin" class="button">Danh sách đơn hàng</a> <a
						href="loadOrder?orderID=${order.orderID}" class="button" style="margin-right: 20px;">Edit</a>
				</div>
				<jsp:include page="OrderDetail.jsp" />
			</c:when>

			<c:when test="${not empty recentlyDelete}">

				<h1
					style="background-color: blueviolet; border-radius: 8px; color: white; padding: 10px; margin: 10px 0px 0px 0px;">
					Danh sách sản phẩm đã xóa gần đây</h1>

				<a href="productListAdmin" class="button" style="margin-left: 5px;">
					Danh sách sản phẩm</a>
				<jsp:include page="RecentlyDelete.jsp" />
			</c:when>
			<c:when test="${not empty orderEdit}">
			 <h1
					style="background-color: blueviolet; border-radius: 8px; color: white; padding: 10px; margin: 10px 0px 0px 0px;">
					Chỉnh sửa đơn hàng</h1> 
				<!-- 	<a href="ordertListAdmin" class="button" style="margin-left: 5px;">
					Danh sách đơn hàng</a> -->
				<jsp:include page="EditOrder.jsp" />
			</c:when>
			<c:otherwise>
				<h1
					style="background-color: blueviolet; border-radius: 8px; color: white; padding: 10px; margin: 10px 0px 0px 0px;">
					Danh sách đơn hàng</h1>
				<div class="button-container">
					<a href="recentlyDelete" class="button"> <i
						class="fas fa-trash-alt" style="margin-right: 10px;"></i>Đã xóa
						gần đây
					</a>
					<div class="search-container">
						<input type="text" placeholder="Tìm kiếm đơn hàng..."
							class="search-input"> <a href="#" class="button"><i
							class="fas fa-search"></i></a>
					</div>
				</div>

				<table>
					<thead>
						<tr>
							<th>ID</th>
							<th>Khách hàng</th>
							<th>Số lượng</th>
							<th>Giá trị đơn hàng</th>
							<th>Ngày đặt hàng</th>
							<th>Thông tin đơn hàng</th>
							<th>Hành động</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="order" items="${orderListAdmin}">
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
								<td><c:if test="${not empty sessionScope.accSession}">
										<!-- Nếu người dùng đã đăng nhập -->
										<button class="button"
											onclick="deleteOrder(${order.orderID})">Xóa</button>
									</c:if> <c:if test="${empty sessionScope.accSession}">
										<!-- Nếu người dùng chưa đăng nhập -->
										<button class="button" onclick="window.location.href='login'">Xóa</button>
									</c:if>
							</tr>
						</c:forEach>
						<!-- Add more products as needed -->
					</tbody>
				</table>
			</c:otherwise>
		</c:choose>
	</div>
	<script>
		function deleteOrder(orderID) {
			console.log("orderID ID to delete js:", orderID); // Debug log
			if (!orderID) {
				alert("orderID ID is required."); // Hiển thị thông báo nếu không có ID
				return; // Dừng hàm nếu không có ID
			}
			if (confirm("Bạn có chắc chắn muốn xóa đơn hàng này?")) {
				// Gọi đến servlet để xóa sản phẩm

				window.location.href = `deleteOrder?orderID=` + orderID;
				alert("Đã xóa thành công!");

			}
		}

		// Đảm bảo DOM đã được tải trước khi thêm sự kiện
		document.addEventListener("DOMContentLoaded", function() {
			// Không cần thêm gì ở đây nếu không có mã JavaScript khác
		});
	</script>
</body>
</html>