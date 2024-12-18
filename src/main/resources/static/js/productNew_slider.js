let currentIndex = 0; // 현재 슬라이드 인덱스
const slider = document.querySelector(".slider");
const slideItem = document.querySelectorAll(".slideItem").length;
const visibleCards = 4; // 한 번에 보여지는 카드 수
const cardWidth = 240; // 카드 너비 + 여백

function moveSlide(direction) {
    currentIndex += direction;

    // 경계값 확인
    if (currentIndex < 0) {
        currentIndex = 0;
    }
    else if (currentIndex > slideItem - visibleCards) {
        currentIndex = slideItem - visibleCards + 1;
    }

    slider.style.transform = `translateX(${-currentIndex * cardWidth}px)`;
}