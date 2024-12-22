<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<link href="css/readme.css" rel="stylesheet">
</head>
<body>
	<jsp:include page="Header.jsp" />

 <main>
        <section id="signature-info">
            <h2>Thông tin người dùng</h2>
            <form id="signature-info-form">
                <label for="username">Tên tài khoản:</label>
                <input type="text" id="username" name="username" value = "${username}">

                <label for="password">Mật khẩu:</label>
                <input type="password" id="password" name="password" value = "${password}">

                <label for="email">Email:</label>
                <input type="email" id="email" name="email" value = "${email}">

           <button class="a" type="button" onclick="downloadSignatureInfo()">Tải thông tin</button>
           <a href="ReadmeDetail.jsp">Xem hướng dẫn chi tiết tại đây</a>
            </form>
            <div id="download-status"></div>
        </section>

        <section id="signature-tool-download">
            <h2>Tải công cụ chữ ký điện tử</h2>
            <button class="a" type="button" onclick="downloadSignatureTool()">Tải công cụ</button>
            <div id="tool-download-status"></div>
        </section>
    </main>
   

   	<jsp:include page="Footer.jsp" />
    <script>
        // Hàm tải thông tin chữ ký điện tử về dưới dạng file
        function downloadSignatureInfo() {
            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;
            const email = document.getElementById('email').value;
            
            if (!username || !password || !email) {
                document.getElementById('download-status').textContent = "Vui lòng điền đầy đủ thông tin trước khi tải xuống.";
                return;
            }

            const signatureData = {
                username,
                password,
                email
              
            };

            const blob = new Blob([JSON.stringify(signatureData, null, 2)], { type: '.txt' });
            const url = URL.createObjectURL(blob);

            const a = document.createElement('a');
            a.href = url;
            a.download = 'signature-info.txt';
            a.click();

            URL.revokeObjectURL(url);
            document.getElementById('download-status').textContent = "Tải xuống thành công!";
        }

        // Hàm tải công cụ chữ ký điện tử
        function downloadSignatureTool() {
            const url = '/path-to-signature-tool.zip'; // Đường dẫn tới tệp công cụ chữ ký điện tử

            const a = document.createElement('a');
            a.href = url;
            a.download = 'signature-tool.zip';
            a.click();

            document.getElementById('tool-download-status').textContent = "Đang tải xuống công cụ...";
        }
    </script>
</body>
</html>