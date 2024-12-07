<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Function Page</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="css/function.css">
</head>
<body>

<button id="backButton" class="btn" onclick="window.location.href='/Web_Watch/home';"><i class="fas fa-arrow-left"></i> Back</button>

<div class="container">
    <!-- Card: Thông Tin Khóa -->
    <div class="card">
        <h2>Thông Tin Khóa</h2>
        <div class="key-info">
            <p><strong>Thời gian tạo:</strong> <span id="createdTime">${createdTime}</span></p>
            <p><strong>Hiệu lực đến:</strong> <span id="validUntil">${expirationTime}</span></p>
            <div class="key-display">
                <div class="key-item">
                    <p><strong>Public Key:</strong></p>
                    <div class="input-wrapper">
                        <input type="text" id="publicKey" readonly value="${publicKey}" />
                        <button id="copyPublicKey"><i class="far fa-copy"></i></button>
                        <button id="downloadPublicKey"><i class="fas fa-arrow-down"></i></button>
                    </div>
                </div>
                <div class="key-item">
                    <p><strong>Private Key:</strong></p>
                    <div class="input-wrapper">
                        <input type="text" id="privateKey" readonly value="${privateKey}" />
                        <button id="copyPrivateKey"><i class="far fa-copy"></i></button>
                        <button id="downloadPrivateKey"><i class="fas fa-arrow-down"></i></button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Card: Tạo Khóa Mới -->
    <div class="card">
        <h2>Tạo Khóa Mới</h2>
        <form id="keyGenForm" action="keyControl" method="POST">
            <input type="hidden" name="action" value="generateKey"/>
            <div class="form-group">
                <label for="keySize">Chọn kích thước khóa:</label>
                <select id="keySize" name="keySize" required>
                    <option value="1024">1024 bit</option>
                    <option value="2048" selected>2048 bit</option>
                    <option value="4096">4096 bit</option>
                </select>
            </div>
            <div class="form-group">
                <button type="submit" class="btn">Tạo Khóa</button>
            </div>
        </form>
    </div>

    <!-- Card: Nhập Khóa Đã Có -->
    <div class="card">
        <h2>Nhập Khóa Đã Có</h2>
        <form id="keyImportForm" action="keyControl" method="POST" enctype="multipart/form-data">
            <input type="hidden" name="action" value="importKey"/>
            <div class="form-group">
                <label for="privateKeyFile">Tải lên file khóa Private:</label>
                <input type="file" id="privateKeyFile" name="privateKeyFile" accept=".key, .txt" />
            </div>
            <div class="form-group">
                <label for="privateKeyText">Hoặc nhập trực tiếp khóa Private:</label>
                <div class="input-wrapper">
                    <input type="text" id="privateKeyText" name="privateKeyText" placeholder="Nhập khóa Private của bạn ở đây..." />
                    <button id="copyPrivateKeyText"><i class="far fa-copy"></i></button>
                </div>
            </div>
            <button type="submit" class="btn">Xác Nhận Khóa</button>
        </form>
    </div>

    <!-- Card: Báo Cáo Khóa -->
    <div class="card">
        <h2>Báo Cáo Khóa Private Bị Lộ</h2>
        <form id="keyReportForm" action="keyControl" method="POST">
            <input type="hidden" name="action" value="reportKey"/>
            <div class="form-group">
                <label for="privateKeyFile">Tải lên file khóa Private bị lộ:</label>
                <input type="file" id="privateKeyFile" name="privateKeyFile" accept=".key, .txt" />
            </div>
            <div class="form-group">
                <label for="reportPrivateKey">Nhập khóa Private bị lộ:</label>
                <div class="input-wrapper">
                    <input type="text" id="reportPrivateKey" name="reportPrivateKey" placeholder="Nhập khóa Private bị lộ ở đây..." required />
                    <button id="copyReportPrivateKey"><i class="far fa-copy"></i></button>
                </div>
            </div>
            <button type="submit" class="btn">Báo Cáo</button>
        </form>
    </div>

</div>

<script>
    // Copy functionality for the input fields
    function copyTextToClipboard(id) {
        const input = document.getElementById(id);
        input.select();
        document.execCommand('copy');
    }

    document.getElementById("copyPublicKey").addEventListener("click", function() {
        copyTextToClipboard("publicKey");
    });

    document.getElementById("copyPrivateKey").addEventListener("click", function() {
        copyTextToClipboard("privateKey");
    });

    document.getElementById("copyPrivateKeyText").addEventListener("click", function() {
        copyTextToClipboard("privateKeyText");
    });

    document.getElementById("copyReportPrivateKey").addEventListener("click", function() {
        copyTextToClipboard("reportPrivateKey");
    });
    
    document.getElementById("downloadPublicKey").addEventListener("click", function() {
        const publicKeyText = document.getElementById("publicKey").value;
        downloadFile(publicKeyText, "publicKey.txt");
    });

    document.getElementById("downloadPrivateKey").addEventListener("click", function() {
        const privateKeyText = document.getElementById("privateKey").value;
        downloadFile(privateKeyText, "privateKey.txt");
    });

    function downloadFile(content, fileName) {
        const blob = new Blob([content], { type: 'text/plain' });
        const link = document.createElement('a');
        link.href = URL.createObjectURL(blob);
        link.download = fileName;
        link.click();
    }

</script>

</body>
</html>
