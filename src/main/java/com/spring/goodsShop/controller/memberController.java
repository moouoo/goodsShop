package com.spring.goodsShop.controller;

import com.spring.goodsShop.service.AdminService;
import com.spring.goodsShop.vo.MaincategoryVo;
import com.spring.goodsShop.vo.MemberVo;
import com.spring.goodsShop.service.MemberService;
import com.spring.goodsShop.vo.SubcategoryVo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/member")
public class memberController {

    @Autowired
    MemberService memberService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    AdminService adminService;

    @RequestMapping(value = "/join", method = RequestMethod.GET)
    String memberJoinGet(){
        return "member/join";
    }

    @RequestMapping(value = "/join", method = RequestMethod.POST)
    String memberJoinPost(@ModelAttribute MemberVo vo){
        String password = vo.getPwd();
        String pwd = bCryptPasswordEncoder.encode(password);
        vo.setPwd(pwd);
        memberService.memberJoin(vo);
        return "redirect:/message/joinOk";
    }

    @ResponseBody
    @RequestMapping(value = "/midCheck", method = RequestMethod.POST)
    String midCheckPost(@RequestBody Map<String, String> jMid){
        String mid = (String) jMid.get("mid");
        boolean check = memberService.midCheck(mid);

        return "{\"available\": " + check + "}";
    }

    @ResponseBody
    @RequestMapping(value = "/emailCheck", method = RequestMethod.POST)
    String emailCheckPost(@RequestBody Map<String, String> jEmail, HttpSession session){
        String email = (String) jEmail.get("email");

        boolean check = memberService.emailCheck(email);
        int number = memberService.sendMail(email);
        String num = Integer.toString(number);
        String code = bCryptPasswordEncoder.encode(num);
        session.setAttribute("code", code);

        return "{\"available\": " + check + "}";
    }

    @ResponseBody
    @RequestMapping(value = "/emailOk", method = RequestMethod.POST)
    String emailCodeCheck(@RequestBody Map<String, String> jEmailOk, HttpSession session){
        String emailOk = (String) jEmailOk.get("emailOk");
        String code = (String) session.getAttribute("code");
        boolean check = bCryptPasswordEncoder.matches(emailOk, code);


        return "{\"available\": " + check + "}";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    String loginGet(){
        return "member/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    String loginPost(String mid, String pwd, HttpSession session, MemberVo vo){
        vo = memberService.loginCheck(mid);

        if(vo != null && bCryptPasswordEncoder.matches(pwd, vo.getPwd())){
            session.setAttribute("sMid", mid);
            session.setAttribute("sEmail", vo.getEmail());
            session.setAttribute("sLevel", vo.getLevel());

            return "redirect:/message/loginOk";
        }
        else {
            return "redirect:/message/loginNo";
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    String logout(HttpSession session){
        session.invalidate();
        return "redirect:/message/logout";
    }

    @RequestMapping(value = "/memberFind", method = RequestMethod.GET)
    String findIdPwdGet(){
        return "member/memberFind";
    }

    @ResponseBody
    @RequestMapping(value = "/findIdPwd", method = RequestMethod.POST)
    String findIdPwd(@RequestBody Map<String, String> findInPwd, Model model){
        String jmid = findInPwd.get("mid");
        String email = findInPwd.get("email");

        if(jmid == null && !email.isEmpty()){
            boolean check = memberService.emailMidCheck(email);

            if(check) {
                String mid = memberService.findMid(email);
                return "{\"success\": " + check + ", \"member\": \"" + mid + "\"}";
            }
            else  return "{\"success\": " + check + "}";

        }
        else if(!jmid.isEmpty() && !email.isEmpty()) {
            boolean check = memberService.emailMidCheck(jmid, email);

            if(check){
                int number = memberService.sendMail(email);
                String num = Integer.toString(number);
                String pwd = bCryptPasswordEncoder.encode(num);
                memberService.setPwd(pwd, jmid);
            }
            return "{\"success\": " + check + "}";
        }
        return "{\"message\": \" OMG...!... \"}";
    }

    @RequestMapping(value = "/memberP", method = RequestMethod.GET)
    String memberP(Model model, HttpSession session){
        String mid = (String) session.getAttribute("sMid");
        String email = (String) session.getAttribute("sEmail");
        int level = (int) session.getAttribute("sLevel");

        String level_name = "";
        if(level == 1) {
            level_name = "일반회원";
        }
        else if(level == 2) {
            level_name = "판매회원";
        }

        MemberVo member = (MemberVo) memberService.getMember(mid, email);
        String account_num = member.getAccount_num();

        if(account_num == null){
            model.addAttribute("mid", mid);
            model.addAttribute("email", email);
            model.addAttribute("account_num", "");
        }
        else {
            model.addAttribute("mid", mid);
            model.addAttribute("email", email);
            model.addAttribute("level", level);
            model.addAttribute("level_name", level_name);
            model.addAttribute("account_num", account_num);
        }

        List<MaincategoryVo> vos_maincategory = new ArrayList<>();
        vos_maincategory = adminService.getMainCategory();

        model.addAttribute("vos_mainCategory", vos_maincategory);

        return "member/memberP";
    }

    @ResponseBody
    @RequestMapping(value = "/getSubCategories") // POST 요청으로 대분류 ID를 받습니다.
    public List<SubcategoryVo> getSubCategories(@RequestBody Map<String, Integer> requestBody) {
        int mainCategoryId = requestBody.get("mainCategoryId");
        List<SubcategoryVo> subCategory = adminService.getSubCategory(mainCategoryId);
        return subCategory;
    }

    @RequestMapping(value = "/editAccount_num", method = RequestMethod.POST)
    String editAccount_num(String account_num, HttpSession session){
        String mid = (String) session.getAttribute("sMid");
        String ACCOUNT_REGEX = "^\\d{2,3}-\\d{2,4}-\\d{2,4}-\\d{2,6}$";

        int level = (int) session.getAttribute("sLevel");

        // 정규식 패턴 컴파일
        Pattern pattern = Pattern.compile(ACCOUNT_REGEX);
        Matcher matcher = pattern.matcher(account_num);

        // 정규식 검증
        if (matcher.matches()) {
            // 유효한 계좌번호일 경우 처리
            memberService.setAccount_num(account_num, mid);
            level = 2;
            memberService.updateLevel(level);
            session.setAttribute("sLevel", level);
            return "redirect:/message/ok_account_num";
        } else {
            // 유효하지 않은 경우 에러 메시지
            return "redirect:/message/no_account_num";
        }
    }

}
