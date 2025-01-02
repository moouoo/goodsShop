package com.spring.goodsShop.controller;

import com.spring.goodsShop.service.AdminService;
import com.spring.goodsShop.service.MemberService;
import com.spring.goodsShop.vo.MaincategoryVo;
import com.spring.goodsShop.vo.MemberVo;
import com.spring.goodsShop.vo.SubcategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService adminService;

    @Autowired
    MemberService memberService;

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

    @ResponseBody
    @RequestMapping(value = "/maincategoryUpdate", method = RequestMethod.POST)
    String maincategoryUpdate(@RequestBody Map<String, String> JupdateMaincategory
            ) {
        String title = (String) JupdateMaincategory.get("mainUpdate");
        String S_id = (String) JupdateMaincategory.get("hiddenMaincategory");


        if(title == "" || title == null || S_id == "" || S_id == null){
            throw new RuntimeException("Invalid input: title or S_id cannot be empty or null");
        }
        else {
            boolean check = adminService.checkMaincategory(title);

            if(check) {
                int id = Integer.parseInt(S_id);
                adminService.updateMaincategory(title, id);
                return "{\"success\": true }";
            }
            else {
                return "{\"success\": false }";
            }
        }
    }

    @RequestMapping(value = "/modalDelKeyCheck_sub", method = RequestMethod.POST)
    String modalDelKeyCheck_sub(
            @RequestParam(value = "delKey_sub") String delKey,
            @RequestParam(value = "hiddenSubcategory") String hiddenSubcategory
        ) {
        if(delKey == "" || delKey == null || hiddenSubcategory == "" || hiddenSubcategory == null){
            return "redirect:/message/modalDelKeyCheckErr";
        }
        else if(!delKey.equals("삭제")){
            return "redirect:/message/modalDelKeyCheckNo";
        }
        else {
            adminService.deleteSubcatergory(hiddenSubcategory);
            return "redirect:/message/ok";
        }
    }

    @RequestMapping(value = "/subcategoryUpdate", method = RequestMethod.POST)
    String subcategoryUpdate(
            @RequestParam(value = "sub_update_title") String sub_update_title,
            @RequestParam(value = "before_title") String before_title,
            @RequestParam(value = "title_id") String S_title_id
        ){

        if(sub_update_title == "" || sub_update_title == null || before_title == "" || before_title == null || S_title_id == "" || S_title_id == null){
            return "redirect:/message/no";
        }
        else {
            int id = Integer.parseInt(S_title_id);
            boolean check = adminService.befUpdateSubcategory(before_title, id);

            if(check) {
                adminService.UpdateSubcategory(sub_update_title, id);
                return "redirect:/message/ok";
            }
            else return "redirect:/message/noUpdate";
        }

    }

    @RequestMapping(value = "/memberM", method = RequestMethod.GET)
    String memberM(MemberVo vo, Model model){
        List<MemberVo> vos = memberService.getMember();
        model.addAttribute("vos", vos);

        return "admin/memberM";
    }

    @ResponseBody
    @RequestMapping(value = "/memberDelete", method = RequestMethod.POST)
    String memberDelete(@RequestBody Map<String, String> JmemberDel){
        String mid = (String) JmemberDel.get("mid");
        String email = (String) JmemberDel.get("email");

        memberService.memberDelete(mid, email);

        return "{\"success\": true }";
    }

    @RequestMapping(value = "/inquiryM", method = RequestMethod.GET)
    String inquiryM(){
        return "admin/inquiryM";
    }

    @RequestMapping(value = "/noticeM", method = RequestMethod.GET)
    String notice(){
        return "admin/noticeM";
    }

    @RequestMapping(value = "/noticeWrite", method = RequestMethod.GET)
    String noticeWrite(){
        return "admin/noticeWrite";
    }

    @RequestMapping(value = "/noticeWritePost", method = RequestMethod.POST)
    String noticeWritePost(String title, String content){
        if(content == null || content.isEmpty() || title == null || title.isEmpty()) return "redirect:/message/noticeWritePostX";
        if(content.length() > 500 || title.length() > 30) return "redirect:/message/longStr";

        adminService.insertNotice(title, content);
        // 저장하는 코드 완성시켜야함.
        return "redirect:/message/noticeWriteOk";
    }
}
