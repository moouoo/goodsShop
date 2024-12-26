reviewModalClose.addEventListener('click', function () {
    reviewWriteModal.style.display = 'none';
});

function OpenReviewModal(reviewProductOrderId){
    reviewWriteModal.style.display = 'flex';
    document.getElementById('reviewProductOrderId').value = reviewProductOrderId;
}

document.querySelectorAll('.star_rating > .star').forEach(star => {
    star.addEventListener('click', function () {
        // 부모 요소의 모든 자식 span에서 클래스 제거
        const allStars = this.parentNode.querySelectorAll('span');
        allStars.forEach(s => s.classList.remove('on'));

        // 현재 클릭한 요소와 이전 모든 형제 span에 클래스 추가
        this.classList.add('on');
        let prevSibling = this.previousElementSibling;
        while (prevSibling) {
            if (prevSibling.tagName === 'SPAN') {
                prevSibling.classList.add('on');
            }
            prevSibling = prevSibling.previousElementSibling;
        }

        // 현재 별의 value값을 읽고 숨겨진 input에 데이터 넣기
        const selectedRating = this.getAttribute('value');
        document.getElementById('starRatingValue').value = selectedRating;
    });
});

function reviewLimit(input, maxLength){
    let reviewText = document.getElementById('reviewText');
    let inputLength = input.value.length;

    if(maxLength < inputLength) input.value = input.value.substring(0, maxLength);
}

function reviewWrite(){
    let reviewProductOrderId = document.getElementById('reviewProductOrderId').value;
    let starRatingValue = document.getElementById('starRatingValue').value;
    let reviewText = document.getElementById('reviewText').value;

    if(reviewProductOrderId == 0 || starRatingValue == 0){
        alert('주문번호 혹은 별점점수 가져오는거 실패');
    }
    else if(reviewText == null){
        alert('리뷰를 적지 않았습니다.');
    }
    else{
        reviewWriteForm.submit();
    }

}