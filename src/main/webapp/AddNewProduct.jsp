<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="css/addform.css" rel="stylesheet">
<title>Thêm sản phẩm mới</title>
</head>
<body>
	<form action="addNewProduct" method="post" class="form-add-product" onsubmit="return showAlert();">
		<label for="productName">Tên sản phẩm:</label> <input
			class="input-add-form" type="text" id="productName"
			name="productName" required> <label for="productName">Tiêu đề:
			</label> <input class="input-add-form" type="text" id="title"
			name="title" required> <label for="productPrice">Giá
			sản phẩm:</label> <input class="input-add-form" type="number"
			id="productPrice" name="productPrice" required> <label
			for="productDescription">Mô tả sản phẩm:</label>
		<textarea id="productDescription" name="productDescription" required></textarea>

		<label for="productImage">Ảnh sản phẩm:</label> <input
			class="input-add-form" type="file" id="productImage"
			name="productImage" accept="image/*" required>

		<button type="submit">Thêm sản phẩm</button>
	</form>

	<script src="js/addform.js">
	
	</script>
	<script>
	function showAlert() {
	    alert("Sản phẩm đã được thêm thành công!");
	    return true; // Trả về true để tiếp tục gửi form
	}
	</script>
	

</body>
</html>