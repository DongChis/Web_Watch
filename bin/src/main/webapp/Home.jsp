<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="Header.jsp">
    <jsp:param name="pageTitle" value="Watch Shop - Home" />
</jsp:include>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="css/home.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
     <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <title>Watch Store</title>
</head>

<main>
    <section class="hero">
        <h2>Welcome to Watch Shop</h2>
        <p>Explore our exclusive collection of premium watches.</p>
        <a href="" class="btn">Shop Now</a>
    </section>

    <section class="featured-products">
        <h2>Featured Products</h2>
        <div class="products">
            <div class="product-item">
            
            <div class="slideshow-container">
        <div class="mySlides fade">
            <div class="numbertext">1 / 3</div>
            <img src="image/slide1.jpg" style="width:100%">
            <div class="text">Đồng Hồ 1</div>
        </div>

        <div class="mySlides fade">
            <div class="numbertext">2 / 3</div>
            <img src="image/slide2.jpg" style="width:100%">
            <div class="text">Đồng Hồ 2</div>
        </div>

        <div class="mySlides fade">
            <div class="numbertext">3 / 3</div>
            <img src="image/slide1.jpg" style="width:100%">
            <div class="text">Đồng Hồ 3</div>
        </div>

        <!-- Các nút điều hướng -->
       <!--  <a class="prev" onclick="plusSlides(-1)">&#10094;</a>
        <a class="next" onclick="plusSlides(1)">&#10095;</a> -->
    </div>
    <br>

    <!-- Chấm chỉ báo -->
    <!-- <div style="text-align:center">
        <span class="dot" onclick="currentSlide(1)"></span>
        <span class="dot" onclick="currentSlide(2)"></span>
        <span class="dot" onclick="currentSlide(3)"></span>
    </div> -->
             
            
        </div>
    </section>
</main>

<jsp:include page="Footer.jsp" />

</html>