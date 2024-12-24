<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="css/home.css" rel="stylesheet">
<title>Danh sach san pham</title>
</head>
<body>

	<jsp:include page="Header.jsp" />
	<div class="container">

		<div class="row">
			<div class="col-sm-9">
				<div class="row">
					<c:forEach var="product" items="${listAllProduct}">
						<div class="col-md-4-l ">
							<div class="card">
								<img class="card-img-top" src="${product.imageURL}"
									alt="${product.imageURL}">
								<div class="card-body">
									<h5 class="card-title">${product.name}</h5>
									<p class="card-text">${product.description}</p>
									<p>
										<strong>${product.price} VND</strong>
									</p>

									<div class="card-btn">
										<a href="detail?pid=${product.productID}" class="btn-view">Xem</a>
										<button class="btn btn-add"
											onclick="addToCart(${product.productID}, 1)">Thêm</button>
									</div>

								</div>
							</div>
						</div>
					</c:forEach>
				</div>

				<c:if test="${totalPages > 1}">
					<div class="pagination">
						<c:forEach var="i" begin="1" end="${totalPages}">
							<a href="productList?page=${i}"
								class="${i == currentPage ? 'active' : ''}">${i}</a>
						</c:forEach>
					</div>
				</c:if>

			</div>
		</div>



	</div>


	<jsp:include page="Footer.jsp" />


</body>
</html>