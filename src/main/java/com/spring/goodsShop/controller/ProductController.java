package com.spring.goodsShop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.goodsShop.enums.PaymentStatus;
import com.spring.goodsShop.etc.NavbarHelper;
import com.spring.goodsShop.etc.NumFormat;
import com.spring.goodsShop.etc.PageProcess;
import com.spring.goodsShop.etc.PaymentHelper;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Autowired
    PageProcess pageProcess;

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
    String allProduct(Model model,
                      @RequestParam(name="pageNum", defaultValue = "1", required=false) int pageNum,
                      @RequestParam(name="onePageCount", defaultValue = "20", required=false) int onePageCount
                      ){
        List<MaincategoryVo> main_list;
        List<ProductVo> product_list;
        List<Product_imgVo> product_img_list;

        main_list = adminService.getMainCategory();
//        product_list = productService.getProduct();
        product_img_list = productService.getProductImg();


        model.addAttribute("main_list", main_list);
//        model.addAttribute("product_list", product_list);
        model.addAttribute("product_img_list", product_img_list);

        List<ProductVo> emptyList = new ArrayList<>();
        String part = "allProduct";
        PageVo pageVo = pageProcess.pageProcess(part, pageNum, onePageCount, emptyList, "");
        product_list = productService.getProductPagination(pageVo.getStartIndexNum(), pageVo.getOnePageCount());

        model.addAttribute("product_list", product_list);
        model.addAttribute("pageVo", pageVo);

        int productCount = productService.getProductAllTotalCount();
        model.addAttribute("productCount", "총 " + productCount + "개의 상품이 존재합니다.");

        // 내비바 설정
        navbarHelper.navbarSetup(model);
        return "product/allProduct";
    }

    @RequestMapping(value = "/{title}", method = RequestMethod.GET)
    String productTitle(@PathVariable("title") String title, Model model,
                        @RequestParam(name="pageNum", defaultValue = "1", required=false) int pageNum,
                        @RequestParam(name="onePageCount", defaultValue = "20", required=false) int onePageCount
                        ){
        List<SubcategoryVo> sub_list;
        List<ProductVo> product_listImsi = new ArrayList<>();
        List<Product_imgVo> product_img_list;

        int mainCategoryId = productService.getMainCategoryIdByTitle(title);
        sub_list = adminService.getSubCategory(mainCategoryId);

        for (int i = 0; i < sub_list.size(); i++) {
            int id = sub_list.get(i).getId();
            List<ProductVo> temList = productService.getProductBySubcategoyId(id);
            product_listImsi.addAll(temList);
        }

        product_img_list = productService.getProductImg();

        // 페이징처리
        String part = "mainCategoryProduct";
        PageVo pageVo = pageProcess.pageProcess(part, pageNum, onePageCount, product_listImsi, "");
        List<ProductVo> product_list = pageProcess.categoryPagination(product_listImsi, onePageCount, pageVo.getStartIndexNum(), pageNum);

        model.addAttribute("title", title);
        model.addAttribute("sub_list", sub_list);
        model.addAttribute("product_list", product_list);
        model.addAttribute("product_img_list", product_img_list);
        model.addAttribute("pageVo", pageVo);
        model.addAttribute("totalCount", "총 " + product_listImsi.size() + "개의 상품이 있습니다.");

        navbarHelper.navbarSetup(model);
        return "product/productPageMain";
    }

    @RequestMapping(value = "/{title}/{id}", method = RequestMethod.GET)
    String productTitleId(@PathVariable("title") String title, @PathVariable("id") int id, Model model,
                          @RequestParam(name="pageNum", defaultValue = "1", required=false) int pageNum,
                          @RequestParam(name="onePageCount", defaultValue = "20", required=false) int onePageCount
                          ){
        List<SubcategoryVo> sub_list;
        List<ProductVo> product_listImsi;
        List<Product_imgVo> product_img_list;

        String sub_title = productService.getSubcategoryTitle(id);
        int mainCategoryId = productService.getMainCategoryIdByTitle(title);
        sub_list = adminService.getSubCategory(mainCategoryId);
        product_img_list = productService.getProductImg();
        product_listImsi = productService.getProductBySubcategoyId(id);

        // 페이징처리
        String part = "subCategoryProduct";
        PageVo pageVo = pageProcess.pageProcess(part, pageNum, onePageCount, product_listImsi, "");
        List<ProductVo> product_list = pageProcess.categoryPagination(product_listImsi, onePageCount, pageVo.getStartIndexNum(), pageNum);

        model.addAttribute("sub_list", sub_list);
        model.addAttribute("sub_title", sub_title);
        model.addAttribute("title", title);
        model.addAttribute("product_list", product_list);
        model.addAttribute("product_img_list", product_img_list);
        model.addAttribute("pageVo", pageVo);
        model.addAttribute("subCategoryId", id);
        model.addAttribute("totalCount", "총 " + product_listImsi.size() + "개의 상품이 있습니다.");


        navbarHelper.navbarSetup(model);
        return "product/productPageSub";
    }

    @RequestMapping(value = "/{product_name}/{id}/{productId}", method = RequestMethod.GET)
    String productDetail(@PathVariable("id") int sub_id, @PathVariable("productId") int productId, @PathVariable("product_name") String product_name, Model model,
                         HttpSession session
                         ) throws JsonProcessingException {
        List<ProductVo> product_list;
        List<Product_imgVo> product_img_list;

        product_list = productService.getProductByProductId(productId);
        int productImgId = productService.getProductImgIdByProductId(productId);
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

        // review
        List<ReviewVo> reviewList = productService.getReview(productId);

        int reviewCount = productService.getReviewCountAll(productId);

        model.addAttribute("reviewList", reviewList);
        model.addAttribute("reviewCount", reviewCount);

        // Q&A
        List<ProductQVo> productQList = productService.getProductQ(productId);

        int productQCount = productService.getProductQCountAll(productId);

        model.addAttribute("productQList", productQList);
        model.addAttribute("productQCount", productQCount);

        navbarHelper.navbarSetup(model);
        return "product/productDetail";
    }

    @ResponseBody
    @RequestMapping(value = "/couponSelect", method = RequestMethod.POST)
    Map<String, Object> couponSelect(@RequestBody Map<String, String> jCouponId){
        int couponId = !jCouponId.get("couponId").toString().isEmpty() ? Integer.parseInt(jCouponId.get("couponId").toString()) : 0;
        int price = !jCouponId.get("price").toString().isEmpty() ? Integer.parseInt(jCouponId.get("price").toString()) : 0;

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
                data.put("price", price);
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
    Map<String, Object> payment(@RequestBody Map<String, Object> JPaymentInfo) {
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> paymentInfo = new HashMap<>();

        List<Object> amountsObj = (List<Object>) JPaymentInfo.get("amount");
        List<Object> productIdsObj = (List<Object>) JPaymentInfo.get("productId");
        List<Map<String, Object>> couponData = (List<Map<String, Object>>) JPaymentInfo.get("couponData");
        List<Object> designsObj = (List<Object>) JPaymentInfo.get("design");

        int finalPrice = !JPaymentInfo.get("finalPrice").toString().isEmpty() ? Integer.parseInt(JPaymentInfo.get("finalPrice").toString()) : 0;
        String email = !JPaymentInfo.get("email").toString().isEmpty() ? JPaymentInfo.get("email").toString() : null;
        String phone = !JPaymentInfo.get("phone").toString().isEmpty() ? JPaymentInfo.get("phone").toString() : null;
        String name = !JPaymentInfo.get("name").toString().isEmpty() ? JPaymentInfo.get("name").toString() : null;
        String address = !JPaymentInfo.get("address").toString().isEmpty() ? JPaymentInfo.get("address").toString() : null;
        int postcode = !JPaymentInfo.get("postcode").toString().isEmpty() ? Integer.parseInt(JPaymentInfo.get("postcode").toString()) : 0;
        int rewardPoint = !JPaymentInfo.get("rewardPoint").toString().isEmpty() ? Integer.parseInt(JPaymentInfo.get("rewardPoint").toString()) : 0;

        int finalDiscountPrice = 0;
        int check = 0;
        List<String> productNames = new ArrayList<>();
        for (int i = 0; i < productIdsObj.size(); i++) {
            int productId = Integer.parseInt(productIdsObj.get(i).toString());
            String design = designsObj.get(i).toString();
            int priceTem = productService.getProductPriceByProductId(productId);
            int price = priceTem * Integer.parseInt(amountsObj.get(i).toString());

            String productNameTem = productService.getProductNameByProductId(productId);
            productNames.add(productNameTem);

            int couponId = 0;
            for (int j = 0; j < couponData.size(); j++) {
                Map<String, Object> coupon = couponData.get(j);
                // couponData에서 productId가 일치하는 couponId 찾기
                if ((int) coupon.get("productId") == productId && coupon.get("design").equals(design)) {
                    Object couponIdObj = coupon.get("couponId");
                    couponId = Integer.parseInt(couponIdObj.toString());
                }
            }

            if (couponId != 0){
                BigDecimal discountRate = productService.getCouponRate(couponId);
                BigDecimal priceBigDecimal = BigDecimal.valueOf(price);
                BigDecimal finalPriceCheckTem = priceBigDecimal.multiply(discountRate);
                int finalPriceCheckInt = finalPriceCheckTem.intValue(); // 할인금액
                check = price - finalPriceCheckInt;
                finalDiscountPrice = finalDiscountPrice + check; // 총할인금액
            }
            else {
                finalDiscountPrice = finalDiscountPrice + price;
            }
        }

        int delivery_fee;
        if(finalPrice >= 50000) delivery_fee = 0;
        else delivery_fee = 2500;

        if(finalPrice != (finalDiscountPrice - rewardPoint + delivery_fee)){
            data.put("success", false);
//            throw new RuntimeException("강제로 발생시킨 오류: 뷰에서 받아온 최종가격과 검증결과의 최종가격이 일치하지 않음.");
            return data;
        }

        PaymentHelper paymentHelper = new PaymentHelper();
        String orderCode = paymentHelper.orderCodeCreate();
        String impUid = paymentHelper.impUidCreate();

        paymentInfo.put("impUid", impUid);
        paymentInfo.put("orderCode", orderCode);
        paymentInfo.put("designsObj", designsObj);
        paymentInfo.put("productNames", productNames);
        paymentInfo.put("finalPrice", finalPrice);
        paymentInfo.put("email", email);
        paymentInfo.put("name", name);
        paymentInfo.put("phone", phone);
        paymentInfo.put("address", address);
        paymentInfo.put("postcode", postcode);
        paymentInfo.put("productIdsObj", productIdsObj);
        paymentInfo.put("amountsObj", amountsObj);
        paymentInfo.put("couponData", couponData);

        data.put("success", true);
        data.put("paymentInfo", paymentInfo);
        return data;
    }

    @ResponseBody
    @RequestMapping(value = "/saveOrderPayment", method = RequestMethod.POST)
    void saveOrderPaymentOne(@RequestBody Map<String, Object> JPaymentInfo, HttpSession session){

        List<Object> amountsObj = (List<Object>) JPaymentInfo.get("amountsObj");
        List<Object> productIdsObj = (List<Object>) JPaymentInfo.get("productIdsObj");
        List<Object> designsObj = (List<Object>) JPaymentInfo.get("designsObj");
        List<Map<String, Object>> couponData = (List<Map<String, Object>>) JPaymentInfo.get("couponData");

        // order테이블에 값 저장
        String mid = session.getAttribute("sMid").toString();
        int  member_id = memberService.getMemberIdBymid(mid);
        String address = JPaymentInfo.get("address").toString();
        String status = PaymentStatus.PAID.getDisplayName();

        OrderVo order = new OrderVo();
        order.setMember_id(member_id);
        order.setAddress(address);
        order.setStatus(status);

        int check = 0;
        for (int i = 0; i < productIdsObj.size(); i++) {
            int productId = Integer.parseInt(productIdsObj.get(i).toString());
            int price = productService.getProductPriceByProductId(productId);
            int amount = Integer.parseInt(amountsObj.get(i).toString());
            productService.updateProductStock(productId, amount);
            String design = designsObj.get(i).toString();
            String productName = productService.getProductNameByProductId(productId);

            order.setProduct_id(productId);
            order.setAmount(amount);
            order.setDesign(design);
            order.setProductName(productName);

            int couponId = 0;
            for (int j = 0; j < couponData.size(); j++) {
                Map<String, Object> coupon = couponData.get(j);
                // couponData에서 productId가 일치하는 couponId 찾기
                if ((int) coupon.get("productId") == productId && coupon.get("design").equals(design)) {
                    Object couponIdObj = coupon.get("couponId");
                    couponId = Integer.parseInt(couponIdObj.toString());
                }
            }

            if (couponId != 0){
                BigDecimal discountRate = productService.getCouponRate(couponId);
                BigDecimal priceBigDecimal = BigDecimal.valueOf(price);
                BigDecimal finalPriceCheckTem = priceBigDecimal.multiply(discountRate);
                int finalPriceCheckInt = finalPriceCheckTem.intValue(); // 할인금액
                check = price - finalPriceCheckInt;
            }
            else {
                check = price;
            }
            order.setPrice(check);

            productService.setOrderOne(order);
        }

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

        Object midObj = session.getAttribute("sMid");

        if (midObj == null) {
            data.put("noLogin", true);
        }
        else {
            String mid = midObj.toString();
            data.put("noLogin", false);
        }

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
            return "redirect:/message/cartX";
        }

        Object midObj = session.getAttribute("sMid");
        if (midObj == null) {
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

    @RequestMapping(value = "/orderPage", method = RequestMethod.GET)
    String orderPage(
            @RequestParam List<Integer> productId, @RequestParam List<String> design,  @RequestParam List<Integer> amount,
            @RequestParam List<Integer> price, @RequestParam List<String> img, Model model, HttpSession session
    ){
        Object midObj = session.getAttribute("sMid");
        String mid;
        if (midObj == null) {
            return "redirect:/message/memberX";
        }
        else {
            mid = midObj.toString();
        }

        if(productId == null || design == null || amount == null || price == null || img == null){
            return "redirect:/message/cartX";
        }

        // 상품 정보 가져오기
        List<ProductVo> productVo = productService.getProductVoOfNameAndBrandByProductId(productId);

        // 상품 ID를 키로 하는 Map으로 변환
        Map<Integer, ProductVo> productMap = productVo.stream().collect(Collectors.toMap(ProductVo::getId, Function.identity()));

        // 데이터를 묶어서 뷰로 전달
        List<Map<String, Object>> purchaseItems = new ArrayList<>();
        for (int i = 0; i < productId.size(); i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("productId", productId.get(i));
            item.put("design", design.get(i));
            item.put("amount", amount.get(i));
            item.put("price", price.get(i));
            item.put("img", img.get(i));

            // 상품 정보를 Map에서 조회
            ProductVo vo = productMap.get(productId.get(i));

            if (vo != null) {
                item.put("productName", vo.getProduct_name());
                item.put("brand", vo.getBrand());
                item.put("subcategoryId", vo.getSub_category_id());
                int maincategoryId = productService.getMainCategoryIdBySubCategoryId(vo.getSub_category_id());
                item.put("maincategoryId", maincategoryId);

            }
            purchaseItems.add(item);
        }

        // 적립금
        int discount_point = productService.getMemberDiscountPointByMid(mid);

        model.addAttribute("item", purchaseItems);
        model.addAttribute("discount_point", discount_point);

        navbarHelper.navbarSetup(model);
        return "product/orderPage";

    }

    @RequestMapping(value = "/selectCouponForItem", method = RequestMethod.POST)
    ResponseEntity<Map<String, Object>> selectCouponForItem(@RequestBody Map<String, Object> item, HttpSession session){
        int subcategoryId = !item.get("subcategoryId").toString().isEmpty() ? Integer.parseInt(item.get("subcategoryId").toString()) : 0;
        int maincategoryId = !item.get("maincategoryId").toString().isEmpty() ? Integer.parseInt(item.get("maincategoryId").toString()) : 0;
        String idTem = !item.get("id").toString().isEmpty() ? item.get("id").toString() : "";
        int productId = !item.get("productId").toString().isEmpty() ? Integer.parseInt(item.get("productId").toString()) : 0;
        String design = !item.get("design").toString().isEmpty() ? item.get("design").toString() : "";
        String amount = item.get("amount") == null ? "" : item.get("amount").toString();

        Map<String, Object> data = new HashMap<>();

        Object sLevelObj = session.getAttribute("sLevel");
        if(sLevelObj == null){
            data.put("login", false);
            return ResponseEntity.badRequest().body(data);
        }
        int sLevel = Integer.parseInt(sLevelObj.toString());
        String level = "level" + sLevel;


        if(subcategoryId == 0 || maincategoryId == 0 || sLevel == 0 || idTem.isEmpty()){
            data.put("success", false);
            data.put("login", true);
            return ResponseEntity.badRequest().body(data);
        }

        int resetPrice = productService.getProductPriceByProductId(productId);

        //itemPrice_0
        //01234567890
        int id = Integer.parseInt(idTem.substring(10));

        List<CouponVo> couponList = productService.getCouponVos(level, maincategoryId, subcategoryId);

        data.put("success", true);
        data.put("login", true);
        data.put("couponList", couponList);
        data.put("id", id);
        data.put("productId", productId);
        data.put("design", design);
        data.put("amount", amount);
        data.put("resetPrice", resetPrice);

        return ResponseEntity.ok(data);
    }

    @RequestMapping(value = "/productNew", method = RequestMethod.GET)
    String productNew(Model model,
                      @RequestParam(name="pageNum", defaultValue = "1", required=false) int pageNum,
                      @RequestParam(name="onePageCount", defaultValue = "20", required=false) int onePageCount
                      ){
        List<ProductVo> product_listLimit10 = productService.getProductDESCLimit10();

        String part = "newProduct";
        List<ProductVo> empty = new ArrayList<>();
        PageVo pageVo = pageProcess.pageProcess(part, pageNum, onePageCount, empty, "");
        List<ProductVo> product_list = productService.getProductDESCPagination(pageVo.getStartIndexNum(), onePageCount);


        model.addAttribute("product_listLimit10", product_listLimit10);
        model.addAttribute("product_list", product_list);
        model.addAttribute("pageVo", pageVo);

        navbarHelper.navbarSetup(model);
        return "product/productNew";
    }

    @RequestMapping(value = "/best100", method = RequestMethod.GET)
    String best100(Model model,
                   @RequestParam(name="pageNum", defaultValue = "1", required=false) int pageNum,
                   @RequestParam(name="onePageCount", defaultValue = "20", required=false) int onePageCount
                   ){
        // 페이징처리
        List<ProductVo> productBest100Imsi = productService.getProductBest100();
        String part = "best100Product";
        PageVo pageVo = pageProcess.pageProcess(part, pageNum, onePageCount, productBest100Imsi, "");
        List<ProductVo> productBest100 = pageProcess.categoryPagination(productBest100Imsi, onePageCount, pageVo.getStartIndexNum(), pageNum);

        model.addAttribute("productBest100", productBest100);
        model.addAttribute("pageVo", pageVo);

        navbarHelper.navbarSetup(model);
        return "product/best100";
    }

    @RequestMapping(value = "/askAboutProduct", method = RequestMethod.POST)
    String askAboutProduct(String askAboutProductContent, String askAboutProductTitle, int productId, HttpSession session, RedirectAttributes redirectAttributes){
        if(askAboutProductTitle.length() > 20 || askAboutProductContent.length() > 100 || productId <= 0){
            return "redirect:/message/accessErr";
        }

        Object ObjMid = session.getAttribute("sMid");
        if(ObjMid == null){
            return "redirect:/message/memberX";
        }
        String mid =  ObjMid.toString();

        int memberId = memberService.getMemberIdBymid(mid);
        productService.insertProductQ(memberId, productId, askAboutProductContent, askAboutProductTitle);

        String product_name = productService.getProductNameByProductId(productId);
        int id = productService.getProductSubCategoryId(productId);

        redirectAttributes.addAttribute("product_name", product_name);
        redirectAttributes.addAttribute("id", id);
        redirectAttributes.addAttribute("productId", productId);
        return "redirect:/message/askAboutProductOk";
    }


}
