/**
 * 
 */
document.getElementById("copyPublicKey").addEventListener("click", function() {
	var publicKey = document.getElementById("publicKey");
	publicKey.select();
	document.execCommand("copy");
	alert("Public Key copied!");
});

document.getElementById("downloadPrivateKey").addEventListener("click", function() {
	var privateKey = "<%= privateKey %>"; // This should be a string representing the private key
	var blob = new Blob([privateKey], { type: "text/plain;charset=utf-8" });
	var link = document.createElement("a");
	link.href = URL.createObjectURL(blob);
	link.download = "private_key.pem";
	link.click();
});

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



function copyToClipboard(id) {
	var copyText = document.getElementById(id);
	copyText.select();
	document.execCommand("copy");
}

function downloadKey(id) {
	var keyText = document.getElementById(id).value;
	var blob = new Blob([keyText], { type: "text/plain" });
	var link = document.createElement("a");
	link.href = URL.createObjectURL(blob);
	link.download = id + ".txt";
	link.click();
}

// Trước khi submit form, gán giá trị của 'createTime' và 'endTime' vào các input ẩn
document.addEventListener("DOMContentLoaded", function() {
	document.getElementById("hiddenCreateTime").value = document.getElementById("createdTime").innerText;
	document.getElementById("hiddenEndTime").value = document.getElementById("validUntil").innerText;
});

// Hàm sao chép vào clipboard
function copyToClipboard(id) {
	var copyText = document.getElementById(id);
	copyText.select();
	copyText.setSelectionRange(0, 99999); // Cho di động hỗ trợ
	document.execCommand("copy");
}

// Hàm tải xuống key
function downloadKey(id) {
	var keyText = document.getElementById(id).value;
	var blob = new Blob([keyText], { type: 'text/plain' });
	var link = document.createElement('a');
	link.href = URL.createObjectURL(blob);
	link.download = id + ".txt"; // Tên file tải về
	link.click();
}

document.addEventListener("DOMContentLoaded", function() {
	// Lấy giá trị ngày tháng từ các phần tử span
	var createTimeValue = document.getElementById("createdTime").innerText;
	var endTimeValue = document.getElementById("validUntil").innerText;

	// Chuyển đổi chuỗi ngày tháng thành đối tượng Date và định dạng lại
	var createTimeDate = new Date(createTimeValue);
	var endTimeDate = new Date(endTimeValue);

	// Kiểm tra xem giá trị có hợp lệ không
	if (isNaN(createTimeDate.getTime())) {
		console.error("Thời gian tạo không hợp lệ");
	} else {
		// Định dạng lại thành chuỗi ngày tháng đúng kiểu
		document.getElementById("hiddenCreateTime").value = createTimeDate.toISOString();
	}

	if (isNaN(endTimeDate.getTime())) {
		console.error("Hiệu lực đến không hợp lệ");
	} else {
		// Định dạng lại thành chuỗi ngày tháng đúng kiểu
		document.getElementById("hiddenEndTime").value = endTimeDate.toISOString();
	}
});
document.addEventListener("DOMContentLoaded", function() {
	const privateKeyInput = document.getElementById("privateKey");
	const messageDiv = document.getElementById("message");

	// Kiểm tra nếu giá trị của privateKey không rỗng
	if (privateKeyInput.value.trim() !== "") {
		messageDiv.style.display = "block"; // Hiển thị thông báo
	}
});

function displayFileName() {
	var fileInput = document.getElementById('publicKeyFile');
	var filePathDisplay = document.getElementById('filePathDisplay');
	// Get the file name (note that the absolute path is not accessible for security reasons)
	var fileName = fileInput.files[0] ? fileInput.files[0].name : 'No file selected';
	filePathDisplay.textContent = "Đã chọn file: " + fileName;
}  
