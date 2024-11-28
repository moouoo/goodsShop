function payment(paymentInfo){
    var IMP = window.IMP; // 생략가능
    IMP.init(paymentInfo.impUid); // <-- 본인 가맹점 식별코드 삽입

    IMP.request_pay({
        pg : 'html5_inicis',
        pay_method : 'card',
        merchant_uid: paymentInfo.orderCode, //상점에서 생성한 고유 주문번호
        name : '[' + paymentInfo.designsObj + ']' + paymentInfo.productNames,
        amount : paymentInfo.finalPrice,
        buyer_email : paymentInfo.email,
        buyer_name : paymentInfo.name,
        buyer_tel : paymentInfo.phone,
        buyer_addr : paymentInfo.address,
        buyer_postcode : paymentInfo.postcode
//        m_redirect_url : '{모바일에서 결제 완료 후 리디렉션 될 URL}' // 예: https://www.my-service.com/payments/complete
    }, function(rsp) {
        if ( !rsp.success ) {
            //결제 시작 페이지로 리디렉션되기 전에 오류가 난 경우
            var msg = '오류로 인하여 결제가 시작되지 못하였습니다.';
            msg += '에러내용 : ' + rsp.error_msg;

            alert(msg);
        }
        else{
            let item = {
                productIdsObj : paymentInfo.productIdsObj,
                amountsObj : paymentInfo.amountsObj,
                address : paymentInfo.address,
                finalPrice : paymentInfo.finalPrice,
                designsObj : paymentInfo.designsObj,
                couponData : paymentInfo.couponData
            }
            // 결제 정보를 데이터베이스에 저장
            fetch("/product/saveOrderPayment", {
                method: "POST",
                headers: {
                    [csrfHeader]: csrfToken,
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(item) // 결제 정보를 서버로 전송
            })
            .then(response => {
                if (response.ok) {
                    alert("결재성공");
                    window.location.href = "/product/paymentOk";
                }
                else {
                    alert("결제 정보를 저장하는 데 실패했습니다.");
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
        }
    });
}