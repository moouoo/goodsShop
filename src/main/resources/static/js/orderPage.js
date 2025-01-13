'use strict'

const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

const modal = document.getElementById("couponModal");
const closeModalBtn = document.querySelector(".close-btn");

let totalPrice = 0;
const orderPriceField = document.querySelectorAll('.order-price');

orderPriceField.forEach(field => {
    const index = field.id.split('_')[1];
    item_price_view(index);

})

function item_price_view(index){
    const itemPrice = parseInt(document.getElementById(`itemPrice_${index}`).textContent);

    if (!isNaN(itemPrice)) {
        totalPrice += itemPrice;
        document.getElementById('totalPrice').textContent = totalPrice.toLocaleString() + ' 원';
    }
    // 각 상품의 가격 포맷
    let itemPriceTem = parseInt(document.getElementById(`itemPrice_${index}`).textContent);
    document.getElementById(`itemPrice_${index}`).textContent = itemPriceTem.toLocaleString() + ' 원';

    // 배송비 결정 (50000미만 2500원, 이상 0원)
    if(totalPrice < 50000) document.getElementById('delivery_fee').textContent = '2,500 원'
    else document.getElementById('delivery_fee').textContent = '0 원'
}

// 이메일 도매인 직접 입력 선택 시
function checkCustomEmail(){
    let email_custom = document.getElementById("email2").value;
    let customEmail = document.getElementById("customEmail");

    if(email_custom === "custom"){
        customEmail.style.display = "inline-block";
    }
    else {
        customEmail.style.display = "none";
    }
}

function checkCustomMsg(){
    let msg_custom = document.getElementById("message").value;
    let customMsg = document.getElementById("customMsg")

    if(msg_custom == "custom"){
        customMsg.style.display = "inline-block";
    }
    else {
        customMsg.style.display = "none";
    }
}

// 모달창
function couponModal(couponList, id, productId, design, amount) {
    document.getElementById('couponModal').style.display = 'flex';
    // 쿠폰 테이블 가져오기
    const tableBody = document.querySelector('#couponModal table tbody');

    // 기존 행 제거
    tableBody.innerHTML = '';

    // 쿠폰 데이터를 테이블에 추가
    couponList.forEach(coupon => {
        const row = `
            <tr class="couponUse${coupon.id}">
                <td>
                    <input type="radio" name="couponSelect" value="${coupon.id}" id="coupon${coupon.id}">

                </td>
                <td>${coupon.coupon_name}</td>
                <td>${coupon.coupon_rate}</td>
                <td>${coupon.end_date}</td>
            </tr>
        `;
        tableBody.insertAdjacentHTML('beforeend', row);
    });

    // 적용 버튼의 onclick 속성에 id 값을 동적으로 설정
    // '적용' 버튼을 동적으로 찾고, onclick 속성 설정
    const couponCheckBtn = document.querySelector('.action-btn');
    if (couponCheckBtn) {
        couponCheckBtn.setAttribute('onclick', `couponCheck(${id}, ${productId}, '${design}', ${amount})`);
    }
    else{
        alert('radio버튼 오류');
    }
}

function closeModal() {
    document.getElementById('couponModal').style.display = 'none';
}

// 모달 닫기 함수 (닫기 버튼 클릭 시)
closeModalBtn.addEventListener("click", () => {
    modal.style.display = "none";
});

// 모달 닫기 함수 (배경 클릭 시)
window.addEventListener("click", (e) => {
    if (e.target === modal) {
        modal.style.display = "none";
    }
});

// 각 상품에 사용할 수 있는 쿠폰 가져오기
function selectCouponForItem(subcategoryId, maincategoryId, idTem, productId, design, amount){
    fetch('/product/selectCouponForItem', {
        method: 'POST',
        headers: {
            [csrfHeader]: csrfToken,
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            subcategoryId : subcategoryId,
            maincategoryId : maincategoryId,
            id : idTem,
            productId : productId,
            design : design,
            amount : amount
        })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        if(data.login){
            if (data.success) couponModal(data.couponList, data.id, data.productId, data.design, data.amount);
            else alert("쿠폰리스트불러오기 실패");
        }
        else{
            alert('로그인세션이 종료되었습니다.');
            location.href = '/member/login';
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert("쿠폰가져오는 중에 에러가 발생했습니다. 관리자야 일해라.");
    });
}

const couponData = []; // 쿠폰 결과를 담을 배열
function couponCheck(id, productId, design, amount){
    // 선택된 라디오 버튼 가져오기
    const selectedCoupon = document.querySelector('input[name="couponSelect"]:checked');
    let couponId = selectedCoupon ? selectedCoupon.value.trim() : "";

    couponData.push({productId, couponId, design});

    // css를 부여해서 radio버튼 비활성하여 중복 쿠폰적용을 막아야함. 지금까지 실패중
    selectedCoupon.classList.add('deactivate');
    selectedCoupon.disabled = true;

    // 할인결과를 위한 선언
    // const availablePoints = [[${discount_point}]];
    let pointInput = document.getElementById("reward-points");
    let enteredPoints = parseInt(pointInput.value) || 0;

    let priceElement = document.getElementById(`itemPrice_${id}`);
    let price = priceElement ? parseInt(priceElement.textContent.replace(/[^0-9]/g, '')) : 0;

    if(couponId == "") alert('쿠폰을 선택해주세요.');
    else if(price == 0) alert('상품가격을 가져올 수 없습니다. 문의해주세요.');

    fetch('/product/couponSelect', {
        method: 'POST',
        headers: {
            [csrfHeader]: csrfToken,
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            couponId: couponId,
            price: price
        })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        if (data.success){
            modal.style.display = "none";

            let discountPrice = parseInt(data.discountedPrice);
            let price = parseInt(data.price);
            let finPrice = price - discountPrice;

            document.getElementById(`itemPrice_${id}`).textContent = finPrice.toLocaleString() + ' 원';
            document.getElementById(`itemPrice_${id}`).classList.add('discountPriceRed');

            let finalDiscountPriceStr = document.getElementById('finalDiscountPrice').textContent;
            let finalDiscountPrice = parseInt(finalDiscountPriceStr.replace(/[^0-9]/g, ''), 10);

            if(enteredPoints === 0) {
                if(finalDiscountPrice === 0){
                    document.getElementById('finalDiscountPrice').textContent = '- ' + discountPrice.toLocaleString() + ' 원';
                    document.getElementById('couponDiscountPrice').textContent = '- ' + discountPrice.toLocaleString() + ' 원';
                    finalPrice();
                }
                else{
                    let updateFinalDiscountPrice = finalDiscountPrice + discountPrice;
                    document.getElementById('finalDiscountPrice').textContent = '- ' + updateFinalDiscountPrice.toLocaleString() + ' 원';
                    document.getElementById('couponDiscountPrice').textContent = '- ' + updateFinalDiscountPrice.toLocaleString() + ' 원';
                    finalPrice();
                }
            }
            else{
                if(finalDiscountPrice === 0){
                    document.getElementById('finalDiscountPrice').textContent = '- ' + discountPrice.toLocaleString() + ' 원';
                    document.getElementById('couponDiscountPrice').textContent = '- ' + discountPrice.toLocaleString() + ' 원';
                    finalPrice();
                }
                else{
                    let updateFinalDiscountPrice = finalDiscountPrice + discountPrice;
                    document.getElementById('finalDiscountPrice').textContent = '- ' + updateFinalDiscountPrice.toLocaleString() + ' 원';
                    document.getElementById('couponDiscountPrice').textContent = '- ' + updateFinalDiscountPrice.toLocaleString() + ' 원';
                    finalPrice();
                }
            }
        }
        else{
            alert("쿠폰적용실패");
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert("쿠폰적용중에 에러가 발생했습니다. 관리자야 일해라.");
    });
}

function finalPrice(){
    //최종가격
    let totalPriceStr = document.getElementById('totalPrice').textContent;
    let totalPrice = parseInt(totalPriceStr.replace(/[^0-9]/g, ''), 10);

    let delivery_feeStr = document.getElementById('delivery_fee').textContent;
    let delivery_fee =  parseInt(delivery_feeStr.replace(/[^0-9]/g, ''), 10);

    let discountPriceStr = document.getElementById('finalDiscountPrice').textContent;
    let discountPrice = parseInt(discountPriceStr.replace(/[^0-9]/g, ''), 10);

    document.getElementById('final_price').textContent = (totalPrice + delivery_fee - discountPrice).toLocaleString() + ' 원';

    // 할인요인에 따른 배송비의 변화 재계산.
    let finalPriceStr = document.getElementById('final_price').textContent;
    let finalPrice = parseInt(finalPriceStr.replace(/[^0-9]/g, ''), 10);

    if(finalPrice < 50000) document.getElementById('delivery_fee').textContent = '2,500 원';
    else document.getElementById('delivery_fee').textContent = '0 원';

    let delivery_feeStr2 = document.getElementById('delivery_fee').textContent;
    let delivery_fee2 =  parseInt(delivery_feeStr2.replace(/[^0-9]/g, ''), 10);
    document.getElementById('final_price').textContent = (totalPrice + delivery_fee2 - discountPrice).toLocaleString() + ' 원';
}
finalPrice();

// 숫자만 입력되게 감시
function validateNumberInput(input){
    input.value = input.value.replace(/[^0-9]/g, '');
}

function discountPointCheck(){
//    const availablePoints = [[${discount_point}]];
    let pointInput = document.getElementById("reward-points");
    let enteredPoints = parseInt(pointInput.value) || 0;

    let couponDiscountPriceStr = document.getElementById('couponDiscountPrice').textContent;
    let couponDiscountPrice = parseInt(couponDiscountPriceStr.replace(/[^0-9]/g, ''), 10);

    // 사용자가 입력한 값이 현재 포인트보다 클 경우 경고 메시지와 입력값 초기화
    if (enteredPoints > availablePoints) {
        alert("입력한 포인트가 보유한 포인트보다 큽니다. 다시 입력해주세요.");
        pointInput.value = '';  // 입력값 초기화

        // 할인값 초기화
        if(couponDiscountPrice === 0) {
            document.getElementById('finalDiscountPrice').textContent = '- 0원';
            finalPrice();
        }
        else {
            document.getElementById('finalDiscountPrice').textContent = couponDiscountPrice.toLocaleString() + ' 원';
            finalPrice();
        }
    }
    else {
        if(couponDiscountPrice === 0) {
            document.getElementById('finalDiscountPrice').textContent = '- ' + enteredPoints.toLocaleString() + ' 원';;
            finalPrice();
        }
        else {
            document.getElementById('finalDiscountPrice').textContent = '- ' + (couponDiscountPrice + enteredPoints).toLocaleString() + ' 원';;;
            finalPrice();
        }
    }

}

function allInPoint(){
//    const availablePoints = [[${discount_point}]];
    document.getElementById("reward-points").value = availablePoints;

    let couponDiscountPriceStr = document.getElementById('couponDiscountPrice').textContent;
    let couponDiscountPrice = parseInt(couponDiscountPriceStr.replace(/[^0-9]/g, ''), 10);

    document.getElementById('finalDiscountPrice').textContent = (couponDiscountPrice + availablePoints).toLocaleString() + ' 원';
    finalPrice();
}

function showPaymentForm(method) {
    // 모든 결제 양식 숨기기
    const paymentForms = document.querySelectorAll('.payment-form');
    paymentForms.forEach(form => {
        form.style.display = 'none';
    });

    // 선택한 결제 방식 양식만 보이기
    const selectedForm = document.getElementById(method);
    if (selectedForm) {
        selectedForm.style.display = 'block';
    }
}

function payCheck(){
    let email1 = document.getElementById('email1').value.trim();
    let email2 = document.getElementById('email2').value.trim();
    let email = email1 + '@' + email2;
    let phone_code = document.getElementById('phone-code').value.trim();
    let phone1 = document.getElementById('phone1').value.trim();
    let phone2 = document.getElementById('phone2').value.trim();
    let phone = phone_code + '-' + phone1 + '-' + phone2;
    let name = document.getElementById('name').value.trim();
    let sample6_address = document.getElementById('sample6_address').value.trim();
    let sample6_detailAddress = document.getElementById('sample6_detailAddress').value.trim();
    let sample6_extraAddress = document.getElementById('sample6_extraAddress').value.trim();

    let address;
    if(sample6_detailAddress == "") address = sample6_address + ' ' + sample6_extraAddress;
    else if(sample6_extraAddress == "") address = sample6_address + ' ' + sample6_detailAddress;
    else address = sample6_address + ' ' + sample6_detailAddress + ' ' + sample6_extraAddress;

    let postcode = document.getElementById('sample6_postcode').value.trim();

    let priceInWon = document.getElementById('final_price').textContent;
    let finalPrice = parseInt(priceInWon.replace(/[^0-9]/g, ''));

    let rewardPoint = document.getElementById('reward-points').value.trim();

    const url = new URL(window.location.href); // 현재 페이지의 URL 가져오기
    let productId = url.searchParams.getAll('productId'); // 'productId'에 해당하는 모든 값 가져오기
    let amount = url.searchParams.getAll('amount');
    let design = url.searchParams.getAll('design');

    if(email1 == "" || email2 == ""){
        alert('이메일을 입력해주세요');
        return;
    }
    else if(phone1 == "" || phone2 == ""){
        alert('휴대폰을 입력해주세요.');
        return;
    }
    else if(sample6_address == "" || postcode == ""){
        alert('주소를 적어주세요.');
        return;
    }
    else if(name == ""){
        alert('이름을 적어주세요');
        return;
    }

    let item = {
        email : email,
        phone : phone,
        name : name,
        address : address,
        postcode : postcode,
        finalPrice : finalPrice,
        amount : amount,
        productId : productId,
        couponData : couponData,
        design : design,
        rewardPoint : rewardPoint
    };

    fetch("/product/payment", {
        method: "POST",
        headers: {
            [csrfHeader]: csrfToken,
            "Content-Type": "application/json"
        },
        body: JSON.stringify(item)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        if (data.success) {
            return payment(data.paymentInfo) // 결제 처리 완료를 기다림
        }
        else {
            alert("결제 실패");
        }
    })
}