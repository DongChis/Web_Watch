
function redirectToLoginPage() {
	window.location.href = 'login';
}
function redirectToServlet() {
	window.location.href = 'logout';
}


// Lắng nghe sự kiện click trên toàn bộ trang
document.addEventListener("click", function (event) {
    const categoryList = document.getElementById("categoryList");
    const categoryButton = document.querySelector(".heros__categories");

 
    if (!categoryList.contains(event.target) && !categoryButton.contains(event.target)) {
        categoryList.style.display = "none"; 
    }
});

// Hàm để toggle (hiện/ẩn) danh sách khi click vào nút
function toggleCategory() {
    const categoryList = document.getElementById("categoryList");
    if (categoryList.style.display === "none" || categoryList.style.display === "") {
        categoryList.style.display = "block"; // Hiển thị danh sách
    } else {
        categoryList.style.display = "none"; // Ẩn danh sách
    }
}
function toggleCategory() {
    var categoryList = document.getElementById('categoryList');
    if (categoryList.style.display === 'none' || categoryList.style.display === '') {
        categoryList.style.display = 'block';
    } else {
        categoryList.style.display = 'none';
    }
}

// Khi trang được tải
window.onload = function() {
   
    if (window.location.pathname.indexOf('/admin') !== -1) {
        if (localStorage.getItem("adminTabOpened") === "true") {
         console.log("h")
            window.location.href = 'login';
        } else {
            // Nếu đây là tab Admin đầu tiên, đánh dấu là tab chính
            localStorage.setItem("adminTabOpened", "true");
        }
    }
   
};

// Khi người dùng đóng tab, kiểm tra và xóa trạng thái khi tất cả tab Admin đã đóng
window.onbeforeunload = function() {
    if (window.location.pathname.indexOf('/admin') !== -1) {
        setTimeout(function() {
            // Kiểm tra xem có còn tab Admin nào khác đang mở không
            if (localStorage.getItem("adminTabOpened") === "true" && window.localStorage.length === 1) {
                localStorage.removeItem("adminTabOpened");
            }
        }, 0);
    }
};

// Lắng nghe sự kiện storage khi một tab thay đổi localStorage
window.addEventListener("storage", function(event) {
    // Kiểm tra nếu có sự thay đổi đối với "adminTabOpened"
    if (event.key === "adminTabOpened") {
        if (event.newValue === "true") {
            // Nếu trạng thái là "true", chuyển hướng tất cả các tab còn lại tới trang login
            if (window.location.pathname.indexOf('/admin') !== -1) {
                window.location.href = 'login';  // Chuyển hướng tới trang login nếu không phải là tab chính
            }
        } else {
            // Nếu trạng thái được xóa, bạn có thể xử lý cho các tab còn lại theo cách bạn muốn
        }
    }
});
