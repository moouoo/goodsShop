package com.spring.goodsShop.service;

import com.spring.goodsShop.vo.MemberVo;
import com.spring.goodsShop.dao.MemberDao;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService{

    @Autowired
    MemberDao memberDao;

    @Autowired
    JavaMailSender emailCheckPost;

    @Override
    public boolean midCheck(String mid) {
        boolean check;
        int count =  memberDao.midCheck(mid);

        if(count == 1) check = false;
        else check = true;

        return check;
    }

    @Override
    public boolean emailCheck(String email) {
        boolean check;
        int count =  memberDao.emailCheck(email);

        if(count == 1) check = false;
        else check = true;

        return check;
    }

    // 이메일 인증번호 보내기
    @Override
    public int sendMail(String email) {
        int number = (int)(Math.random() * (90000000)) + 10000000;// (int) Math.random() * (최댓값-최소값+1) + 최소값
        final String senderEmail= "hoon134615@gmail.com";

        MimeMessage message = emailCheckPost.createMimeMessage();
        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("goodsShop에서 보낸 이메일입니다.");
            String body = "";
            body += "<h3>" + "요청하신 번호입니다." + "</h3>";
            body += "<h1>" + number + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body,"UTF-8", "html");
        }
        catch (MessagingException e) {
            e.printStackTrace();
        }
        emailCheckPost.send(message);

        return number;
    }

    @Override
    public void memberJoin(MemberVo vo) {
        memberDao.memberJoin(vo);
    }

    @Override
    public MemberVo loginCheck(String mid) {
        return memberDao.loginCheck(mid);
    }

    @Override
    public String findMid(String email) {
        return memberDao.findMid(email);
    }

    @Override
    public boolean emailMidCheck(String mid, String email) {
        boolean check;
        int count =  memberDao.emailPwdCheck(mid, email);

        if(count == 1) check = true;
        else check = false;

        return check;
    }

    @Override
    public boolean emailMidCheck(String email) {
        boolean check;
        int count =  memberDao.emailCheck(email);

        if(count == 1) check = true;
        else check = false;

        return check;
    }

    @Override
    public void setPwd(String pwd, String mid) {
        memberDao.setPwd(pwd, mid);
    }

}
