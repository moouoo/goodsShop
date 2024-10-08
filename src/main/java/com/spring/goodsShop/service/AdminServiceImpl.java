package com.spring.goodsShop.service;

import com.spring.goodsShop.dao.AdminDao;
import com.spring.goodsShop.vo.SubcategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService{
    @Autowired
    AdminDao adminDao;

    @Override
    public void setMainCategory(String title) {
        adminDao.setMainCategory(title);
    }

    @Override
    public List getMainCategory() {
        return adminDao.getMainCategory();
    }

    @Override
    public void setSubCategory(String subTitle, String main_id) {
        adminDao.setSubCategory(subTitle, main_id);
    }

    @Override
    public List<SubcategoryVo> getSubCategory() {
        return adminDao.getSubCategory();
    }

    @Override
    public void deleteMaincategory(String hiddenMaincategory) {
        adminDao.deleteMaincategory(hiddenMaincategory);
    }

    @Override
    public void updateMaincategory(String title, int id) {
        adminDao.updateMaincategory(title, id);
    }

    @Override
    public boolean checkMaincategory(String title) {
        boolean check;

        int checkCode = adminDao.checkMaincategory(title);
        if(checkCode >= 1) check = false;
        else check = true;

        return check;
    }

    @Override
    public void deleteSubcatergory(String hiddenSubcategory) {
        adminDao.deleteSubcatergory(hiddenSubcategory);
    }

    @Override
    public boolean befUpdateSubcategory(String before_title, int id) {
        boolean check;
        int num = adminDao.befUpdateSubcategory(before_title, id);
        System.out.println(num);
        if(num >= 1) check = true;
        else check = false;
        return check;
    }

    @Override
    public void UpdateSubcategory(String sub_update_title, int id) {
        adminDao.UpdateSubcategory(sub_update_title, id);
    }

    @Override
    public List<SubcategoryVo> getSubCategory(int mainCategoryId) {
        return adminDao.getSubCategory2(mainCategoryId);
    }
}
