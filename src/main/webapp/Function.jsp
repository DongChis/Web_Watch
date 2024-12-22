<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Function Page</title>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
<link rel="stylesheet" href="css/function.css">
</head>
<body>

	<button id="backButton" class="btn"
		onclick="window.location.href='/Web_Watch/home';">
		<i class="fas fa-arrow-left"></i> Back
	</button>

	<div class="container">
		<!-- Feedback Message -->
		<c:if test="${not empty message}">
			<div
				class="${messageType == 'success' ? 'alert alert-success' : 'alert alert-danger'}">
				${message}</div>
		</c:if>

		<!-- Card: Key Information -->
		<div class="card">
			<h2>Thông tin khóa đang sử dụng</h2>
			<div class="key-info">
				<form action="keyControl" method="post">
					<p>
						<strong>Tài khoản sở hữu:</strong> <span>${sessionScope.accSession.username}</span>
					</p>
					<p>
						<strong>Thời gian tạo:</strong> <input type="text"
							id="createdTime" name="createTime" value="${createTime}" />
					</p>
					<p>
						<strong>Hiệu lực đến:</strong> <input type="text" id="validUntil"
							name="endTime" value="${endTime}" />
					</p>
					<div class="key-display">
						<!-- Public Key Display -->
						<c:choose>
							<c:when test="${not empty publicKey}">
								<div class="key-item">
									<p>
										<strong>Public Key:</strong>
									</p>
									<div class="input-wrapper">
										<input type="text" id="publicKey" name="publicKey" readonly
											value="${publicKey}" />
										<button type="button" id="copyPublicKey"
											onclick="copyToClipboard('publicKey')">
											<i class="far fa-copy"></i>
										</button>
										<button type="button" id="downloadPublicKey"
											onclick="downloadKey('publicKey')">
											<i class="fas fa-arrow-down"></i>
										</button>
									</div>
								</div>
							</c:when>
							<c:otherwise>
								<div class="key-item">
									<p>
										<strong>Public Key:</strong>
									</p>
									<span>Chưa có khóa công khai</span>
								</div>
							</c:otherwise>
						</c:choose>

						<!-- Private Key Display -->
						<div class="key-item">
							<p>
								<strong>Tải xuống Private Key</strong>
							</p>
							<div class="input-wrapper">
								<input type="hidden" id="privateKey" name="privateKey" readonly
									value="${privateKey}" />

								<button type="button" id="downloadPrivateKey"
									onclick="downloadKey('privateKey')">
									<i class="fas fa-arrow-down"></i>
								</button>
							</div>
						</div>

						<!-- Hidden input to specify the action -->
						<input type="hidden" name="action" value="importKey" />

						<div class="form-group">
							<button type="submit" class="btn">Lưu</button>
						</div>
					</div>
				</form>
			</div>
		</div>

		<!-- Card: Create New Key -->
		<div class="card">
			<h2>Tạo Khóa Mới</h2>
			<form id="keyGenForm" action="keyControl" method="POST">
				<input type="hidden" name="action" value="generateKey" />
				<div class="form-group">
					<label for="keySize">Chọn kích thước khóa:</label> <select
						id="keySize" name="keySize" required>
						<option value="1024">1024 bit</option>
						<option value="2048" selected>2048 bit</option>
						<option value="4096">4096 bit</option>
					</select>
				</div>

				<div class="form-group">
					<button type="submit" class="btn">Tạo Khóa</button>
				</div>
			</form>

			<!-- Card to display generated keys -->
			<div id="keyDisplay" class="key-display" style="display: none;">
				<h3>Thông tin khóa</h3>
				<div class="key-item">
					<p>
						<strong>Public Key:</strong>
					</p>
					<textarea id="publicKey" readonly rows="5" cols="50"></textarea>
					<button id="copyPublicKey" class="btn">Sao chép Public Key</button>
				</div>
				<div class="key-item">
					<p>
						<strong>Private Key:</strong>
					</p>
					<p id="privateKeyMessage">Khóa riêng tư không hiển thị. Bạn có
						thể tải về.</p>
					<button id="downloadPrivateKey" class="btn">Tải về Private
						Key</button>
				</div>
			</div>
		</div>

		<!-- Card: Import Key -->
		<!-- Card: Import Key -->
<div class="card">
    <h2>Nhập Khóa Công Khai</h2>
    <form id="keyImportForm" action="keyControl" method="POST" enctype="multipart/form-data">
        <input type="hidden" name="action" value="importKey" />

        <!-- Public Key File Upload -->
        <div class="form-group">
            <label for="publicKeyFile">Tải lên tệp khóa công khai:</label> 
            <input type="file" id="publicKeyFile" name="publicKeyFile" accept=".key, .txt" />
        </div>

        <!-- Public Key Input -->
        <div class="form-group">
            <label for="publicKeyText">Hoặc nhập trực tiếp khóa công khai:</label>
            <input type="text" id="publicKey" name="publicKey" />
        </div>

        <!-- Submit Button -->
        <div class="form-group">
            <button type="submit" class="btn">Tải Khóa Lên</button>
        </div>
    </form>
</div>




		<!-- Card: Report Key -->
		<div class="card">
			<h2>Báo Mất Khóa</h2>
			<form id="keyReportForm" action="keyControl" method="POST">
				<input type="hidden" name="action" value="reportKey" />
				<div class="form-group">
					<label for="reportPrivateKey">Nhập khóa Private cần báo
						mất:</label> <input type="text" id="reportPrivateKey"
						name="reportPrivateKey" required />
				</div>
				<div class="form-group">
					<button type="submit" class="btn">Báo Mất</button>
				</div>
			</form>
		</div>
	</div>

	<script src="js/key.js"></script>
</body>
</html>
