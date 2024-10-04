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
	<div class="product-list-ad">

		<c:choose>
			<c:when test="${not empty addNewProduct}">
				<h1 style="background-color: blueviolet;
							border-radius: 8px;
							color: white;
							padding: 10px;
							margin: 10px 0px 0px 5px;">Thêm sản phẩm</h1>
				<a href="productListAdmin" class="button" style="margin-left: 5px;
				">Danh Sách sản phẩm</a>
				<jsp:include page="AddNewProduct.jsp" />
			</c:when>
			<c:otherwise>
				<h1 style="background-color: blueviolet;
							border-radius: 8px;
							color: white;
							padding: 10px;
							margin: 10px 0px 0px 5px;">Danh sách sản phẩm</h1>
				<a href="addNewProduct" class="button" style="margin-left: 5px;">Thêm sản phẩm</a>
				<table>
					<thead>
						<tr>
							<th>ID</th>
							<th>Tên sản phẩm</th>
							<th>Giá</th>
							<th>Mô tả</th>
							<th>Hành động</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>1</td>
							<td>Sản phẩm A</td>
							<td>100,000 VNĐ</td>
							<td>Mô tả sản phẩm A</td>
							<td><a href="editProduct.html?id=1" class="button">Sửa</a>
								<button class="button" onclick="deleteProduct(1)">Xóa</button></td>
						</tr>
						<tr>
							<td>2</td>
							<td>Sản phẩm B</td>
							<td>200,000 VNĐ</td>
							<td>Mô tả sản phẩm B</td>
							<td><a href="editProduct.html?id=2" class="button">Sửa</a>
								<button class="button" onclick="deleteProduct(2)">Xóa</button></td>
						</tr>
						<!-- Add more products as needed -->
					</tbody>
				</table>
			</c:otherwise>
		</c:choose>
	</div>
	<script>
		function deleteProduct(productId) {
			if (confirm("Bạn có chắc chắn muốn xóa sản phẩm này?")) {
				// Add logic to delete the product, e.g., sending a request to the server.
				alert("Sản phẩm với ID " + productId + " đã được xóa.");
			}
		}
	</script>
</body>
</html>