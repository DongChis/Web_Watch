
function redirectToLoginPage() {
	window.location.href = 'login';
}
function redirectToServlet() {
	window.location.href = 'logout';
}

window.onload = function() {
    // Kiểm tra nếu có bất kỳ tab Admin nào đã được mở
    console.log("onload")
    if (localStorage.getItem("adminAccessed")) {
        // Nếu đã có tab Admin, chuyển hướng về trang home
        window.location.href = "/home";
    } else {
        // Nếu chưa có tab Admin nào, đánh dấu tab Admin đã được mở
        localStorage.setItem("adminAccessed", "true");
    }
};

// Khi người dùng đóng tab, xóa trạng thái trong localStorage
window.onbeforeunload = function() {
    // Chỉ xóa trạng thái khi không còn tab nào khác đang mở
    setTimeout(function() {
        if (localStorage.getItem("adminAccessed") === "true") {
            localStorage.removeItem("adminAccessed");
        }
    }, 0);
};