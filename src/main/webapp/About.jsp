<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang Web Đồng Hồ</title>
    <link rel="stylesheet" href="css/about.css">
</head>
<body>
   <jsp:include page="Header.jsp" />

    <section id="home">
    <nav class="breadcrumb">
			<a href="home">Trang chủ</a> | <span>Giới thiệu</span>
		</nav>
        <div class="container">
        
            <h2>Trang Web Chuyên Bán Đồng Hồ Chính Hãng</h2>
            <p>Chúng tôi cung cấp các mẫu đồng hồ cao cấp, thời trang, thể thao, và nhiều loại khác cho mọi lứa tuổi và sở thích.</p>
        </div>
    </section>

    <section id="about">
        <div class="container">
            <h2>Giới Thiệu</h2>
            <p>Trang web của chúng tôi cam kết mang đến cho bạn những sản phẩm đồng hồ chất lượng cao với giá cả hợp lý. Chúng tôi hợp tác với các thương hiệu lớn như Casio, Seiko, Rolex, và nhiều thương hiệu nổi tiếng khác để cung cấp cho khách hàng những lựa chọn tốt nhất.</p>
        </div>
    </section>

    <section id="products">
        <div class="container">
            <h2>Các Sản Phẩm Đồng Hồ</h2>
            <div class="product-list">
                <div class="product-item">
                    <img src="https://cdn.tgdd.vn//News/1178420//nen-mua-dong-ho-nam-hang-nao-cac-tieu-chi-lua-chon-thuong-02.2.1-800x575-800x575.jpg" alt="Đồng hồ Casio">
                    <h3>Đồng hồ Casio G-Shock</h3>
                    <p>Đồng hồ thể thao, chống sốc, chống nước 200m.</p>
                </div>
                <div class="product-item">
                    <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSwwbyyALyzPgJikv3-eqjwsLIZ2rAhqSNe0A&s" alt="Đồng hồ Seiko">
                    <h3>Đồng hồ Seiko 5</h3>
                    <p>Đồng hồ tự động, thiết kế cổ điển, dây kim loại.</p>
                </div>
                <div class="product-item">
                    <img src="https://cdn.tgdd.vn/Files/2020/03/11/1241419/c1_1280x960-800-resize.jpg" alt="Đồng hồ Omega">
                    <h3>Đồng hồ Omega De Ville</h3>
                    <p>Đồng hồ cao cấp, bộ máy Swiss, mặt kính Sapphire.</p>
                </div>
            </div>
        </div>
    </section>

    <section id="contact">
        <div class="container">
            <h2>Liên Hệ</h2>
            <p>Để biết thêm thông tin về sản phẩm, xin vui lòng liên hệ với chúng tôi qua email: <strong>support@dongho.com</strong></p>
        </div>
    </section>

   	<jsp:include page="Footer.jsp" />
</body>
</html>
