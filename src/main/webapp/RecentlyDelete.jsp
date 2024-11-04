<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="css/recentDel.css" rel="stylesheet">
<title>Insert title here</title>
</head>
<body>
	<table>
		<thead>
			<tr>
				<th>ID</th>
				<th>Tên sản phẩm</th>
				<th>Thời gian xóa</th>
				<th>Người xóa</th>
				<th>Hành động</th>
			</tr>
		</thead>
		<tbody>
				<c:forEach var="delproduct" items="${listDeletedProduct}">

				<tr>
					<td>${delproduct.productId}</td>
					<td>${delproduct.productName}</td>
					<td>${delproduct.deletedAt}</td>
					<td>${delproduct.deletedBy}</td>
					<td><button class="restore-button"
							onclick="RestoreProduct(${delproduct.productId})">Khôi phục</button></td>
				</tr>
			</c:forEach>
		</tbody>

		<!-- Nút xóa tất cả sản phẩm đã xóa gần đây -->
		<button class="clear-button" onclick="clearDeletedProducts()">Xóa
			tất cả sản phẩm đã xóa gần đây</button>
	</table>

		
		

	<script>
		function RestoreProduct(productId) {
			alert("Đã khôi phục: " + productId);
			 window.location.href = `restore?pid=`+productId;
		}
		function clearDeletedProducts() {
			// Xác nhận hành động xóa
			if (confirm("Bạn có chắc chắn muốn xóa tất cả sản phẩm đã xóa gần đây không?")) {
				// Gửi yêu cầu đến servlet để xóa tất cả
				alert("Đã xóa thành công!");
				window.location.href = 'clearDeletedProducts'; // Thay thế URL với URL servlet của bạn
			}
		}
	</script>

</body>
</html>