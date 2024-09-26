package com.spring.goodsShop.controller;

import com.spring.goodsShop.vo.MemberVo;
import com.spring.goodsShop.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/member")
public class memberController {

    @Autowired
    MemberService memberService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping(value = "/join", method = RequestMethod.GET)
    String memberJoinGet(){
        return "join";
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
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    String loginPost(String mid, String pwd, HttpSession session, MemberVo vo){
        vo = memberService.loginCheck(mid);

        if(vo != null && bCryptPasswordEncoder.matches(pwd, vo.getPwd())){
            session.setAttribute("sMid", mid);
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
        return "memberFind";
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

}
