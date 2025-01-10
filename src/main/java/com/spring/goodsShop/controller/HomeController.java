package com.spring.goodsShop.controller;

import com.spring.goodsShop.etc.NavbarHelper;
import com.spring.goodsShop.etc.PageProcess;
import com.spring.goodsShop.service.AdminService;
import com.spring.goodsShop.service.ProductService;
import com.spring.goodsShop.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    AdminService adminService;

    @Autowired
    NavbarHelper navbarHelper;

    @Autowired
    ProductService productService;

    @Autowired
    PageProcess pageProcess;

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

    @RequestMapping(value = "search", method = RequestMethod.GET)
    String search(Model model, String search,
                  @RequestParam(name="pageNum", defaultValue = "1", required=false) int pageNum,
                  @RequestParam(name="onePageCount", defaultValue = "20", required=false) int onePageCount
                  ){
        // 페이징처리
        List<ProductVo> empty = new ArrayList<>();
        String part = "searchProduct";
        PageVo pageVo = pageProcess.pageProcess(part, pageNum, onePageCount, empty, search);
        List<SearchVo> searchProductList = productService.getSearchResultPagination(search, pageVo.getStartIndexNum(), pageVo.getOnePageCount());
        model.addAttribute("searchProductList", searchProductList);
        model.addAttribute("pageVo", pageVo);
        model.addAttribute("search", search);

        int searchProductListCount = productService.getSearchResultCount(search);
        model.addAttribute("searchProductListCount", "총 "+searchProductListCount + "개의 상품이 존재합니다.");
        model.addAttribute("searchProductListExist", searchProductListCount);

        navbarHelper.navbarSetup(model);
        return "/search";
    }
}
