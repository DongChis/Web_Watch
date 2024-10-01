
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet"
	href="https://use.fontawesome.com/releases/v6.4.2/css/all.css">
<link href="css/footer.css" rel="stylesheet" type="text/css"> 
<link href="css/style.css" rel="stylesheet" type="text/css">
<title>Insert title here</title>
</head>
<body>
	<!-- Footer -->
	<footer class="text-light">
		<div class="container">
			<div class="row">
				<div class="col-md-3 col-lg-4 col-xl-3">
					<h5>Về chúng tôi</h5>
					<hr class="bg-white mb-2 mt-0 d-inline-block mx-auto w-25">
					<p class="mb-0">Tự hào là một trong những doanh nghiệp đồng hồ
						lớn nhất và lâu đời nhất tại Sài Gòn, với đội ngũ KTV và nhân viên
						nhiệt tình phục vụ quý khách.</p>
				</div>

				<div class="col-md-2 col-lg-2 col-xl-2 mx-auto">
					<h5>Thông tin</h5>
					<hr class="bg-white mb-2 mt-0 d-inline-block mx-auto w-25">
					<ul class="list-unstyled">
						<li><a href="">Link 1</a></li>
						<li><a href="">Link 2</a></li>
						<li><a href="">Link 3</a></li>
						<li><a href="">Link 4</a></li>
					</ul>
				</div>

				<div class="col-md-3 col-lg-2 col-xl-2 mx-auto">
					<h5>Link khác</h5>
					<hr class="bg-white mb-2 mt-0 d-inline-block mx-auto w-25">
					<ul class="list-unstyled">
						<li><a href="">Link 1</a></li>
						<li><a href="">Link 2</a></li>
						<li><a href="">Link 3</a></li>
						<li><a href="">Link 4</a></li>
					</ul>
				</div>

				<div class="col-md-4 col-lg-3 col-xl-3">
					<h5>Liên hệ</h5>
					<hr class="bg-white mb-2 mt-0 d-inline-block mx-auto w-25">
					<ul class="list-unstyled">
						<li><i class="fa fa-home mr-2"></i>Hoàng Anh Dũng - Phan Đông
							Chí</li>
						<li><i class="fa fa-envelope mr-2"></i>glassstore@gmail.com</li>
						<li><i class="fa fa-phone mr-2"></i> + 33 425 6426</li>
						<li><i class="fa fa-print mr-2"></i> + 0333 868 686</li>
					</ul>
				</div>
				<div class="col-12 copyright mt-3">
					<p class="float-left">
						<a href="#"><i class="fas fa-arrow-up fa-lg"></i></a>
					</p>
					<p class="text-right text-muted">
						Sản phẩm được tạo bởi <i class="fa fa-heart"></i><a
							href="https://t-php.fr/43-theme-ecommerce-bootstrap-4.html"><i>Java
								Servlet & JSP</i></a> | <span>v. 1.0</span>
					</p>
				</div>
			</div>
		</div>
	</footer>
	<script
		src="https://www.gstatic.com/dialogflow-console/fast/messenger/bootstrap.js?v=1"></script>
	<df-messenger intent="WELCOME" chat-title="Glass Store"
		agent-id="a9d6b041-eb2b-48c5-9565-6b3196b35e42" language-code="vi"></df-messenger>
	<div class="contact-bubble">
		<a href="#" class="contact-link"> <span> <i
				class="fa-solid fa-phone-volume fa-lg fa-shake" style="color: orange;"></i>
		</span>
		</a>
	</div>
</body>
</html>
