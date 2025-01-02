package com.spring.goodsShop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping(value = "/community")
@Controller
public class CommunityController {

    @RequestMapping(value = "/notice", method = RequestMethod.GET)
    String notice(){
        return "/community/notice";
    }
}
