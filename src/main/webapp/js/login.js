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

    if (categoryList && categoryButton && 
        !categoryList.contains(event.target) && !categoryButton.contains(event.target)) {
        categoryList.style.display = "none";
    }
});

// Hàm để toggle (hiện/ẩn) danh sách khi click vào nút
function toggleCategory() {
    const categoryList = document.getElementById('categoryList');
    if (categoryList.style.display === 'none' || categoryList.style.display === '') {
        categoryList.style.display = 'block';
    } else {
        categoryList.style.display = 'none';
    }
}



