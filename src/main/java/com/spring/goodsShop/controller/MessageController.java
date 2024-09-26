package com.spring.goodsShop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MessageController {

    @RequestMapping(value = "/message/{msgFlag}", method = RequestMethod.GET)
    public String listGet(@PathVariable String msgFlag, Model model){
        if(msgFlag.equals("Ok")){
            model.addAttribute("msg", "test성공");
            model.addAttribute("url", "/item/test");
        }
        else if(msgFlag.equals("joinOk")){
            model.addAttribute("msg", "회원가입되었습니다.");
            model.addAttribute("url", "/");
        }
        else if(msgFlag.equals("loginOk")){
            model.addAttribute("msg", "로그인되었습니다.");
            model.addAttribute("url", "/");
        }
        else if(msgFlag.equals("loginNo")){
            model.addAttribute("msg", "로그인 실패!");
            model.addAttribute("url", "/member/login");
        }
        else if(msgFlag.equals("logout")){
            model.addAttribute("msg", "로그아웃 했습니다.");
            model.addAttribute("url", "/");
        }

        return "include/message";
    }

}
