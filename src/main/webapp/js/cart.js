
<<<<<<< HEAD
=======
var lengthCart = 0; //  số sản phẩm trong giõ hàng

>>>>>>> b7c2ec775dbd71ab0fb4fd2dad21a712e9ba0197
function addToCart(productId, quantity) {
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "add-to-cart", true);  // Gửi yêu cầu POST tới servlet
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) { // Kiểm tra nếu yêu cầu đã hoàn tất
            if (xhr.status === 401) {
                // Người dùng chưa đăng nhập, chuyển hướng đến trang đăng nhập
                alert("Bạn phải đăng nhập để thêm sản phẩm vào giỏ hàng.");
                window.location.href = "/Web_Watch/Login.jsp"; // Chuyển hướng
            } else if (xhr.status === 200) {
                // Sản phẩm đã được thêm vào giỏ hàng thành công
                lengthCart++;
                document.getElementById("cart-count").innerText = lengthCart; // Cập nhật số lượng giỏ hàng
                alert("Sản phẩm đã được thêm vào giỏ hàng!");
            } else {
                // Xử lý các mã lỗi khác nếu cần (ví dụ: 500, 404)
                alert("Có lỗi xảy ra, vui lòng thử lại.");
            }
        }
    };

    // Gửi yêu cầu với dữ liệu sản phẩm và số lượng
    xhr.send("id=" + productId + "&quantity=" + quantity);
}


/*function addToCart(productId, quantity) {
	
	  	
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "add-to-cart", true);  // Gửi yêu cầu POST tới servlet
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    
     if (!isLoggedIn) {
      
      alert("Bạn phải đăng nhập để thêm sản phẩm vào giỏ hàng.");
      window.location.href = "/Web_Watch/Login.jsp";
      return; 
    }
    
    xhr.onreadystatechange = function () {
<<<<<<< HEAD
        if (xhr.readyState == 4 && xhr.status == 200) {      
=======
        if (xhr.readyState == 4 && xhr.status == 200) {
			lengthCart++;
        	document.getElementById("cart-count").innerText = lengthCart;// đếm số sản phẩm thêm vào và hiển thị
>>>>>>> b7c2ec775dbd71ab0fb4fd2dad21a712e9ba0197
            alert("Sản phẩm đã được thêm vào giỏ hàng!");
        }
    };
  
   
    xhr.send("id=" + productId + "&quantity=" + quantity);
}
*/
function saveCartToSession(count) {
	
    localStorage.setItem("cartCount", count);
  }
 

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
				//console.assert('url', contextPath);
                window.location.href = '/Web_Watch' + '/Cart.jsp';
            } else {
                console.error('Lỗi khi xóa sản phẩm:', response);
            }
        }).catch(error => {
            console.error('Lỗi khi xóa sản phẩm:', error);
        });
    }