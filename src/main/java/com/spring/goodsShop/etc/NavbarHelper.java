package com.spring.goodsShop.etc;

import com.spring.goodsShop.service.AdminService;
import com.spring.goodsShop.vo.MaincategoryVo;
import com.spring.goodsShop.vo.SubcategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.List;

@Component
public class NavbarHelper {
    @Autowired
    AdminService adminService;

    public void navbarSetup(Model model){
        // nav - 상품명 설정
        List<MaincategoryVo> all_main_list;
        List<SubcategoryVo> all_sub_list;

        all_main_list = adminService.getMainCategory();
        all_sub_list = adminService.getSubCategory();

        model.addAttribute("all_main_list", all_main_list);
        model.addAttribute("all_sub_list", all_sub_list);
    }
}
