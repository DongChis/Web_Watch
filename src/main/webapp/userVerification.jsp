<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Xác Minh Người Dùng - Chữ Ký Điện Tử</title>
<link rel="stylesheet" href="css/userVerification.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>
<body>
	<jsp:include page="Header.jsp" />
	<div class="container">
		<h1>Xác Minh Người Mua - Chữ Ký Điện Tử</h1>

		<form action="userVerification" method="POST"
			enctype="multipart/form-data">
			<div class="form-group">
				<label for="signatureOption">Chọn phương thức ký:</label> <select
					id="signatureOption" name="signatureOption"
					onchange="toggleSignatureInput()">
					<option value="text">Chữ ký văn bản</option>
					<option value="file">Tải lên chữ ký</option>
				</select>
			</div>

			<div id="textSignature" class="form-group">
				<label for="signatureText">Chữ ký của bạn:</label> <input
					type="text" id="signatureText" name="signatureText"
					placeholder="Nhập chữ ký của bạn" >
			</div>

			<div id="fileSignature" class="form-group" style="display: none;">
				<label for="signatureFile">Tải lên chữ ký:</label> <input
					type="file" id="signatureFile" name="signatureFile" accept=".txt">
			</div>
			<!-- Hiển thị thông báo kết quả -->
			<div id="resultMessage" style="color: ${messageColor};">
				${resultMessage != null ? resultMessage : " "}</div>
			<button type="submit">Xác Minh</button>
		</form>


	</div>

	
		<jsp:include page="Footer.jsp" />

>>>>>>> main
	<script>
		function toggleSignatureInput() {
			var signatureOption = document.getElementById('signatureOption').value; // Lấy giá trị của signatureOption
			console.log("Lựa chọn phương thức ký: " + signatureOption); // Kiểm tra giá trị của signatureOption

<<<<<<< HEAD
			// Ẩn cả hai phần tử
			document.getElementById('textSignature').style.display = 'none';
			document.getElementById('fileSignature').style.display = 'none';
=======
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
>>>>>>> main

			// Hiển thị phần tương ứng
			if (signatureOption === 'text') {
				document.getElementById('textSignature').style.display = 'block';
			} else if (signatureOption === 'file') {
				document.getElementById('fileSignature').style.display = 'block';
			}
		}

		// Gọi hàm ngay khi trang tải để chọn phương thức ký mặc định
		window.onload = function() {
			toggleSignatureInput(); // Gọi hàm khi trang tải lên để xử lý trạng thái mặc định
		};
	</script>

</body>
</html>
