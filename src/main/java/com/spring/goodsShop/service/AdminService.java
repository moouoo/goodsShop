package com.spring.goodsShop.service;

import com.spring.goodsShop.vo.SubcategoryVo;

import java.util.List;

public interface AdminService {
    void setMainCategory(String title);

    List getMainCategory();

    void setSubCategory(String subTitle, String main_id);

    List<SubcategoryVo> getSubCategory();

    void deleteMaincategory(String hiddenMaincategory);

    void updateMaincategory(String title, int id);

    boolean checkMaincategory(String title);

    void deleteSubcatergory(String hiddenSubcategory);

    boolean befUpdateSubcategory(String before_title, int id);

    void UpdateSubcategory(String sub_update_title, int id);

    List<SubcategoryVo> getSubCategory(int mainCategoryId);
}
