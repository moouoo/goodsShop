package com.spring.goodsShop.service;

import com.spring.goodsShop.vo.SubcategoryVo;

import java.util.List;

public interface AdminService {
    void setMainCategory(String title);

    List getMainCategory();

    void setSubCategory(String subTitle, String main_id);

    List<SubcategoryVo> getSubCategory();

    void deleteMaincategory(String hiddenMaincategory);
}
