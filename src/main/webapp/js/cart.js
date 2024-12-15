
function addToCart(productId, quantity) {
	var xhr = new XMLHttpRequest();
	xhr.open("POST", "add-to-cart", true);  // Gửi yêu cầu POST tới servlet
	xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
 // Gửi yêu cầu với dữ liệu sản phẩm và số lượng
    xhr.send("id=" + productId + "&quantity=" + quantity);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) { // Kiểm tra nếu yêu cầu đã hoàn tất
			if (xhr.status === 401) {
				// Người dùng chưa đăng nhập, chuyển hướng đến trang đăng nhập
				alert("Bạn phải đăng nhập để thêm sản phẩm vào giỏ hàng.");
				window.location.href = "login"; // Chuyển hướng
			} else if (xhr.status === 200) {
				var response = JSON.parse(xhr.responseText);
				var totalQuantity = response.totalQuantity;

				// Cập nhật số lượng giỏ hàng trong giao diện
				document.getElementById("cart-count").innerText = totalQuantity;
				alert("Sản phẩm đã được thêm vào giỏ hàng!");
			} else {
				// Xử lý các mã lỗi khác nếu cần (ví dụ: 500, 404)
				alert("Có lỗi xảy ra, vui lòng thử lại.");
			}
		}
	};
}





function saveCartToSession(count) {

	localStorage.setItem("cartCount", count);
}


function removeFromCart(productId) {
	const url =  'remove-from-cart';

	fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams({
            'id': productId
        })
    })
    .then(response => {
        if (response.ok) {
            // Giả sử server trả về tổng số lượng sau khi xóa sản phẩm
            return response.json();  // Chuyển phản hồi thành JSON
        } else {
            throw new Error('Lỗi khi xóa sản phẩm');
        }
    })
    .then(data => {
        // Cập nhật lại số lượng giỏ hàng trên giao diện
        const cartCountElement = document.getElementById("cart-count");
        if (cartCountElement) {
            cartCountElement.innerText = data.totalQuantity;  // Dùng tổng số lượng từ server
        }
    })
    .catch(error => {
        console.error('Lỗi khi xóa sản phẩm:', error);
    });
}