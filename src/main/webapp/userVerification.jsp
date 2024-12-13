<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Xác Minh Người Dùng - Chữ Ký Điện Tử</title>
<link rel="stylesheet" href="css/userVerification.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>
<body>
<jsp:include page="Header.jsp" />
	<div class="container">
    <h1>Xác Minh Người Mua - Chữ Ký Điện Tử</h1>
    <form action="/submitVerification" method="POST" id="verificationForm" enctype="multipart/form-data">
        
        <div class="form-group">
            <label for="signatureOption">Chọn phương thức ký:</label>
            <select id="signatureOption" name="signatureOption" onchange="toggleSignatureInput()">
                <option value="text">Chữ ký văn bản</option>
                <option value="file">Tải lên chữ ký</option>
            </select>
        </div>

        <div id="textSignature" class="form-group">
            <label for="signatureText">Chữ ký của bạn:</label>
            <input type="text" id="signatureText" name="signatureText" placeholder="Nhập chữ ký của bạn" required>
        </div>

        <div id="fileSignature" class="form-group" style="display: none;">
            <label for="signatureFile">Tải lên chữ ký:</label>
            <input type="file" id="signatureFile" name="signatureFile" accept="image/*,application/pdf,.docx,.txt" required>
        </div>
        
        <p>Nếu bạn chưa có chữ ký? <a href="javascript:void(0);" id="createSignatureLink">Tạo chữ ký</a></p>

        <button type="submit">Xác Minh</button>
    </form>
</div>


	<!-- Dialog box -->
	<div id="dialog" class="dialog-overlay">
		<div class="dialog-box">
			<h3>Bạn đã có Key chưa?</h3>
			<button class="yes" onclick="handleResponse(true)">Có</button>
			<button class="no" onclick="handleResponse(false)">Chưa</button>
		</div>
	</div>
	
		<jsp:include page="Footer.jsp" />

	<script>
    // Khi nhấp vào "Tạo chữ ký", hiển thị dialog hỏi người dùng có khóa chữ ký chưa
    document.getElementById("createSignatureLink").onclick = function() {
        document.getElementById("dialog").style.display = "flex"; // Hiển thị dialog
    };

    // Hàm xử lý khi người dùng trả lời dialog
    function handleResponse(hasKey) {
        if (hasKey) {
            alert("Vui lòng Tải tool để tạo Chữ ký bằng private key");
            window.location.href = "loadInfoUser";
        } else {
            window.location.href = "Function.jsp";
        }
        // Đóng dialog
        document.getElementById("dialog").style.display = "none";
    }

    // Hàm để thay đổi phương thức ký (text hoặc file)
    function toggleSignatureInput() {
        const signatureOption = document.getElementById("signatureOption").value;
        const textSignature = document.getElementById("textSignature");
        const fileSignature = document.getElementById("fileSignature");
        
        if (signatureOption === "text") {
            textSignature.style.display = "block";
            fileSignature.style.display = "none";
        } else {
            textSignature.style.display = "none";
            fileSignature.style.display = "block";
        }
    }
</script>

</body>
</html>
