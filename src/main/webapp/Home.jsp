<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<link href="css/home.css" rel="stylesheet">
<!-- link rel="stylesheet"
	href="https://use.fontawesome.com/releases/v6.4.2/css/all.css"> -->
<title>Watch Store</title>
</head>
<body>
	<jsp:include page="Header.jsp" />

	<div class="container">

		<div class="slideshow-container">
			<div class="slides">
				<img src="img/image1.png" alt="Ảnh 1"> <img
					src="img/image2.jpg" alt="Ảnh 2"> <img src="img/image3.png"
					alt="Ảnh 3">
			</div>
		</div>


		<!-- Product slider: Sản phẩm mới ra mắt -->
    <div class="container-list">
        <h2 class="text-center">Sản phẩm mới ra mắt</h2>
        <div class="product-slider">
            <div class="slider-track new-arrival-slides">
                <c:forEach var="product" items="${listAllProduct}">
                    <div class="slider-item">
                        <div class="card">
                            <img src="${product.imageURL != null ? product.imageURL : 'img/placeholder.png'}" alt="${product.name}">
                            <div class="card-body">
                                <h5 class="card-title">${product.name}</h5>
                                <p class="card-text">${product.description}</p>
                                <p><strong>${product.price} VND</strong></p>
                                <div class="card-btn">
                                    <a href="detail?pid=${product.productID}" class="btn-view">Xem</a>
                                    <button onclick="addToCart(${product.productID}, 1)" class="btn btn-add">Thêm</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div class="slider-controls">
                <button class="prev-btn" onclick="moveSlide('.new-arrival-slides', -1)">&#10094;</button>
                <button class="next-btn" onclick="moveSlide('.new-arrival-slides', 1)">&#10095;</button>
            </div>
        </div>
    </div>

    <!-- Product slider: Sản phẩm bán chạy -->
    <div class="container-list">
        <h2 class="text-center">Sản phẩm bán chạy</h2>
        <div class="product-slider">
            <div class="slider-track best-seller-slides">
                <c:forEach var="product" items="${listAllProduct}">
                    <div class="slider-item">
                        <div class="card">
                            <img src="${product.imageURL}" alt="${product.imageURL}">
                            <div class="card-body">
                                <h5 class="card-title">${product.name}</h5>
                                <p class="card-text">${product.description}</p>
                                <p><strong>${product.price} VND</strong></p>
                                <div class="card-btn">
                                    <a href="detail?pid=${product.productID}" class="btn-view">Xem</a>
                                    <button class="btn btn-add" onclick="addToCart(${product.productID}, 1)">Thêm</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div class="slider-controls">
                <button class="prev-btn" onclick="moveSlide('.best-seller-slides', -1)">&#10094;</button>
                <button class="next-btn" onclick="moveSlide('.best-seller-slides', 1)">&#10095;</button>
            </div>
        </div>
    </div>



	</div>

	<jsp:include page="Footer.jsp" />

	<script src="js/slide.js"></script>
	<script src="js/cart.js"></script>
	<script src="js/slidersp.js"></script>
	
	


</body>
</html>

