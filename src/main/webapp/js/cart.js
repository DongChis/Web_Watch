function addToCart(productId, quantity) {
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "add-to-cart", true);  // Gửi yêu cầu POST tới servlet
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4 && xhr.status == 200) {
            // Khi nhận được phản hồi thành công từ server
            alert("Sản phẩm đã được thêm vào giỏ hàng!");

            // Chuyển hướng đến trang giỏ hàng sau khi thêm sản phẩm
            window.location.href = "Cart.jsp";  // Chuyển hướng sang trang Cart.jsp
        }
    };
    
    // Gửi dữ liệu (productId và quantity) tới servlet
    xhr.send("id=" + productId + "&quantity=" + quantity);
}

    var contextPath = "${pageContext.request.contextPath}";


 function removeFromCart(productId) {
    var xhr = new XMLHttpRequest();
    // Sử dụng biến contextPath để xây dựng URL
    xhr.open("POST", contextPath + "/remove-from-cart", true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
            // Sau khi xóa thành công, tải lại trang
            window.location.reload();
        } else if (xhr.readyState == 4 && xhr.status != 200) {
            console.error("Lỗi khi xóa sản phẩm:", xhr.responseText);
        }
    };
    xhr.send("id=" + productId);
}
