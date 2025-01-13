<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <link href="css/detail.css" rel="stylesheet">
    <meta charset="UTF-8">
    <title>${detail.name} - Chi tiết sản phẩm</title>
</head>
<body>

<jsp:include page="Header.jsp" />

<div class="container">
    <!-- Breadcrumb -->
    <nav class="breadcrumb">
        <a href="home">Trang chủ</a>
        <span>›</span>
        <a href="productList">Sản phẩm</a>
        <span>›</span>
        <span>${detail.name}</span>
    </nav>

    <!-- Product Detail Section -->
    <div class="product-detail">
        <!-- Hình ảnh sản phẩm -->
        <div class="product-images">
            <div class="main-image">
                <img id="displayed-image" src="${detail.imageURL}" alt="${detail.name}">
            </div>
            <div class="thumbnails">
                <img src="${detail.imageURL}" alt="Thumbnail 1" onclick="changeImage('${detail.imageURL}')">
                <img src="${detail.imageURL}" alt="Thumbnail 2" onclick="changeImage('${detail.imageURL}')">
                <img src="${detail.imageURL}" alt="Thumbnail 3" onclick="changeImage('${detail.imageURL}')">
            </div>
        </div>

        <!-- Thông tin sản phẩm -->
        <div class="product-info">
            <h1 class="product-title">${detail.name}</h1>
            <p class="product-description">${detail.description}</p>
            <div class="product-price">
                <strong>${detail.price} VND</strong>
            </div>
            <div class="product-options">
                <label for="quantity">Số lượng:</label>
                <input type="number" id="quantity" name="quantity" value="1" min="1">
            </div>
            <button class="add-to-cart-btn" onclick="addToCart(${idProduct}, document.getElementById('quantity').value)">
                Thêm vào giỏ
            </button>
        </div>
    </div>

    <!-- Đánh giá sản phẩm -->
    <div class="product-reviews">
        <h2>Đánh giá sản phẩm</h2>
        <div class="review-summary">
            <div class="average-rating">
                <h3>4.5/5</h3>
                <div class="rating-stars">★★★★☆</div>
                <p>125 đánh giá</p>
            </div>
        </div>
        <div class="review-list">
            <h3>Đánh giá gần đây</h3>
            <div class="review">
                <h4>John Doe <span>★★★★☆</span></h4>
                <p>Sản phẩm tốt, chất lượng như mong đợi!</p>
            </div>
            <div class="review">
                <h4>Jane Smith <span>★★★★★</span></h4>
                <p>Chất lượng tuyệt vời, giao hàng nhanh chóng. Rất hài lòng!</p>
            </div>
        </div>
    </div>
</div>

<jsp:include page="Footer.jsp" />

<script>
    function changeImage(imageSrc) {
        document.getElementById('displayed-image').src = imageSrc;
    }
</script>
<script src="js/cart.js"></script>
</body>
</html>
