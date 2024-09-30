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

 const contextPath = '${pageContext.request.contextPath}';

    function removeFromCart(productId) {
        const url = '/Web_Watch' + '/remove-from-cart';
		
        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: new URLSearchParams({
                'id': productId
            })
        }).then(response => {
            if (response.ok) {
				console.assert('url', contextPath);
                window.location.href = '/Web_Watch' + '/Cart.jsp';  // Chuyển hướng về giỏ hàng
            } else {
                console.error('Lỗi khi xóa sản phẩm:', response);
            }
        }).catch(error => {
            console.error('Lỗi khi xóa sản phẩm:', error);
        });
    }