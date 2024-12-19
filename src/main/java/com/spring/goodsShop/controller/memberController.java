package com.spring.goodsShop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.goodsShop.enums.OrderStatus;
import com.spring.goodsShop.etc.NavbarHelper;
import com.spring.goodsShop.service.AdminService;
import com.spring.goodsShop.service.ProductService;
import com.spring.goodsShop.vo.*;
import com.spring.goodsShop.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    MemberService memberService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    AdminService adminService;

    @Autowired
    ProductService productService;

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
    String memberP(Model model, HttpSession session) throws JsonProcessingException {
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

        // section - productOrder
        List<Object> productOrderList = new ArrayList<>();

        int memberId = memberService.getMemberIdBymid(mid); // 상품등록한 유저 번호.
        List<Integer> productIds = memberService.getProductIdsByMemberId(memberId);

        for (int i = 0; i < productIds.size(); i++) {
            int productId = Integer.parseInt(productIds.get(i).toString());
            List<OrderVo> orderVosTem = memberService.getOrderVoByProductId(productId);

            if(orderVosTem.isEmpty()) continue;

            for (int j = 0; j < orderVosTem.size(); j++) {
                int member_id = orderVosTem.get(j).getMember_id();
                String orderMemberName = productService.getOrderMemberNameByMemberId(member_id);

                OrderVo vo = new OrderVo();
                vo.setProductName(orderVosTem.get(j).getProductName());
                vo.setDesign(orderVosTem.get(j).getDesign());
                vo.setAmount(orderVosTem.get(j).getAmount());
                vo.setOrderMemberName(orderMemberName);
                vo.setAddress(orderVosTem.get(j).getAddress());
                vo.setPrice(orderVosTem.get(j).getPrice());
                vo.setStatus(orderVosTem.get(j).getStatus());

                vo.setId(orderVosTem.get(j).getId());
                vo.setOrderStatus(orderVosTem.get(j).getOrderStatus());

                productOrderList.add(vo);
            }
        }
        model.addAttribute("productOrderList", productOrderList);

        // section - orders
        // int memberId = memberService.getMemberIdBymid(mid); -> 이미 위에 int memberId 만든게 존재함.(section-productOrder)
        List<OrderVo> sectionOrdersList = memberService.getOrderVoByMemberId(memberId);

        model.addAttribute("sectionOrdersList", sectionOrdersList);

        // section - wishList
        ObjectMapper objectMapper = new ObjectMapper();
        String productIdsJson = memberService.getWishListProductIds(memberId);
        // productIdsJson가 비어있을경우 에러 피하기
        if(productIdsJson == null || productIdsJson.isEmpty()){
            productIdsJson = "[]";
        }

        List<Integer> wishListProductIds = objectMapper.readValue(productIdsJson, new TypeReference<>() {});

        List<ProductVo> wishListProduct =  new ArrayList<>();
        for (int i = 0; i < wishListProductIds.size(); i++) {
            // 상품의 사진, 이름, 가격정보를 가져와야함.
            // 이번에는 sql문의 join을 이용해서 한번 짜보자.
            int wishListProductId = wishListProductIds.get(i);
            List<ProductVo> wishListProductTem = memberService.getProductForWishList(wishListProductId);
            wishListProduct.addAll(wishListProductTem);
        }

        model.addAttribute("wishListProduct", wishListProduct);

        //section - review
        List<ProductVo> orderProductList = productService.getOrderProduct(memberId);
        model.addAttribute("orderProductList", orderProductList);

        return "member/memberP";
    }

    @ResponseBody
    @RequestMapping(value = "/getSubCategories", method = RequestMethod.POST) // POST 요청으로 대분류 ID를 받습니다.
    List<SubcategoryVo> getSubCategories(@RequestBody Map<String, Integer> requestBody) {
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

    @RequestMapping(value = "/pwdSet", method = RequestMethod.POST)
    String pwdSet(String pwd, HttpSession session){
        String mid = (String) session.getAttribute("sMid");
        String PASSWORD_REGEX = "^[a-z\\d!@#$%^&*]{8,}$";

        // 정규식 패턴 컴파일
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(pwd);

        if(matcher.matches()){
            // 유효한 비밀번호인 경우 처리
            String encodePwd = bCryptPasswordEncoder.encode(pwd);
            memberService.pwdSet(encodePwd, mid);
            return "redirect:/message/ok_pwdSet";
        }
        else return "redirect:/message/no_pwdSet";

    }

    @RequestMapping(value = "/completeOrder", method = RequestMethod.POST)
    ResponseEntity<Map<String, Object>> completeOrder(@RequestBody Map<String, Object> item){
        Map<String, Object> data = new HashMap<>();
        int productOrderId = !item.get("productOrderId").toString().isEmpty() ? Integer.parseInt(item.get("productOrderId").toString()) : 0;
        int productId = !item.get("productId").toString().isEmpty() ? Integer.parseInt(item.get("productId").toString()) : 0;
        int amount = !item.get("amount").toString().isEmpty() ? Integer.parseInt(item.get("amount").toString()) : 0;

        if(productOrderId == 0 ||amount == 0 || productId == 0){
            data.put("success", false);
            return ResponseEntity.badRequest().body(data);
        }
        String orderStatus = OrderStatus.DELIVERED.getOrderStatusStr();
        memberService.updateOrderStatusByProductOrderId(productOrderId, orderStatus);

        // 상품배송완료시 product테이블의 sales_count컬럼 업그레이드
        int salesCount = productService.getProductSalesCount(productId);
        int addSalesCount = salesCount + amount;
        productService.updateAddSalesCount(productId, addSalesCount);

        data.put("success", true);

        return ResponseEntity.ok(data);
    }

    @RequestMapping(value = "/refund", method = RequestMethod.POST)
    String refund(int productOrderId, String refundTextarea, HttpSession session){
        Object midObj = session.getAttribute("sMid");
        String mid;
        if (midObj == null) {
            return "redirect:/message/memberX";
        }
        else {
            mid = midObj.toString();
        }

        if(productOrderId == 0 || refundTextarea.isEmpty()){
            return "redirect:/message/refundX";
        }

        int memberId = memberService.getMemberIdBymid(mid);
        int productId = memberService.getProductIdByProductOrderId(productOrderId);

        String orderStatus = memberService.getOrderStatusByProductOrderId(productOrderId);

        if(orderStatus == "배송중"){
            try {
                memberService.setRefundMessage(memberId, refundTextarea, productId);

                String refundProcessingOrderStatusStr = OrderStatus.REFUND_PROCESSING.getOrderStatusStr();
                memberService.updateOrderStatusSwitchRefund(refundProcessingOrderStatusStr, productOrderId);

                int refundMessageId = memberService.getRefundMessageIdByMemberIdAndProductId(memberId, productId);
                memberService.setProductOrderRefundMsgId(refundMessageId);

                return "redirect:/message/refundOk";
            }
            catch (Exception e) {
                throw new RuntimeException("refundMessage테이블 저장중 오류");
            }
        }
        else{
            return "redirect:/message/refundStatusErr";
        }
    }

    @RequestMapping(value = "/refundRefuse", method = RequestMethod.POST)
    String refundRefuse(int productOrderId, String refundRefuseTextarea, HttpSession session){
        Object midObj = session.getAttribute("sMid");
        String mid;
        if (midObj == null) {
            return "redirect:/message/memberX";
        }
        else {
            mid = midObj.toString();
        }

        if(productOrderId == 0 || refundRefuseTextarea.isEmpty()){
            return "redirect:/message/refundRefuseX";
        }

        int memberId = memberService.getMemberIdBymid(mid);
        int productId = memberService.getProductIdByProductOrderId(productOrderId);

        String orderStatus = memberService.getOrderStatusByProductOrderId(productOrderId);

        if(orderStatus.equals("환불처리중")){
            try {
                int refundMessageId = memberService.getRefundMessageIdByProductOrderId(productOrderId);

                memberService.setRefundRefuseMessage(refundRefuseTextarea, refundMessageId);

                String refundRefuseOrderStatusStr = OrderStatus.Refund_Refuse.getOrderStatusStr();
                memberService.updateOrderStatusSwitchRefundRefuse(refundRefuseOrderStatusStr, productOrderId);

                return "redirect:/message/refundRefuseOk";
            }
            catch (Exception e) {
                throw new RuntimeException("refundRefuseMessage테이블 저장중 오류");
            }
        }
        else{
            return "redirect:/message/refundRefuseStatusErr";
        }
    }

    @RequestMapping(value = "/refundReason", method = RequestMethod.POST)
    ResponseEntity<Map<String, Object>> refundReason(@RequestBody Map<String, Object> item){
        Map<String, Object> data = new HashMap<>();
        int productOrderId = !item.get("productOrderId").toString().isEmpty() ? Integer.parseInt(item.get("productOrderId").toString()) : 0;

        if(productOrderId == 0){
            data.put("success", false);
            return ResponseEntity.badRequest().body(data);
        }

        int refundMessageId = memberService.getRefundMessageIdByProductOrderId(productOrderId);
        String refundReason = memberService.getRefundReasonByrefundMessageId(refundMessageId);
        data.put("content", refundReason);
        data.put("success", true);

        return ResponseEntity.ok(data);
    }

    @RequestMapping(value = "/refundRefuseReason", method = RequestMethod.POST)
    ResponseEntity<Map<String, Object>> refundRefuseReason(@RequestBody Map<String, Object> item){
        Map<String, Object> data = new HashMap<>();
        int productOrderId = !item.get("productOrderId").toString().isEmpty() ? Integer.parseInt(item.get("productOrderId").toString()) : 0;

        if(productOrderId == 0){
            data.put("success", false);
            return ResponseEntity.badRequest().body(data);
        }

        int refundMessageId = memberService.getRefundMessageIdByProductOrderId(productOrderId);
        String refundRefuseReason = memberService.getRefundRefuseMessageByRefundMessageId(refundMessageId);
        data.put("content", refundRefuseReason);
        data.put("success", true);

        return ResponseEntity.ok(data);
    }

    @RequestMapping(value = "/sendProduct", method = RequestMethod.POST)
    ResponseEntity<Map<String, Object>> sendProduct(@RequestBody Map<String, Object> item){
        Map<String, Object> data = new HashMap<>();
        int productOrderId = !item.get("productOrderId").toString().isEmpty() ? Integer.parseInt(item.get("productOrderId").toString()) : 0;

        if(productOrderId == 0){
            data.put("success", false);
            return ResponseEntity.badRequest().body(data);
        }

        String orderStatus= OrderStatus.SHIPPING.getOrderStatusStr();

        try {
            memberService.updateOrderStatusByProductOrderId(productOrderId, orderStatus);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        data.put("success", true);
        return ResponseEntity.ok(data);
    }

    @RequestMapping(value = "/addWishList", method = RequestMethod.POST)
    ResponseEntity<Map<String, Object>> addWishList(@RequestBody Map<String, Object> item, HttpSession session) throws JsonProcessingException {
        Map<String, Object> data = new HashMap<>();
        int productId = !item.get("productId").toString().isEmpty() ? Integer.parseInt(item.get("productId").toString()) : 0;

        if(productId == 0){
            throw new RuntimeException("위시리스트 등록 시 productId 넘기기 실패");
        }

        String mid = (String) session.getAttribute("sMid");
        if (mid == null) {
            data.put("loginOk", false);
        }
        else{
            int memberId = memberService.getMemberIdBymid(mid);

            boolean check = memberService.checkWishListExist(memberId);

            if(check){
                // 존재하지않으므로 해당 내용 테이블에 생성... check == true
                ObjectMapper objectMapper = new ObjectMapper();

                // 단일 int 데이터를 리스트로 변환
                String productIdJson = objectMapper.writeValueAsString(Collections.singletonList(productId));
                memberService.insertWishList(memberId, productIdJson);
            }
            else {
                // 존재하므로 기존 테이블에 productId가 존재하는지 확인 후 있으면 거부, 없으면 넣어서 저장.
                ObjectMapper objectMapper = new ObjectMapper();
                String productIdsJson = memberService.getWishListProductIds(memberId);
                List<Integer> productIds = objectMapper.readValue(productIdsJson, new TypeReference<>() {});

                if(!productIds.contains(productId)){
                    productIds.add(productId);
                    String productIdJson = objectMapper.writeValueAsString(productIds);
                    memberService.updateWishList(memberId, productIdJson);
                    data.put("success", true);
                }
                else{
                    // 이미존재하는 상품번호이다.
                    data.put("success", false);
                }
            }
        }
        data.put("loginOk", true);
        return ResponseEntity.ok(data);
    }

    @RequestMapping(value = "/deleteWishList", method = RequestMethod.POST)
    ResponseEntity<Map<String, Object>> deleteWishList(@RequestBody Map<String, Integer> item, HttpSession session) throws JsonProcessingException {
        Map<String, Object> data = new HashMap<>();
//        int productId = !item.get("productId").toString().isEmpty() ? Integer.parseInt(item.get("productId").toString()) : 0;
        int productId = !item.get("productId").toString().isEmpty() ? item.get("productId") : 0;
        if(productId == 0){
            throw new RuntimeException("위시리스트 삭제 시 productId 넘기기 실패");
        }

        String mid = (String) session.getAttribute("sMid");
        if (mid == null) {
            System.out.println("로그인세션없음");
            data.put("loginOk", false);
        }
        else {
            // 맴버아이디를 이용해서 위시리스트 접근후 삭제.
            int memberId = memberService.getMemberIdBymid(mid);

            ObjectMapper objectMapper = new ObjectMapper();
            // db에서 데이터 꺼내온후에 json나눠서 따로 만든다음에 들어온 번호와 일치하는거 제거후에 다시 포장해서 db에 저장
            String productIdsJson = memberService.getWishListProductIds(memberId);
            List<Integer> productIds = objectMapper.readValue(productIdsJson, new TypeReference<>() {});

            if(productIds.contains(productId)){
                productIds.remove(Integer.valueOf(productId));
                String productIdJson = objectMapper.writeValueAsString(productIds);
                memberService.updateWishList(memberId, productIdJson);

                data.put("success", true);
                data.put("loginOk", true);
            }
            else{
                // 위시리스트에서 상품제거 실패
                data.put("success", false);
                System.out.println("위시리스트상품제거실패");
            }
        }
        return ResponseEntity.ok(data);
    }
}
