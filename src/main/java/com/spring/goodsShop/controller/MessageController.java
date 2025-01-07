package com.spring.goodsShop.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MessageController {

    @RequestMapping(value = "/message/{msgFlag}", method = RequestMethod.GET)
    public String listGet(@PathVariable String msgFlag, Model model,
                          @RequestParam(required = false) String product_name,
                          @RequestParam(required = false) Integer id,
                          @RequestParam(required = false) Integer productId
                          ){

        if(msgFlag.equals("err")){
            model.addAttribute("msg", "err");
            model.addAttribute("url", "/");
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
        else if(msgFlag.equals("no")){
            model.addAttribute("msg", "에러");
            model.addAttribute("url", "/admin/categoryAdd");
        }
        else if(msgFlag.equals("ok")){
            model.addAttribute("msg", "성공");
            model.addAttribute("url", "/admin/categoryAdd");
        }
        else if(msgFlag.equals("noUpdate")){
            model.addAttribute("msg", "이미 존재하는 소분류입니다.");
            model.addAttribute("url", "/admin/categoryAdd");
        }
        else if(msgFlag.equals("ok_account_num")){
            model.addAttribute("msg", "계좌번호를 성공적으로 수정하였습니다.");
            model.addAttribute("url", "/member/memberP");
        }
        else if(msgFlag.equals("no_account_num")){
            model.addAttribute("msg", "계좌번호를 형식에 맞게 잘 입력해주세요. 하이폰은 필수입니다.");
            model.addAttribute("url", "/member/memberP");
        }
        else if(msgFlag.equals("ok_product")){
            model.addAttribute("msg", "상품이 등록되었습니다.");
            model.addAttribute("url", "/member/memberP");
        }
        else if(msgFlag.equals("no_product_detail")){
            model.addAttribute("msg", "상품설명사진저장중 에러");
            model.addAttribute("url", "/member/memberP");
        }
        else if(msgFlag.equals("ok_pwdSet")){
            model.addAttribute("msg", "비밀번호를 수정하였습니다.");
            model.addAttribute("url", "/member/memberP");
        }
        else if(msgFlag.equals("no_pwdSet")){
            model.addAttribute("msg", "비밀번호를 수정 에러");
            model.addAttribute("url", "/member/memberP");
        }
        else if(msgFlag.equals("memberX")){
            model.addAttribute("msg", "로그인후 주문가능합니다.");
            model.addAttribute("url", "/member/login");
        }
        else if(msgFlag.equals("cartX")){
            model.addAttribute("msg", "장바구니가 비어있습니다.");
            model.addAttribute("url", "/");
        }
        else if(msgFlag.equals("refund")){
            model.addAttribute("msg", "환불처리되었습니다.");
            model.addAttribute("url", "/member/memberP#orders");
        }
        else if(msgFlag.equals("refundX")){
            model.addAttribute("msg", "환불처리중 오류(주문번호 혹은 메모내용 없음.)");
            model.addAttribute("url", "/member/memberP#orders");
        }
        else if(msgFlag.equals("refundOk")){
            model.addAttribute("msg", "환불처리되었습니다.");
            model.addAttribute("url", "/member/memberP#orders");
        }
        else if(msgFlag.equals("refundStatusErr")){
            model.addAttribute("msg", "배송중일때만 가능함. 배송상태 에러");
            model.addAttribute("url", "/member/memberP#orders");
        }
        else if(msgFlag.equals("refundRefuseX")){
            model.addAttribute("msg", "환불거절중 오류(주문번호 혹은 메모 내용 없음.");
            model.addAttribute("url", "/member/memberP#orders");
        }
        else if(msgFlag.equals("refundRefuseOk")){
            model.addAttribute("msg", "환불거절하셨습니다.");
            model.addAttribute("url", "/member/memberP#productOrder");
        }
        else if(msgFlag.equals("refundRefuseStatusErr")){
            model.addAttribute("msg", "배송상태가 환불처리중 일때만 가능한 기능입니다.");
            model.addAttribute("url", "/member/memberP#productOrder");
        }
        else if(msgFlag.equals("reviewX")){
            model.addAttribute("msg", "리뷰할 데이터가 정상적으로 보내지지 않았습니다.");
            model.addAttribute("url", "/member/memberP#review");
        }
        else if(msgFlag.equals("reviewOk")){
            model.addAttribute("msg", "리뷰를 등록하셨습니다.");
            model.addAttribute("url", "/member/memberP#review");
        }
        else if(msgFlag.equals("reviewReplyWriteOk")){
            model.addAttribute("msg", "리뷰에 대한 댓글을 등록하셨습니다.");
            model.addAttribute("url", "/member/memberP#reviewReply");
        }
        else if(msgFlag.equals("reviewReplyWriteNo")){
            model.addAttribute("msg", "리뷰에 대한 댓글을 등록실패.");
            model.addAttribute("url", "/member/memberP#reviewReply");
        }
        else if(msgFlag.equals("noticeWritePostX")){
            model.addAttribute("msg", "알림작성을 위한 데이터 가져오기 실패");
            model.addAttribute("url", "/admin/adminP");
        }
        else if(msgFlag.equals("longStr")){
            model.addAttribute("msg", "가져온 데이터가 너무 길어용");
            model.addAttribute("url", "/admin/noticeWrite");
        }
        else if(msgFlag.equals("noticeWriteOk")){
            model.addAttribute("msg", "알림을 등록했습니다.");
            model.addAttribute("url", "/admin/noticeM");
        }
        else if(msgFlag.equals("goMain")){
            model.addAttribute("msg", "세션이 완료되었습니다.");
            model.addAttribute("url", "/");
        }
        else if(msgFlag.equals("askAboutProductOk")){
            model.addAttribute("msg", "문의사항을 등록했습니다.");
            model.addAttribute("url", "/product" + "/" + product_name + "/" + id +"/" +productId);
        }
        else if(msgFlag.equals("accessErr")){
            model.addAttribute("msg", "잘못된 접근입니다.");
            model.addAttribute("url", "/");
        }
        else if(msgFlag.equals("qnaErr")){
            model.addAttribute("msg", "qna데이터 받아올때 에러 발생");
            model.addAttribute("url", "/member/memberP#qna");
        }
        else if(msgFlag.equals("updateQnaReplyContent")){
            model.addAttribute("msg", "문의에 대한 답변을 완료했습니다.");
            model.addAttribute("url", "/member/memberP#qna");
        }
        else if(msgFlag.equals("existContent")){
            model.addAttribute("msg", "이미 존재하는 답변내용입니다.");
            model.addAttribute("url", "/member/memberP#qna");
        }


        return "include/message";
    }

}
