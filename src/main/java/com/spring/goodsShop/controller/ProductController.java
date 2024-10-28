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
        String email = JPaymentInfo.get("email").toString();
        String phone = JPaymentInfo.get("phone").toString();
        String name = JPaymentInfo.get("name").toString();
        String address = JPaymentInfo.get("address").toString();
        int postcode = Integer.parseInt(JPaymentInfo.get("postcode").toString());
        int finalPrice = Integer.parseInt(JPaymentInfo.get("finalPrice").toString());
        String product_name = JPaymentInfo.get("product_name").toString();
        int price = Integer.parseInt(JPaymentInfo.get("price").toString());
        int amount = Integer.parseInt(JPaymentInfo.get("amount").toString());
        int product_id = Integer.parseInt(JPaymentInfo.get("productId").toString());
        String design = JPaymentInfo.get("design").toString();

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
        int price = Integer.parseInt(JPaymentInfo.get("price").toString());
        int discountPrice = price * amount - finalPrice;

        PaymentVo payment = new PaymentVo();
        payment.setOrder_id(order.getId());
        payment.setPayMethod("카드");
        payment.setPayment_date(formattedDate);
        payment.setDiscountPrice(discountPrice);
        payment.setFinalPrice(finalPrice);

        productService.setPaymentOne(payment);

        // 현재 생각해봐야하는것
        // 현재 단일상품 구매 및 카드결재니깐 이런식으로 코드를 짠거지 다량구매, 다른결재방식을 이용하게 된다면 코드가 달라져야한다.
        // 다량 구매는 장바구니 페이지를 만들어서 따로 구현한다고 해도 결재방법에 따라서 저장하는 방식은 만들어야한다.
        // 웹페이지에 스위치를 ㅅ추가하여 카드, 실시간계좌이체, 가상계좌, 휴대폰소액결제를 선택할 수 있게한다.
        // 아임포트에 데이터를 넘셔서 볼 수 있어야함. -> 현재 결제성공해야 데이터베이스 로직 들어가는것까지성공, 디비에 저장시 500에러가 뜨거 있는 상태
    }

    @RequestMapping(value = "/saveImport", method = RequestMethod.POST)
    ResponseEntity<String> saveImport(){
        // 아임포트 API 키와 시크릿 입력 + impUuid
        String impUid = "imp47844026";
        String apiKey = "4604456016044856";
        String apiSecret = "o0LfQTCdvfNpEGlv9w4sFrB8f2mkfZq8KCAHt07uzWJqz4M9DTaP8E1L8bbg64jxGpuyBf5z1jKMFdmS";

        // 토큰 발급 요청
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> authParams = Map.of("imp_key", apiKey, "imp_secret", apiSecret);
        HttpEntity<Map<String, String>> authEntity = new HttpEntity<>(authParams, headers);

        ResponseEntity<Map> authResponse = restTemplate.postForEntity(
                "https://api.iamport.kr/users/getToken", authEntity, Map.class);
        if (authResponse.getStatusCode() == HttpStatus.OK) {
            String token = (String) ((Map) authResponse.getBody().get("response")).get("access_token");

            // 토큰을 이용한 결제 내역 조회
            headers.set("Authorization", token);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<String> paymentResponse = restTemplate.exchange(
                    "https://api.iamport.kr/payments/" + impUid,
                    HttpMethod.GET,
                    entity,
                    String.class
            );
            return paymentResponse;
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰 발급 실패");

    }

}
