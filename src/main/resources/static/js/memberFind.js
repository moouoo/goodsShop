'use strict'
const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

const findIdBtn = document.getElementById('findIdBtn');
const findPwdBtn = document.getElementById('findPwdBtn');
const findIdForm = document.getElementById('findIdForm');
const findPwdForm = document.getElementById('findPwdForm');

findIdBtn.addEventListener('click', function() {
    findIdForm.classList.add('active');
    findPwdForm.classList.remove('active');
    findIdBtn.classList.add('active');
    findPwdBtn.classList.remove('active');
});

findPwdBtn.addEventListener('click', function() {
    findPwdForm.classList.add('active');
    findIdForm.classList.remove('active');
    findPwdBtn.classList.add('active');
    findIdBtn.classList.remove('active');
});

function findIdCheck() {
    let email = document.getElementById("email-mid").value.trim();

    if(email == "") {
        alert("이메일을 입력해주세요");
        formFindId.email.focus();
        return
    }
    else {
        fetch('/member/findIdPwd', {
        method: 'POST',
        headers: {
            [csrfHeader]: csrfToken,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email: email })
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // 성공적으로 데이터를 받아온 경우
                document.getElementById('userInfo').innerText = `아이디: ${data.member}`;
                openModal(); // 모달 열기
            } else {
                alert("아이디가 존재하지 않습니다."); // 오류 메시지 표시
            }
        })
        .catch(error => console.error('Error:', error));
    }
}

function findPwdCheck() {
    let mid = document.getElementById("mid").value.trim();
    let email = document.getElementById("email-pwd").value.trim();

    if(mid == "") {
        alert("아이디를 입력해주세요");
        formFindPwd.mid.focus();
        return
    }
    else if(email == "") {
        alert("이메일을 입력해주세요.");
        formFindPwd.email.focus();
        return
    }
    else {
        fetch('/member/findIdPwd', {
        method: 'POST',
        headers: {
            [csrfHeader]: csrfToken,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ mid: mid, email: email })
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert("새로운 비밀번호를 이메일로 보냈습니다.");
            } else {
                alert("일치하는 정보가 없습니다."); // 오류 메시지 표시
            }
        })
        .catch(error => console.error('Error:', error));
    }

}

// 모달창
function openModal() {
    document.getElementById('userInfoModal').style.display = 'block';
}

function closeModal() {
    document.getElementById('userInfoModal').style.display = 'none';
}