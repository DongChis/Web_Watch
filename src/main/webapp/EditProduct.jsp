<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="css/edit.css" rel="stylesheet">
</head>
<body>

<form class="edit-form" action="edit?pid=${product.productID}" method="post" onsubmit="return showAlert();">
    <input type="hidden" name="ProductID" value="${product.productID}"/>
    
    <label class="label-edit-form" for="title">Tiêu đề:</label>
    <input class="input-edit-form" type="text"  name="title" value="${product.title}" required/><br/>

    <label class="label-edit-form" for="name">Tên sản phẩm:</label>
    <input class="input-edit-form" type="text"  name="name" value="${product.name}" required/><br/>

    <label class="label-edit-form" for="description">Mô tả:</label>
    <textarea  name="description" required>${product.description}</textarea><br/>

    <label class="label-edit-form" for="price">Giá:</label>
    <input class="input-edit-form" type="number" name="price" value="${product.price}" required/><br/>

    <label class="label-edit-form" for="imageURL">Ảnh sản phẩm:</label>
    <input class="input-edit-form" type="file" name="imageURL" accept="image/*"/><br/>
    <img src="${product.imageURL}" alt="Current Image" style="width: 100px; height: auto;"/><br/>


    <button class="btn-edit-form"type="submit">Cập nhật sản phẩm</button>
</form>
	<script>
	function showAlert() {
	    alert("Sản phẩm đã được cập nhật!");
	    return true; // Trả về true để tiếp tục gửi form
	}
	</script>
</body>
</html>