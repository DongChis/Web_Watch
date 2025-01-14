
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


function updateQuantity(productID, change) {
    // Lấy phần tử hiển thị số lượng sản phẩm
    var quantityElement = document.querySelector(`[data-id='${productID}'] .quantity-display`);
    var currentQuantity = parseInt(quantityElement.textContent);

    // Tính toán số lượng mới
    var newQuantity = currentQuantity + change;

    // Nếu số lượng mới hợp lệ, cập nhật lại giao diện
    if (newQuantity >= 1) {
        quantityElement.textContent = newQuantity;

        // Gửi yêu cầu cập nhật số lượng đến server (bằng AJAX hoặc request tương tự)
        var xhr = new XMLHttpRequest();
        xhr.open('POST', 'updateQuantity', true);  // Đổi 'updateQuantity' thành URL đúng của bạn
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4 && xhr.status == 200) {
                // Xử lý phản hồi từ server, ví dụ như cập nhật giá trị giỏ hàng
                console.log('Số lượng đã được cập nhật');
            }
        };
        xhr.send('productID=' + productID + '&quantity=' + newQuantity);
    }
}




function saveCartToSession(count) {

	localStorage.setItem("cartCount", count);
}


function removeFromCart(productId) {
    if (!productId) {
        console.error("Product ID không hợp lệ");
        return;
    }

    const url = "remove-from-cart";

    fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        body: new URLSearchParams({
            id: productId,
        }),
    })
        .then((response) => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error("Lỗi khi xóa sản phẩm");
            }
        })
        .then((data) => {
            const productElement = document.querySelector(`.cart-item[data-id="${productId}"]`);

            // Xóa phần tử khỏi giao diện
            if (productElement) {
                productElement.remove();
            }

            // Cập nhật giỏ hàng trống
            const cartItemsContainer = document.querySelector(".cart-items");
            if (cartItemsContainer && cartItemsContainer.children.length === 0) {
                cartItemsContainer.innerHTML = `
                    <div class="empty-cart-message">
                        <p>Giỏ hàng của bạn hiện đang trống</p>
                    </div>
                `;
                // Ẩn phần cart-summary
                const cartSummary = document.querySelector(".cart-summary");
                if (cartSummary) {
                    cartSummary.style.display = "none";
                }
            }

            // Cập nhật số lượng giỏ hàng
            const cartCountElement = document.getElementById("cart-count");
            if (cartCountElement) {
                cartCountElement.innerText = data.totalQuantity || 0;
            }
        })
        .catch((error) => {
            console.error("Lỗi khi xóa sản phẩm:", error);
            alert("Không thể xóa sản phẩm khỏi giỏ hàng. Vui lòng thử lại!");
        });
}

function updateCartCount(count) {
    const cartCountElement = document.getElementById("cart-count");
    if (cartCountElement) {
        cartCountElement.innerText = count;
    }
    localStorage.setItem("cartCount", count);
}
