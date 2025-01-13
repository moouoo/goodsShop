package com.spring.goodsShop.dao;

import com.spring.goodsShop.vo.NoticeVo;
import com.spring.goodsShop.vo.SubcategoryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminDao {
    void setMainCategory(@Param("title") String title);

    List getMainCategory();

    void setSubCategory(@Param("subTitle") String subTitle, @Param("main_id") String main_id);

    List<SubcategoryVo> getSubCategory();

    void deleteMaincategory(@Param("hiddenMaincategory") String hiddenMaincategory);

    void updateMaincategory(@Param("title") String title, @Param("id") int id);

    int checkMaincategory(@Param("title") String title);

    void deleteSubcatergory(@Param("hiddenSubcategory") String hiddenSubcategory);

    int befUpdateSubcategory(@Param("before_title") String before_title, @Param("id") int id);

    void UpdateSubcategory(@Param("sub_update_title") String sub_update_title, @Param("id") int id);

    List<SubcategoryVo> getSubCategory2(@Param("mainCategoryId") int mainCategoryId);

    void insertNotice(@Param("title") String title, @Param("content") String content, @Param("mid") String mid);

    List<NoticeVo> getNoticeAll();

    void deleteNotice(@Param("noticeId") int noticeId);

    NoticeVo getNoticeByNoticeId(@Param("noticeId") int noticeId);

    void updateNoticeCount(@Param("noticeId") int noticeId);

    List<NoticeVo> getNoticeAllPagination(@Param("startIndexNum") int startIndexNum, @Param("onePageCount") int onePageCount);

    int getNoticeTotalCount();
}
