function moveSlide(selector, direction) {
    const sliderTrack = document.querySelector(selector);
    if (!sliderTrack) return;

    const sliderItems = sliderTrack.children;
    const totalSlides = sliderItems.length;

    // Lấy chiều rộng của mỗi item trong slider (bao gồm margin)
    const itemStyle = window.getComputedStyle(sliderItems[0]);
    const itemWidth = sliderItems[0].offsetWidth + parseFloat(itemStyle.marginRight);

    // Khởi tạo slideIndex nếu chưa có
    if (!sliderTrack.dataset.slideIndex) {
        sliderTrack.dataset.slideIndex = 0;
    }

    let slideIndex = parseInt(sliderTrack.dataset.slideIndex, 10);

    // Cập nhật chỉ số slide (tiến/lùi)
    slideIndex += direction;

    // Xử lý khi vượt qua slide cuối hoặc trước slide đầu
    if (slideIndex >= totalSlides) {
        slideIndex = 0; // Quay lại slide đầu
    } else if (slideIndex < 0) {
        slideIndex = totalSlides - 1; // Quay lại slide cuối
    }

    sliderTrack.dataset.slideIndex = slideIndex;

    // Cập nhật vị trí slider
    const offset = -(slideIndex * itemWidth);
    sliderTrack.style.transform = `translateX(${offset}px)`;
    sliderTrack.style.transition = 'transform 0.5s ease-in-out';
}



