package com.spring.goodsShop.controller;

import com.spring.goodsShop.service.AdminService;
import com.spring.goodsShop.vo.MaincategoryVo;
import com.spring.goodsShop.vo.SubcategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    AdminService adminService;

    @GetMapping("/")
    String hello(Model model){
        List<MaincategoryVo> all_main_list;
        List<SubcategoryVo> all_sub_list;

        all_main_list = adminService.getMainCategory();
        all_sub_list = adminService.getSubCategory();

        model.addAttribute("all_main_list", all_main_list);
        model.addAttribute("all_sub_list", all_sub_list);

        return "index";
    }
}
