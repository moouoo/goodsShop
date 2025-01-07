let qnaModalForm = document.getElementById('qnaModalForm');

qnaModalClose.addEventListener('click', function () {
    qnaModal.style.display = 'none';
});

function qnaCheck(){
    let qnaReplyContent = document.getElementById('qnaReplyContent').value;
    let productQId = document.getElementById('productQId').value;

    if(qnaReplyContent.length > 300 || qnaReplyContent == ""){
        alert('문의내용이 너무 길어요. 최대 300자까지 입력이 가능합니다.');
        return;
    }
    else if(productQId == 0){
        alert('데이터받아오는거 에러');
        return;
    }
    qnaModalForm.submit();
}

let qnaTitle = document.querySelectorAll('.qnaTitle');

qnaTitle.forEach(title => {
    title.addEventListener('click', async () => {
        let productQId = title.getAttribute('productQId');
        let reply = title.getAttribute('reply');

        try{
            let response = await fetch('/member/qna', {
                method: 'POST',
                headers: {
                        [csrfHeader]: csrfToken,
                        'Content-Type': 'application/json',
                    },
                body: JSON.stringify({ productQId: productQId })
            });
            // 응답 상태 확인
            if (!response.ok) throw new Error("데이터 로드 실패");
            // JSON 파싱
            const data = await response.json();

            if(data.success){
                document.getElementById('qnaModalContent').textContent = data.qnaContent;
                document.getElementById('productQId').value = data.productQId;
                qnaModal.style.display = 'flex';
            }
            else{
                alert('data.success, false');
            }
        }
        catch(error){
            alert('데이터를 가져오는 도중 오류가 발생햇습니다. qna');
            console.error('Error:', error);
        }
    })
})

async function qnaView(productQId, qnaViewCount){
    try{
        let response = await fetch('/member/qnaView',{
            method: 'post',
            headers: {
                 [csrfHeader]: csrfToken,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ productQId: productQId, qnaViewCount : qnaViewCount })
        });

        if(!response.ok) throw new Error("데이터 로드 실패");
        const data = await response.json();

        if(data.success){
            let qnaViewReplyContent = document.getElementById('qnaViewReplyContent' + `${qnaViewCount}`);
            document.getElementById('qnaViewReplyContent' + `${qnaViewCount}`).innerHTML = data.htmlReplyContent;
            qnaViewReplyContent.style.display =  'block';
        }
        else{
            alert('qnaView, 실패!');
        }
    }
    catch(error){
        alert('데이터를 가져오는 도중 오류가 발생햇습니다. qnaView');
        console.error('Error:', error);
    }
}

function qnaViewClose(qnaViewCount){
     let qnaViewReplyContent = document.getElementById('qnaViewReplyContent' + `${qnaViewCount}`);
     qnaViewReplyContent.style.display = 'none';
}