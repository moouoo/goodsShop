package com.spring.goodsShop.service;

import com.spring.goodsShop.vo.MemberVo;

import java.util.List;

public interface MemberService {

    boolean midCheck(String mid);

    boolean emailCheck(String email);

    int sendMail(String email);

    void memberJoin(MemberVo vo);

    MemberVo loginCheck(String mid);

    String findMid(String email);

    boolean emailMidCheck(String mid, String email);

    boolean emailMidCheck(String email);

    void setPwd(String pwd, String mid);

    List<MemberVo> getMember();

    void memberDelete(String mid, String email);
}
