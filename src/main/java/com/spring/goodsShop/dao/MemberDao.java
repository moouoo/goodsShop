package com.spring.goodsShop.dao;

import com.spring.goodsShop.vo.MemberVo;
import com.spring.goodsShop.vo.ProductVo;
import com.spring.goodsShop.vo.Product_imgVo;
import com.spring.goodsShop.vo.SubcategoryVo;
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

    MemberVo getMember2(@Param("mid") String mid, @Param("email") String email);

    List<SubcategoryVo> getSubCategoriesByMainCategory(@Param("email") String mainCategory);

    void setAccount_num(@Param("account_num") String account_num, @Param("mid") String mid);

    void updateLevel(@Param("level") int level);

    int getMemberIdBymid(@Param("mid") String mid);

    void setProduct_img(@Param("imgVo") Product_imgVo imgVo);

    int getProductImgIdByImg1(@Param("img1") String img1);

    void setProduct(@Param("productVo") ProductVo productVo);

    void pwdSet(@Param("encodePwd") String encodePwd, @Param("mid") String mid);

    String findEmailByMid( @Param("mid") String mid);
}
