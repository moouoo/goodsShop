package com.spring.goodsShop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.goodsShop.enums.OrderStatus;
import com.spring.goodsShop.etc.NavbarHelper;
import com.spring.goodsShop.etc.NumFormat;
import com.spring.goodsShop.service.AdminService;
import com.spring.goodsShop.service.MemberService;
import com.spring.goodsShop.service.ProductService;
import com.spring.goodsShop.vo.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    MemberService memberService;

    @Autowired
    AdminService adminService;

    @Autowired
    ProductService productService;

    @Autowired
    NavbarHelper navbarHelper;

    @Autowired
    NumFormat numFormat;

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

        // 내비바 설정
        navbarHelper.navbarSetup(model);
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

        navbarHelper.navbarSetup(model);
        return "product/productPageMain";
    }

    @RequestMapping(value = "/{title}/{id}", method = RequestMethod.GET)
    String productTitleId(@PathVariable("title") String title, @PathVariable("id") int id, Model model){
        List<SubcategoryVo> sub_list;
        List<ProductVo> product_list;
        List<Product_imgVo> product_img_list;

        String sub_title = productService.getSubcategoryTitle(id);
        int mainCategoryId = productService.getMainCategoryIdByTitle(title);
        sub_list = adminService.getSubCategory(mainCategoryId);
        product_img_list = productService.getProductImg();
        product_list = productService.getProductBySubcategoyId(id);

        model.addAttribute("sub_list", sub_list);
        model.addAttribute("sub_title", sub_title);
        model.addAttribute("title", title);
        model.addAttribute("product_list", product_list);
        model.addAttribute("product_img_list", product_img_list);

        navbarHelper.navbarSetup(model);
        return "product/productPageSub";
    }

    @RequestMapping(value = "/{product_name}/{id}/{productId}", method = RequestMethod.GET)
    String productDetail(@PathVariable("id") int sub_id, @PathVariable("productId") int productId, @PathVariable("product_name") String product_name, Model model) throws JsonProcessingException {
        List<ProductVo> product_list;
        List<Product_imgVo> product_img_list;

        product_list = productService.getProductByProductId(productId);
        int productImgId = productService.getProductImgIdByProductName(product_name);
        product_img_list = productService.getProductImgByProductImgId(productImgId);

        // 디자인처리
        ObjectMapper objectMapper = new ObjectMapper();
        String designJson = productService.getproductDesignByProductId(productId);
        List<String> designList = objectMapper.readValue(designJson, List.class);

        model.addAttribute("product_list", product_list);
        model.addAttribute("product_img_list", product_img_list);
        model.addAttribute("title", product_name);
        model.addAttribute("designList", designList);
        model.addAttribute("productId", productId);

        navbarHelper.navbarSetup(model);
        return "product/productDetail";
    }

    @RequestMapping(value = "/befOrder", method = RequestMethod.GET)
    String befOrder(HttpSession session, int amount, int productId, String design, Model model){
        String mid = (String) session.getAttribute("sMid");

        Integer sLevelObj = (Integer) session.getAttribute("sLevel"); // null 안전하게 처리 가능
        int sLevel = (sLevelObj != null) ? sLevelObj : 0;
        String level = "level" + sLevel;

        if(mid == null || mid.isEmpty() || sLevel == 0 ){
            return "redirect:/message/memberX";
        }
        else if(amount == 0 || productId == 0 || design == null || design.isEmpty()){
            return "redirect:/message/err";
        }
        else{
            int discount_point = productService.getMemberDiscountPointByMid(mid);
            String discount_point_format = numFormat.nemberFormat(discount_point);

            ProductVo product = productService.getProductOneByProductId(productId);
            String product_name = product.getProduct_name();
            String product_brand = product.getBrand();

            int product_subCategory = product.getSub_category_id();
            int product_mainCategory = productService.getMainCategoryIdBySubCategoryId(product_subCategory);

            int product_price = product.getPrice();
            int price = product_price * amount;

            int delivery_fee;
            if(price < 50000) delivery_fee = 2500;
            else delivery_fee = 0;

            String delivery_fee_String = numFormat.nemberFormat(delivery_fee);

            String formatPrice = product.getFormatOneProductPrice(amount);

            int product_img_id = product.getProduct_img_id();
            List<Product_imgVo> product_img_all = productService.getProductImgByProductImgId(product_img_id);
            String product_img_leader = product_img_all.get(0).getImg1();

            String email = memberService.findEmailByMid(mid);
            String data[] = email.split("@");
            String email1 = data[0];
            String email2 = data[1];

            List<CouponVo> couponList = productService.getCouponVos(level, product_mainCategory, product_subCategory);
            int couponListCount = couponList.size();

            model.addAttribute("email1", email1);
            model.addAttribute("email2", email2);
            model.addAttribute("amount", amount);
            model.addAttribute("design", design);
            model.addAttribute("product_img_leader", product_img_leader);
            model.addAttribute("product_name", product_name);
            model.addAttribute("product_brand", product_brand);
            model.addAttribute("formatPrice", formatPrice);
            model.addAttribute("delivery_fee", delivery_fee);
            model.addAttribute("delivery_fee_String", delivery_fee_String);
            model.addAttribute("couponList", couponList);
            model.addAttribute("price", price);
            model.addAttribute("couponListCount", couponListCount);
            model.addAttribute("discount_point", discount_point);
            model.addAttribute("discount_point_format", discount_point_format);
            model.addAttribute("productId", productId);

            navbarHelper.navbarSetup(model);

            return "product/orderOne";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/couponSelect", method = RequestMethod.POST)
    Map<String, Object> couponSelect(@RequestBody Map<String, String> jCouponId){
        int couponId = Integer.parseInt(jCouponId.get("couponId").toString());
        int price = Integer.parseInt(jCouponId.get("price").toString());

        if(couponId == 0 || price == 0) {
            Map<String, Object> data = new HashMap<>();
            data.put("success", false);
            return data;
        }
        else{
            boolean check = productService.getCouponCheck(couponId);
            if(check){
                BigDecimal coupon_rate = productService.getCouponRate(couponId);

                // price를 BigDecimal로 변환
                BigDecimal priceBigDecimal = BigDecimal.valueOf(price);

                // price * coupon_rate를 계산
                BigDecimal discountPriceBigDecimal = priceBigDecimal.multiply(coupon_rate);

                // 소수점 이하 반올림 후 정수형으로 변환
                int discountPriceInt = discountPriceBigDecimal.intValue();

                // int를 String으로 변환
                String discountPrice = String.valueOf(discountPriceInt);

                Map<String, Object> data = new HashMap<>();
                data.put("success", true);
                data.put("discountedPrice", discountPrice);
                return data;
            }
            else{
                Map<String, Object> data = new HashMap<>();
                data.put("success", false);
                return data;
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    Map<String, Object> payment(@RequestBody Map<String, Object> JPaymentInfo, HttpSession session){

        // 주문번호 생성
        String uuid = UUID.randomUUID().toString().replace("-", "");
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDay = today.format(formatter).replace("-", "");
        String orderCode = formattedDay + "-" + uuid;
        JPaymentInfo.put("orderCode", orderCode);

        // 아임포트 impUid
        String impUid = "imp47844026";
        JPaymentInfo.put("impUid", impUid);

        Map<String, Object> data = new HashMap<>();
        data.put("paymentInfo", JPaymentInfo);
        data.put("success", true);

        return data;
    }

    @ResponseBody
    @RequestMapping(value = "/saveOrderPaymentOne", method = RequestMethod.POST)
    void saveOrderPaymentOne(@RequestBody Map<String, Object> JPaymentInfo, HttpSession session){

        // order테이블에 값 저장
        String mid = session.getAttribute("sMid").toString();
        int product_id = Integer.parseInt(JPaymentInfo.get("productId").toString());
        int amount = Integer.parseInt(JPaymentInfo.get("amount").toString());
        String address = JPaymentInfo.get("address").toString();
        int  member_id = memberService.getMemberIdBymid(mid);
        String status = OrderStatus.PAID.getDisplayName();

        OrderVo order = new OrderVo();
        order.setMember_id(member_id);
        order.setProduct_id(product_id);
        order.setAmount(amount);
        order.setAddress(address);
        order.setStatus(status);

        productService.setOrderOne(order);

        // patment테이블에 값 저장
        LocalDateTime today = LocalDateTime.now();
        String formattedDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        int finalPrice = Integer.parseInt(JPaymentInfo.get("finalPrice").toString());

        PaymentVo payment = new PaymentVo();
        payment.setOrder_id(order.getId());
        payment.setPayMethod("카드");
        payment.setPayment_date(formattedDate);
        payment.setFinalPrice(finalPrice);

        productService.setPaymentOne(payment);
    }

    @RequestMapping(value = "/paymentOk", method = RequestMethod.GET)
    String paymentOk(Model model){
        navbarHelper.navbarSetup(model);
        return "product/paymentOk";
    }

    @ResponseBody
    @RequestMapping(value = "/addToCart", method = RequestMethod.POST)
    Map<String, Object> addToCart(@RequestBody Map<String, Object> JCart, HttpSession session){

        Map<String, Object> data = new HashMap<>();

        String mid = session.getAttribute("sMid").toString();
        if(mid == null) {
            data.put("noLogin", true);
        }
        else data.put("noLogin", false);

        try {
            int productId = Integer.parseInt(JCart.get("productId").toString());
            String design = JCart.get("design").toString();
            int amount = Integer.parseInt(JCart.get("amount").toString());
            int price = Integer.parseInt(JCart.get("price").toString());

            // 세션에서 장바구니 가져오기 (없으면 새로운 리스트 생성)
            List<CartVo> cart = (List<CartVo>) session.getAttribute("sCart");
            if (cart == null) {
                cart = new ArrayList<>();
            }

            // 장바구니에 상품 추가 (중복 방지)
            boolean voExists = false;
            for (int i = 0; i < cart.size(); i++) {
                CartVo vo = cart.get(i);
                if (vo.getProductId() == productId && vo.getDesign().equals(design)) {
                    voExists = true;
                    // 이미 존재하면 수량만 업데이트 (amount)
                    vo.setAmount(amount);
                    break;
                }
            }

            // 상품이 존재하지 않으면 새로 추가
            if (!voExists) {
                cart.add(new CartVo(productId, design, amount, price));
            }

            // 업데이트된 장바구니를 세션에 저장
            session.setAttribute("sCart", cart);

            data.put("success", true);
        }
        catch (Exception e) {
            data.put("success", false);
            throw new RuntimeException(e);
        }
        return data;
    }

    @RequestMapping(value = "/cart", method = RequestMethod.GET)
    String cart(Model model, HttpSession session){
        List<CartListVo> cartList = new ArrayList<>();
        List<CartVo> cart = (List<CartVo>) session.getAttribute("sCart");

        if(cart == null){
            return "redirect:/message/memberX";
        }

        for (int i = 0; i < cart.size(); i++) {
            CartVo item = cart.get(i);
            int productId = item.getProductId();
            List<ProductVo> product_list_tem = productService.getProductByProductId(productId);
            CartListVo cartListVo = new CartListVo();

            String product_name = product_list_tem.get(0).getProduct_name();
            String design = cart.get(i).getDesign();
            int price =  cart.get(i).getPrice();
            int amount = cart.get(i).getAmount();
            int productImgId = product_list_tem.get(0).getProduct_img_id();
            String img = productService.getProductImg1ByProductImgId(productImgId);

            cartListVo.setProductId(productId);
            cartListVo.setProduct_name(product_name);
            cartListVo.setDesign(design);
            cartListVo.setPrice(price);
            cartListVo.setAmount(amount);
            cartListVo.setImg(img);

            cartList.add(cartListVo);
        }

        model.addAttribute("cartList", cartList);

        navbarHelper.navbarSetup(model);
        return "product/cart";
    }

    // ResponseEntity 사용해보기
    @RequestMapping(value = "/deleteCartItem", method = RequestMethod.POST)
    ResponseEntity<Map<String, Object>> deleteCartItem(@RequestBody Map<String, Object> item, HttpSession session){
        int productId = !item.get("productId").toString().isEmpty() ? Integer.parseInt(item.get("productId").toString()) : 0;
        String design = item.get("design").toString();

        Map<String, Object> data = new HashMap<>();

        if(productId == 0 || design.isEmpty()){
            data.put("success", false);
            return ResponseEntity.badRequest().body(data);  // 400 Bad Request 응답을 반환
        }

        List<CartVo> cart = (List<CartVo>) session.getAttribute("sCart");
        if (cart != null) {
            // cart에서 일치하는 항목 찾기
            // -> 람다식 이라고 한다.
            cart.removeIf(cartItem -> cartItem.getProductId() == productId && cartItem.getDesign().equals(design));

            // cart 목록이 변경된 후 세션에 다시 저장
            session.setAttribute("sCart", cart);
        }

        data.put("success", true);
        return ResponseEntity.ok(data);  // 200 OK 응답을 반환
    }

    // 체크박스를 이용한 선택상품구매 기능 구현, 전체상품구매 기능 구현
    // 상품구매 버튼 누르시 주문페이지로 이동.

}
