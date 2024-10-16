package com.spring.goodsShop.controller;

import com.spring.goodsShop.service.AdminService;
import com.spring.goodsShop.service.MemberService;
import com.spring.goodsShop.service.ProductService;
import com.spring.goodsShop.vo.MaincategoryVo;
import com.spring.goodsShop.vo.ProductVo;
import com.spring.goodsShop.vo.Product_imgVo;
import com.spring.goodsShop.vo.SubcategoryVo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    MemberService memberService;

    @Autowired
    AdminService adminService;

    @Autowired
    ProductService productService;

    @RequestMapping(value = "insertProduct", method = RequestMethod.POST)
    String product(@ModelAttribute Product_imgVo imgVo, HttpSession session,
                   @RequestParam(value = "subcategory", required = false, defaultValue = "0") int subcategory,
                   @RequestParam(value = "brand", required = false, defaultValue = "") String brand,
                   @RequestParam(value = "product_name", required = false, defaultValue = "") String product_name,
                   @RequestParam(value = "product_img_detail", required = false, defaultValue = "") MultipartFile product_img_detail,
                   @RequestParam(value = "price", required = false, defaultValue = "0") int price,
                   @RequestParam(value = "stock", required = false, defaultValue = "0") int stock,
                   @RequestParam(value = "designData", required = false, defaultValue = "") String designData,
                   @RequestParam("product_img") List<MultipartFile> files
                   ) throws IOException {
        if(subcategory == 0 || brand == "" || product_name == "" || product_img_detail.isEmpty() || price == 0 || stock == 0 || designData == ""){
            System.out.println("데이터값 받아오는거 에러");
            return "redirect:/message/no_product";
        }
        else {
            String mid = (String) session.getAttribute("sMid");
            int member_id = memberService.getMemberIdBymid(mid);

            // 상품사진들 상품사진들테이블에 저장
            memberService.setProduct_img(imgVo, files);
            // 상품사진테이블 id 가져오기
            String img1 = imgVo.getImg1();
            int product_img_id = memberService.getProductImgIdByImg1(img1);

            // 상품디테일 사진 저장할 경로 가져오기
            String saveFileName = memberService.setProduct_img_detail_saveName(product_img_detail);

            ProductVo productVo = new ProductVo();
            productVo.setSub_category_id(subcategory);
            productVo.setBrand(brand);
            productVo.setProduct_name(product_name);
            productVo.setMember_id(member_id);
            productVo.setProduct_img_id(product_img_id);
            productVo.setProduct_img_detail(saveFileName);
            productVo.setPrice(price);
            productVo.setStock(stock);
            productVo.setDesign(designData);

            memberService.setProduct(productVo, product_img_detail);
            return "redirect:/message/ok_product";
        }
    }

    @RequestMapping(value = "/allProduct", method = RequestMethod.GET)
    String allProduct(Model model){
        List<MaincategoryVo> main_list;
        List<ProductVo> product_list;
        List<Product_imgVo> product_img_list;

        main_list = adminService.getMainCategory();
        product_list = productService.getProduct();
        product_img_list = productService.getProductImg();

        model.addAttribute("main_list", main_list);
        model.addAttribute("product_list", product_list);
        model.addAttribute("product_img_list", product_img_list);
        return "product/allProduct";
    }

    @RequestMapping(value = "/{title}", method = RequestMethod.GET)
    String productTitle(@PathVariable("title") String title, Model model, ProductVo productVo){
        List<SubcategoryVo> sub_list;
        List<ProductVo> product_list = new ArrayList<>();
        List<Product_imgVo> product_img_list;

        int mainCategoryId = productService.getMainCategoryIdByTitle(title);
        sub_list = adminService.getSubCategory(mainCategoryId);

        for (int i = 0; i < sub_list.size(); i++) {
            int id = sub_list.get(i).getId();
            List<ProductVo> temList = productService.getProductBySubcategoyId(id);
            product_list.addAll(temList);
        }

        product_img_list = productService.getProductImg();

        model.addAttribute("title", title);
        model.addAttribute("sub_list", sub_list);
        model.addAttribute("product_list", product_list);
        model.addAttribute("product_img_list", product_img_list);
        return "product/productPageMain";
    }

    @RequestMapping(value = "/{title}/{id}", method = RequestMethod.GET)
    String productTitleId(@PathVariable("title") String title, @PathVariable("id") int id){
        return "product/productPageSub";
    }


}
