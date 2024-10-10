let productSlideIndex = 0;

function showProductSlides() {
    const slides = document.querySelector('.slides');
    const totalSlides = slides.children.length;

    // Calculate the translateX value based on the current index
    const offset = -(productSlideIndex * (slides.children[0].clientWidth + 20)); // 20 is the margin
    slides.style.transform = `translateX(${offset}px)`;
}


function changeProductSlide(n) {
    const slides = document.querySelector('.slides');
    const totalSlides = slides.children.length;

    productSlideIndex += n;

    // Wrap around the slide index
    if (productSlideIndex >= totalSlides) {
        productSlideIndex = 0;
    } else if (productSlideIndex < 0) {
        productSlideIndex = totalSlides - 1;
    }

    showProductSlides();
}

showProductSlides();