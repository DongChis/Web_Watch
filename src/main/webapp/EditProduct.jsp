<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="css/edit.css" rel="stylesheet">
</head>
<body>

<form class="edit-form" action="updateProduct" method="post" enctype="multipart/form-data">
    <input type="hidden" name="ProductID" value="${param.ProductID}"/>
    
    <label class="label-edit-form" for="title">Tiêu đề:</label>
    <input class="input-edit-form" type="text" id="title" name="title" value="${Title}" required/><br/>

    <label class="label-edit-form" for="name">Tên sản phẩm:</label>
    <input class="input-edit-form" type="text" id="name" name="name" value="${Name}" required/><br/>

    <label class="label-edit-form" for="description">Mô tả:</label>
    <textarea id="description" name="description" required>${Description}</textarea><br/>

    <label class="label-edit-form" for="price">Giá:</label>
    <input class="input-edit-form" type="number" id="price" name="price" value="${Price}" required/><br/>

    <label class="label-edit-form" for="imageURL">Ảnh sản phẩm:</label>
    <input class="input-edit-form" type="file" id="imageURL" name="imageURL" accept="image/*"/><br/>
    <img src="${ImageURL}" alt="Current Image" style="width: 100px; height: auto;"/><br/>

    <label class="label-edit-form" for="gender">Giới tính:</label>
    <select id="gender" name="gender">
        <option value="Male" ${Gender == 'Male' ? 'selected' : ''}>Nam</option>
        <option value="Female" ${Gender == 'Female' ? 'selected' : ''}>Nữ</option>
        <option value="Unisex" ${Gender == 'Unisex' ? 'selected' : ''}>Unisex</option>
    </select><br/>

    <button class="btn-edit-form"type="submit">Cập nhật sản phẩm</button>
</form>
</body>
</html>