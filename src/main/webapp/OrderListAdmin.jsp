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
			<c:when test="${not empty addNewProduct}">
				<h1
					style="background-color: blueviolet; border-radius: 8px; color: white; padding: 10px; margin: 10px 0px 0px 0px;">Thêm
					sản phẩm</h1>
				<a href="productListAdmin" class="button" style="margin-left: 5px;">Danh
					Sách sản phẩm</a>
				<jsp:include page="AddNewProduct.jsp" />
			</c:when>

			<c:when test="${not empty orderDetail}">
				<h1
					style="background-color: blueviolet; border-radius: 8px; color: white; padding: 10px; margin: 10px 0px 0px 0px;">Chi
					tiết đơn hàng</h1>
				<a href="orderListAdmin" class="button" style="margin-left: 5px;">
					Danh sách đơn hàng</a>
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
							<th>Số lượng sản phẩm</th>
							<th>Giá trị đơn hàng</th>
							<th>Ngày đặt hàng</th>
							<th>Thông tin đơn hàng</th>
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

							</tr>
						</c:forEach>
						<!-- Add more products as needed -->
					</tbody>
				</table>
			</c:otherwise>
		</c:choose>
	</div>
	<script>
		function deleteProduct(productId) {
			console.log("Product ID to delete js:", productId); // Debug log
			if (!productId) {
				alert("Product ID is required."); // Hiển thị thông báo nếu không có ID
				return; // Dừng hàm nếu không có ID
			}
			if (confirm("Bạn có chắc chắn muốn xóa sản phẩm này?")) {
				// Gọi đến servlet để xóa sản phẩm

				window.location.href = `deleteProduct?pid=` + productId;
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