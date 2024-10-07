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
			<c:when test="${not empty edit}">
				<h1
					style="background-color: blueviolet; border-radius: 8px; color: white; padding: 10px; margin: 10px 0px 0px 0px;">Chỉnh sửa
					sản phẩm</h1>
				<a href="productListAdmin" class="button" style="margin-left: 5px;">
					Danh sách sản phẩm</a>
				<jsp:include page="EditProduct.jsp" />
			</c:when>
			<c:otherwise>
				<h1
					style="background-color: blueviolet; border-radius: 8px; color: white; padding: 10px; margin: 10px 0px 0px 0px;">
					Danh sách sản phẩm</h1>
				<div class="button-container">
					<a href="addNewProduct" class="button">Thêm sản phẩm</a>
					<div class="search-container">
						<input type="text" placeholder="Tìm kiếm sản phẩm..." class="search-input">
						<a href="#" class="button"><i class="fas fa-search"></i></a>
					</div>
				</div>


				<table>
					<thead>
						<tr>
							<th>ID</th>
							<th>Ảnh sản phẩm</th>
							<th>Tên sản phẩm</th>
							<th>Giá</th>
							<th>Mô tả</th>
							<th>Hành động</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="product" items="${productListAdmin}">
							<tr>
								<td>${product.productID}</td>
								<td><img src="${product.imageURL}" style="width: 70px; height: auto;"></td>
								<td>${product.name}</td>
								<td>${product.price}VNĐ</td>
								<td>${product.title}</td>
								<td><a href="edit" class="button">Sửa</a>
									<button class="button"  onclick="deleteProduct(${product.productID})">Xóa</button></td>
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
	       
	            window.location.href = `deleteProduct?pid=`+productId;
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