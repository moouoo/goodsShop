package com.spring.goodsShop.controller;

import com.spring.goodsShop.enums.OrderStatus;
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
    String memberP(Model model, HttpSession session){
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

                productOrderList.add(vo);
            }
        }
        model.addAttribute("productOrderList", productOrderList);

        // section - orders
        // int memberId = memberService.getMemberIdBymid(mid); -> 이미 위에 int memberId 만든게 존재함.(section-productOrder)
        List<OrderVo> sectionOrdersList = memberService.getOrderVoByMemberId(memberId);

        model.addAttribute("sectionOrdersList", sectionOrdersList);

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

        if(productOrderId == 0){
            data.put("success", false);
            return ResponseEntity.badRequest().body(data);
        }
        String orderStatus = OrderStatus.DELIVERED.getOrderStatusStr();
        memberService.updateOrderStatusByProductOrderId(productOrderId, orderStatus);

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

        try {
            memberService.setRefundMessage(memberId, refundTextarea, productId);
        } catch (Exception e) {
            throw new RuntimeException("refundMessage테이블 저장중 오류");
        }

        // 지금 발생하는 문제가 db에는 저장이 되는데 message를 넘길때 null로 넘겨지고 있음
        // 사실 환불버튼이 배송중 일때 눌러야 환불중으로 바뀌어야하는데 지금은 아무떄나 눌러도 환불중으로 바뀔뿐만 아니라 db에 저장이 됨.
        // if문을 이용해서 enun의 배달중이라는 글짜일때만 디비에 존재할때 다음 처리가 가능하게 제한걸고 이러한 글자가 아니라면 message를 이용해서 튕겨내야할듯
        // 프로트에서도 배달중이 아니라면 누르지못하게 프론트에서도 막아야할듯.
        
        String refundProcessingOrderStatusStr =OrderStatus.REFUND_PROCESSING.getOrderStatusStr();
        memberService.updateOrderStatusSwitchRefund(refundProcessingOrderStatusStr, productOrderId);
        return "redirect:/message/refundOk";
    }
}
