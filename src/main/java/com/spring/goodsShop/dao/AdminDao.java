package com.spring.goodsShop.dao;

import com.spring.goodsShop.vo.SubcategoryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminDao {
    void setMainCategory(@Param("title") String title);

    List getMainCategory();

    void setSubCategory(@Param("subTitle") String subTitle, @Param("main_id") String main_id);

    List<SubcategoryVo> getSubCategory();

    void deleteMaincategory(@Param("hiddenMaincategory") String hiddenMaincategory);
}
