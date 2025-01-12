<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Watch Store</title>
    <link href="css/home.css" rel="stylesheet">
</head>
<body>
    <!-- Header -->
    <jsp:include page="Header.jsp" />

    <main class="container">
        <!-- Slideshow Section -->
        <section class="slideshow-container">
            <div class="slides">
                <img src="img/image1.png" alt="Watch Image 1">
                <img src="img/image2.jpg" alt="Watch Image 2">
                <img src="img/image3.png" alt="Watch Image 3">
            </div>
        </section>

        <!-- New Arrivals Section -->
        <section class="product-slider">
            <h2>Sản phẩm mới ra mắt</h2>
            <div class="slider-track new-arrival-slides">
                <c:forEach var="product" items="${listAllProduct}">
                    <article class="slider-item">
                        <div class="card">
                            <img src="${product.imageURL != null ? product.imageURL : 'img/placeholder.png'}" 
                                 alt="${product.name}">
                            <div class="card-body">
                                <h3 class="card-title">${product.name}</h3>
                                <p class="card-text">${product.description}</p>
                                <p><strong>${product.price} VND</strong></p>
                                <div class="card-btn">
                                    <a href="detail?pid=${product.productID}" class="btn-view">Xem</a>
                                    <button onclick="addToCart(${product.productID}, 1)" class="btn btn-add">Thêm</button>
                                </div>
                            </div>
                        </div>
                    </article>
                </c:forEach>
            </div>
            <div class="slider-controls">
                <button class="prev-btn" onclick="moveSlide('.new-arrival-slides', -1)"></button>
                <button class="next-btn" onclick="moveSlide('.new-arrival-slides', 1)"></button>
            </div>
        </section>

        <!-- Best Sellers Section -->
        <section class="product-slider">
            <h2>Sản phẩm bán chạy</h2>
            <div class="slider-track best-seller-slides">
                <c:forEach var="product" items="${listAllProduct}">
                    <article class="slider-item">
                        <div class="card">
                            <img src="${product.imageURL != null ? product.imageURL : 'img/placeholder.png'}" 
                                 alt="${product.name}">
                            <div class="card-body">
                                <h3 class="card-title">${product.name}</h3>
                                <p class="card-text">${product.description}</p>
                                <p><strong>${product.price} VND</strong></p>
                                <div class="card-btn">
                                    <a href="detail?pid=${product.productID}" class="btn-view">Xem</a>
                                    <button class="btn btn-add" onclick="addToCart(${product.productID}, 1)">Thêm</button>
                                </div>
                            </div>
                        </div>
                    </article>
                </c:forEach>
            </div>
            <div class="slider-controls">
                <button class="prev-btn" onclick="moveSlide('.best-seller-slides', -1)"></button>
                <button class="next-btn" onclick="moveSlide('.best-seller-slides', 1)"></button>
            </div>
        </section>
    </main>

    <!-- Footer -->
    <jsp:include page="Footer.jsp" />

    <!-- Scripts -->
    <script src="js/slide.js"></script>
    <script src="js/cart.js"></script>
    <script src="js/slidersp.js"></script>
</body>
</html>
