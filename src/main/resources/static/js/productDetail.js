'use strict'
const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

function changeImage(src) {
    const mainImage = document.getElementById('main-image');
    mainImage.src = src;
}

function defOrderCheck(){
    let design = document.getElementById("design").value.trim();
    let amount = document.getElementById("amount").value.trim();

    if(design == "" || amount == "" || amount == 0){
        alert("필수옵션 혹은 수량을 입력해주세요.");
    }
    else{
        befOrderForm.submit();
    }
}

document.querySelectorAll('.tab-item').forEach(item => {
    item.addEventListener('click', () => {
        const tab = item.getAttribute('data-tab');

        // 탭 활성화
        document.querySelectorAll('.tab-item').forEach(tabItem => {
            tabItem.classList.remove('active');
        });
        item.classList.add('active');

        // 콘텐츠 표시
        document.querySelectorAll('.tab-pane').forEach(pane => {
            pane.classList.remove('active');
            if (pane.id === tab) {
                pane.classList.add('active');
            }
        });
    });
});

function addToCart(productId){
    let design = document.getElementById("design").value.trim();
    let amount = document.getElementById("amount").value.trim();
    let price = parseInt(document.getElementById("price").textContent);

    if(design == "" || amount == "" || amount == 0){
        alert("필수옵션 혹은 수량을 입력해주세요.");
    }
    else{
        let item = {
            productId : productId,
            design : design,
            amount : amount,
            price : price
        }

        fetch('/product/addToCart', {
            method: 'POST',
            headers: {
                [csrfHeader]: csrfToken,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(item)
        })
        .then(response => response.json())
        .then(data => {
            if(data.noLogin){
                alert("로그인이 필요한 서비스입니다.");
                window.location.href = "/member/login";
            }
            if(data.success) {
                window.location.href = "/product/cart";
            }
            else {
                alert('장바구니 등록 실패');
            }
        })
        .catch(error => console.error('Error:', error));
    }
}

function addWishList(productId){
    fetch('/member/addWishList',{
        method: "POST",
        headers: {
            [csrfHeader]: csrfToken,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ productId : productId })
    })
    .then(response => response.json())
    .then(data => {
        if(!data.loginOk) {
            console.log(data.loginOk);
            alert('로그인을 하셔야 관심상품에 등록할 수 있습니다.');
            window.location.href='/member/login';
            return;
        }
        if(data.success){
            alert('위시리스트에 등록되었습니다.');
        }
        else alert('위시리스트에 존재하는 상품입니다.');
    })
    .catch(error => {
        console.error(error);
        alert('서버에 오류가 발생했습니다.');
    });
}