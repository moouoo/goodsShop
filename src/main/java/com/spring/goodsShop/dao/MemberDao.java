package com.spring.goodsShop.dao;

import com.spring.goodsShop.vo.MemberVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberDao {
    int midCheck(@Param("mid") String mid);

    int emailCheck(@Param("email") String email);

    void memberJoin(@Param("vo") MemberVo vo);

    MemberVo loginCheck(@Param("mid") String mid);

    String findMid(@Param("email") String email);

    int emailPwdCheck(@Param("mid") String mid, @Param("email") String email);

    int emailCheck2(@Param("email") String email);

    void setPwd(@Param("pwd") String pwd, @Param("mid") String mid);

    List<MemberVo> getMember();

    void memberDelete(@Param("mid") String mid, @Param("email") String email);
}
