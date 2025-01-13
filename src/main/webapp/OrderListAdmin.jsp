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
					style="background-color: blueviolet; border-radius: 8px; color: white; padding: 10px; margin: 10px 0px 0px 0px;">
					Chi tiết đơn hàng</h1>
				<div class="button-container">
					<a href="orderListAdmin" class="button">Danh sách đơn hàng</a>
					<%-- <a
						href="loadOrder?orderID=${order.orderID}" class="button"
						style="margin-right: 20px;">Edit</a> --%>
				</div>
				<jsp:include page="OrderDetail.jsp" />
			</c:when>

			<c:when test="${not empty recentlyDelete}">
				<h1
					style="background-color: blueviolet; border-radius: 8px; color: white; padding: 10px; margin: 10px 0px 0px 0px;">
					Danh sách sản phẩm đã xóa gần đây</h1>
				<a href="productListAdmin" class="button" style="margin-left: 5px;">Danh
					sách sản phẩm</a>
				<jsp:include page="RecentlyDelete.jsp" />
			</c:when>

			<c:when test="${not empty orderEdit}">
				<h1
					style="background-color: blueviolet; border-radius: 8px; color: white; padding: 10px; margin: 10px 0px 0px 0px;">
					Chỉnh sửa đơn hàng</h1>
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

				<c:if test="${not empty error}">
					<div class="error">${error}</div>
				</c:if>

				<c:if test="${not empty message}">
					<div class="message">${message}</div>
				</c:if>

				<table>
					<thead>
						<tr>
							<th>Mã đơn hàng</th>
							<th>Khách hàng</th>
							<th>Số sản phẩm</th>
							<th>Tổng giá trị</th>
							<th>Ngày đặt hàng</th>
							<th>Hành động</th>
							<th>Trạng thái</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="order" items="${orderListAdmin}">
							<tr>
								<td><c:out value="${order.orderID}" /></td>
								<td><c:out value="${order.customerName}" /></td>
								<td><c:out value="${order.cartItems.size()}" /></td>
								<td><c:set var="totalOrderPrice" value="0" /> <c:forEach
										var="item" items="${order.cartItems}">
										<c:set var="totalOrderPrice"
											value="${totalOrderPrice + item.totalPrice}" />
									</c:forEach> ${totalOrderPrice} VNĐ</td>
								<td><c:out value="${order.orderDate}" /></td>
								<td><a href="orderDetail?orderID=${order.orderID}">Xem</a>
									<c:if test="${not empty sessionScope.accSession}">
										<button class="button" style="margin-left: 10px;"
											onclick="deleteOrder(${order.orderID})">Xóa</button>
									</c:if> <c:if test="${empty sessionScope.accSession}">
										<button class="button" style="margin-left: 10px;"
											onclick="window.location.href='login'">Xóa</button>
									</c:if></td>
								<td><c:choose>
										<c:when test="${order.statusReport == 'Invalid'}">
											<span style="color: red;">Cần xác minh</span>
										</c:when>
										<c:otherwise>
											<c:choose>
												<c:when test="${order.orderStatus == 'process'}">
													<span style="color: orange;">Đang xử lý</span>
													<c:if test="${order.edited}">
														<span style="color: blue; margin-left: 10px;">Đã
															chỉnh sửa</span>
													</c:if>
													<button class="button" style="margin-left: 10px;"
														onclick="cancelOrder(${order.orderID})">Hủy</button>
												</c:when>

												<c:when test="${order.orderStatus == 'Hoàn tất'}">
													<span style="color: green;">Đã hoàn tất</span>
												</c:when>

												<c:when test="${order.orderStatus == 'cancel'}">
													<span style="color: red;">Đơn hàng bị hủy</span>
												</c:when>

												<c:otherwise>
													<span style="color: red;">${order.orderStatus != null ? order.orderStatus : 'cancel'}</span>
												</c:otherwise>
											</c:choose>
										</c:otherwise>
									</c:choose></td>

							</tr>
						</c:forEach>
					</tbody>
				</table>

				<c:if test="${totalPages > 1}">
					<div class="pagination">
						<c:forEach var="i" begin="1" end="${totalPages}">
							<a href="orderListAdmin?page=${i}"
								class="${i == currentPage ? 'active' : ''}">${i}</a>
						</c:forEach>
					</div>
				</c:if>
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
		
		function cancelOrder(orderID) {
			if (confirm("Bạn có chắc chắn muốn xóa đơn hàng này?")) {
				// Gọi đến servlet để xóa sản phẩm

				window.location.href = `cancelOrder?orderID=` + orderID;
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