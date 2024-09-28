package com.spring.goodsShop.controller;

import com.spring.goodsShop.service.AdminService;
import com.spring.goodsShop.vo.MaincategoryVo;
import com.spring.goodsShop.vo.SubcategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService adminService;

    @RequestMapping(value = "/adminP", method = RequestMethod.GET)
    String admin(){
        return "admin/adminP";
    }

    @RequestMapping(value = "/categoryAdd", method = RequestMethod.GET)
    String categoryM(Model model, MaincategoryVo maincategoryVo){
        List<MaincategoryVo> vos;
        vos = adminService.getMainCategory();
        model.addAttribute("mainCategory", vos);
        return "admin/categoryAdd";
    }

    @RequestMapping(value = "/mainCategoryAdd", method = RequestMethod.POST)
    String mainCategoryAdd(@RequestParam(value = "main_title") String title){
        if(title == "" || title == null){
            return "redirect:/message/mainCategoryAddNo";
        }
        else {
            adminService.setMainCategory(title);
            return "redirect:/message/mainCategoryAddOk";
        }
    }
    @RequestMapping(value = "/subCategoryAdd", method = RequestMethod.POST)
    String subCategoryAdd(
            @RequestParam(value = "sub-category") String sub_title,
            @RequestParam(value = "main-category-select") String main_id
                          ){
        if(sub_title == "" || sub_title == null || main_id == "" || main_id == null){
            return "redirect:/message/mainCategoryAddNo";
        }
        else {
            adminService.setSubCategory(sub_title, main_id);
            return "redirect:/message/subCategoryAddOk";
        }
    }

    @RequestMapping(value = "/categoryDeleteUpdate", method = RequestMethod.GET)
    String memberM(Model model, SubcategoryVo subcategoryVo, MaincategoryVo maincategoryVo){
        List<MaincategoryVo> vos;
        List<SubcategoryVo> vos2;
        vos = adminService.getMainCategory();
        vos2 = adminService.getSubCategory();
        model.addAttribute("mainCategory", vos);
        model.addAttribute("subCategory", vos2);
        return "admin/categoryDeleteUpdate";
    }

    @Transactional
    @RequestMapping(value = "/modalDelKeyCheck", method = RequestMethod.POST)
    String modalDelKeyCheck(
            @RequestParam(value = "delKey") String delKey,
            @RequestParam(value = "hiddenMaincategory") String hiddenMaincategory
                            ){
        if(delKey == "" || delKey == null || hiddenMaincategory == "" || hiddenMaincategory == null){
            return "redirect:/message/modalDelKeyCheckErr";
        }
        else if(!delKey.equals("삭제")){
            return "redirect:/message/modalDelKeyCheckNo";
        }
        else {
            try {
                adminService.deleteMaincategory(hiddenMaincategory);
                return "redirect:/message/modalDelKeyCheckOk";
            } catch (DataAccessException e) {
                // 외래 키 제약 조건으로 인한 오류 처리
                return "redirect:/message/databaseDeleteErr";
            }
        }
    }


    @RequestMapping(value = "/inquiryM", method = RequestMethod.GET)
    String inquiryM(){
        return "admin/inquiryM";
    }
}
