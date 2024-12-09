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