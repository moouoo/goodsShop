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
        else if(msgFlag.equals("mainCategoryAddNo")){
            model.addAttribute("msg", "생성 실패! 개발자에게 문의해주세요.");
            model.addAttribute("url", "/admin/adminP");
        }
        else if(msgFlag.equals("mainCategoryAddOk")){
            model.addAttribute("msg", "대분류 생성 성공!");
            model.addAttribute("url", "/admin/categoryAdd");
        }
        else if(msgFlag.equals("subCategoryAddOk")){
            model.addAttribute("msg", "소분류 생성 성공!");
            model.addAttribute("url", "/admin/categoryAdd");
        }
        else if(msgFlag.equals("modalDelKeyCheckOk")){
            model.addAttribute("msg", "삭제되었습니다.");
            model.addAttribute("url", "/admin/categoryAdd");
        }
        else if(msgFlag.equals("modalDelKeyCheckErr")){
            model.addAttribute("msg", "삭제하는 도중 데이터 값이 안넘어왔습니다. 개발자에게 문의해주세요.");
            model.addAttribute("url", "/admin/categoryAdd");
        }
        else if(msgFlag.equals("modalDelKeyCheckNo")){
            model.addAttribute("msg", "삭제키가 다릅니다. 다시 확인해주세요.");
            model.addAttribute("url", "/admin/categoryAdd");
        }
        else if(msgFlag.equals("databaseDeleteErr")){
            model.addAttribute("msg", "삭제중, 데이터베이스처리에 오류가 생겼습니다. \n혹시 대분류 안에 남아있는 소분류가 있는 경우, 소분류를 전부 삭제하시고 다시 시도하십시오.");
            model.addAttribute("url", "/admin/categoryAdd");
        }

        return "include/message";
    }

}