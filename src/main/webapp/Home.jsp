<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet"
	href="https://use.fontawesome.com/releases/v6.4.2/css/all.css">
<link href="css/home.css" rel="stylesheet" type="text/css">
<title>Watch Store</title>
</head>
<body>
	<div class="container">

		<jsp:include page="Header.jsp">
			<jsp:param name="pageTitle" value="Watch Shop - Home" />
		</jsp:include>

		<div class="slideshow-container">
			<div class="slides">
				<img src="img/image1.png" alt="Ảnh 1"> <img src="img/image2.jpg"
					alt="Ảnh 2"> <img src="img/image3.png" alt="Ảnh 3">
			</div>		
		</div>
		<div class="container-list">
			<h2 class="text-center">Danh sách sản phẩm</h2>
			<div class="row">
				<c:forEach var="product" items="${listAllProduct}">
					<div class="col-md-4 mb-4">
						<div class="card">
							<img class="card-img-top" src="${product.imageURL}"
								alt="${product.imageURL}">
							<div class="card-body">
								<h5 class="card-title">${product.name}</h5>
								<p class="card-text">${product.description}</p>
								<p>
									<strong>${product.price} VND</strong>
								</p>
								<a href="detail?pid=${product.productID}"
									class="btn btn-primary">Xem chi tiết</a>
							</div>
						</div>
					</div>
				</c:forEach>
			</div>
		</div>
		

		<jsp:include page="Footer.jsp" />

	</div>
	<script src="js/slide.js"></script>
</body>
</html>

