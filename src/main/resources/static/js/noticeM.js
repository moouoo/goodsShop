'use strict';

const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

function deleteNotice(noticeId){
    if(confirm('해당 공지를 삭제하시겠습니까?')){
        fetch('/admin/deleteNotice',{
            method: 'POST',
            headers: {
                [csrfHeader]: csrfToken,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ noticeId : noticeId })
        })
        .then(response => response.json())
        .then(data => {
            if(data.success){
                alert('삭제되었습니다.');
                window.location.href='/admin/noticeM';
            }
            else{
                alert('notice 삭제실패.');
            }
        })
        .catch(error => {
            console.error(error);
        });
    }
    else alert('취소되었습니다.');
}