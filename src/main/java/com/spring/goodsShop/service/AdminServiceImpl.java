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
}
