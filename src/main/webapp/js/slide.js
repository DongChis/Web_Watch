let slideIndex = 0;
const slides = document.querySelector('.slides');
const totalSlides = slides.children.length;

function showSlides() {
    slideIndex++;
    if (slideIndex >= totalSlides) {
        slideIndex = 0;
    }
    slides.style.transform = `translateX(-${slideIndex * 100}%)`;
    setTimeout(showSlides, 3000); // Thay đổi slide mỗi 3 giây
}

// Khởi động slideshow
showSlides();