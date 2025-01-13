<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Blog Đồng Hồ</title>
    <link rel="stylesheet" href="css/blogs.css">
</head>
<body>
   <jsp:include page="Header.jsp" />

    <div class="blog-container">
    <nav class="breadcrumb">
			<a href="home">Trang chủ</a> | <span>Bài viết</span>
		</nav>
        <h1>Blog Đồng Hồ</h1>

        <!-- Bài viết 1 -->
        <div class="post">
            <h2>Những Mẫu Đồng Hồ Sang Trọng Dành Cho Mùa Lễ Hội</h2>
            <p class="post-meta">Đăng bởi <strong>Admin</strong> vào <strong>13 Tháng 1, 2025</strong></p>
            <p class="post-content">
                Đồng hồ không chỉ là một công cụ để xem giờ, mà còn là một phụ kiện thời trang không thể thiếu. Những mẫu đồng hồ cao cấp dưới đây sẽ là lựa chọn hoàn hảo cho các dịp lễ hội...
            </p>
            <a href="/blog/detail/1" class="read-more">Đọc thêm</a>
        </div>

        <!-- Bài viết 2 -->
        <div class="post">
            <h2>Cách Chọn Đồng Hồ Phù Hợp Với Mỗi Dáng Tay</h2>
            <p class="post-meta">Đăng bởi <strong>Admin</strong> vào <strong>10 Tháng 1, 2025</strong></p>
            <p class="post-content">
                Chọn đồng hồ phù hợp với dáng tay không phải là điều dễ dàng. Hãy cùng khám phá những bí quyết giúp bạn lựa chọn được chiếc đồng hồ phù hợp nhất với cơ thể của mình...
            </p>
            <a href="/blog/detail/2" class="read-more">Đọc thêm</a>
        </div>

        <!-- Bài viết 3 -->
        <div class="post">
            <h2>5 Lý Do Tại Sao Đồng Hồ Là Quà Tặng Tuyệt Vời</h2>
            <p class="post-meta">Đăng bởi <strong>Admin</strong> vào <strong>5 Tháng 1, 2025</strong></p>
            <p class="post-content">
                Đồng hồ không chỉ là một công cụ để theo dõi thời gian, mà còn là một món quà tuyệt vời trong những dịp đặc biệt. Dưới đây là 5 lý do tại sao đồng hồ lại là món quà lý tưởng...
            </p>
            <a href="/blog/detail/3" class="read-more">Đọc thêm</a>
        </div>

        <!-- Bài viết 4 -->
        <div class="post">
            <h2>Top 10 Mẫu Đồng Hồ Cao Cấp Dành Cho Doanh Nhân</h2>
            <p class="post-meta">Đăng bởi <strong>Admin</strong> vào <strong>1 Tháng 1, 2025</strong></p>
            <p class="post-content">
                Nếu bạn là một doanh nhân, chiếc đồng hồ không chỉ là món phụ kiện mà còn là biểu tượng của sự thành đạt. Hãy cùng khám phá 10 mẫu đồng hồ cao cấp, nổi bật nhất trong năm nay...
            </p>
            <a href="/blog/detail/4" class="read-more">Đọc thêm</a>
        </div>

    </div>

   <jsp:include page="Footer.jsp" />
</body>
</html>
