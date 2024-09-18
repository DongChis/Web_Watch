<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link href="css/detail.css" rel="stylesheet">
<meta charset="UTF-8">
<title>Danh sach san pham</title>
</head>
<body>

	<jsp:include page="Header.jsp" />
	<div class="container">
        <div class="product-detail">
            <div class="product-images">
                <div class="main-image">
                    <img id="displayed-image" src="img/image1.png" alt="Product Image">
                </div>
                <div class="thumbnails">
                    <img src="img/image1.png" alt="Thumbnail 1" onclick="changeImage('img/image1.png')">
                    <img src="img/image2.jpg" alt="Thumbnail 2" onclick="changeImage('img/image2.jpg')">
                    <img src="img/image3.png" alt="Thumbnail 3" onclick="changeImage('img/image3.png')">
                </div>
            </div>
            <div class="product-info">
                <h1 class="product-title">Product Name</h1>
                <p class="product-description">
                    This is a detailed description of the product. It provides essential information about the product, its features, and benefits.
                </p>
                <div class="product-price">
                    <span>$49.99</span>
                </div>
                <button class="add-to-cart-btn">Add to Cart</button>
            </div>
        </div>
        
        <!-- Phần Đánh Giá Sản Phẩm -->
        <div class="product-reviews">
            <h2>Customer Reviews</h2>
            <div class="review-summary">
                <div class="average-rating">
                    <h3>4.5/5</h3>
                    <div class="rating-stars">
                        ★★★★☆
                    </div>
                    <p>125 reviews</p>
                </div>
                <div class="rating-distribution">
                    <div class="rating-bar">
                        <span>5 stars</span>
                        <div class="bar">
                            <div class="fill" style="width: 70%;"></div>
                        </div>
                        <span class="percent">70%</span>
                    </div>
                    <div class="rating-bar">
                        <span>4 stars</span>
                        <div class="bar">
                            <div class="fill" style="width: 20%;"></div>
                        </div>
                        <span class="percent">20%</span>
                    </div>
                    <div class="rating-bar">
                        <span>3 stars</span>
                        <div class="bar">
                            <div class="fill" style="width: 7%;"></div>
                        </div>
                        <span class="percent">7%</span>
                    </div>
                    <div class="rating-bar">
                        <span>2 stars</span>
                        <div class="bar">
                            <div class="fill" style="width: 2%;"></div>
                        </div>
                        <span class="percent">2%</span>
                    </div>
                    <div class="rating-bar">
                        <span>1 star</span>
                        <div class="bar">
                            <div class="fill" style="width: 1%;"></div>
                        </div>
                        <span class="percent">1%</span>
                    </div>
                </div>
            </div>

            <div class="review-list">
                <h3>Recent Reviews</h3>
                <div class="review">
                    <h4>John Doe <span>★★★★☆</span></h4>
                    <p>
                        The product is great, but delivery was delayed. Overall, I'm satisfied with the quality.
                    </p>
                </div>
                <div class="review">
                    <h4>Jane Smith <span>★★★★★</span></h4>
                    <p>
                        Amazing product! The material feels premium and the design is exactly what I was looking for. Highly recommend!
                    </p>
                </div>
                <div class="review">
                    <h4>Mike Johnson <span>★★★☆☆</span></h4>
                    <p>
                        Good product, but the color was a bit different from what was shown in the pictures.
                    </p>
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

</body>
</html>