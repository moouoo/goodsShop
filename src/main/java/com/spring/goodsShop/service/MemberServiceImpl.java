package com.spring.goodsShop.service;

import com.spring.goodsShop.etc.ImgHandler;
import com.spring.goodsShop.vo.*;
import com.spring.goodsShop.dao.MemberDao;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class MemberServiceImpl implements MemberService{

    @Autowired
    MemberDao memberDao;

    @Autowired
    JavaMailSender emailCheckPost;

    @Override
    public boolean midCheck(String mid) {
        boolean check;
        int count =  memberDao.midCheck(mid);

        if(count == 1) check = false;
        else check = true;

        return check;
    }

    @Override
    public boolean emailCheck(String email) {
        boolean check;
        int count =  memberDao.emailCheck(email);

        if(count == 1) check = false;
        else check = true;

        return check;
    }

    // 이메일 인증번호 보내기
    @Override
    public int sendMail(String email) {
        int number = (int)(Math.random() * (90000000)) + 10000000;// (int) Math.random() * (최댓값-최소값+1) + 최소값
        final String senderEmail= "hoon134615@gmail.com";

        MimeMessage message = emailCheckPost.createMimeMessage();
        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("goodsShop에서 보낸 이메일입니다.");
            String body = "";
            body += "<h3>" + "요청하신 번호입니다." + "</h3>";
            body += "<h1>" + number + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body,"UTF-8", "html");
        }
        catch (MessagingException e) {
            e.printStackTrace();
        }
        emailCheckPost.send(message);

        return number;
    }

    @Override
    public void memberJoin(MemberVo vo) {
        memberDao.memberJoin(vo);
    }

    @Override
    public MemberVo loginCheck(String mid) {
        return memberDao.loginCheck(mid);
    }

    @Override
    public String findMid(String email) {
        return memberDao.findMid(email);
    }

    @Override
    public boolean emailMidCheck(String mid, String email) {
        boolean check;
        int count =  memberDao.emailPwdCheck(mid, email);

        if(count == 1) check = true;
        else check = false;

        return check;
    }

    @Override
    public boolean emailMidCheck(String email) {
        boolean check;
        int count =  memberDao.emailCheck(email);

        if(count == 1) check = true;
        else check = false;

        return check;
    }

    @Override
    public void setPwd(String pwd, String mid) {
        memberDao.setPwd(pwd, mid);
    }

    @Override
    public List<MemberVo> getMember() {
        return memberDao.getMember();
    }

    @Override
    public void memberDelete(String mid, String email) {
        memberDao.memberDelete(mid, email);
    }

    @Override
    public MemberVo getMember(String mid, String email) {
        return (MemberVo) memberDao.getMember2(mid, email);
    }

    @Override
    public List<SubcategoryVo> getSubCategoriesByMainCategory(String mainCategory) {
        return memberDao.getSubCategoriesByMainCategory(mainCategory);
    }

    @Override
    public void setAccount_num(String account_num, String mid) {
        memberDao.setAccount_num(account_num, mid);
    }

    @Override
    public void updateLevel(int level, String mid) {
        memberDao.updateLevel(level, mid);
    }

    @Override
    public int getMemberIdBymid(String mid) {
        return memberDao.getMemberIdBymid(mid);
    }

    @Override
    public void setProduct_img(Product_imgVo imgVo, List<MultipartFile> files) throws IOException {
        ImgHandler imgHandler = new ImgHandler();
        String urlPath = "product_img";

        for (int i = 0; i < files.size(); i++) {
            MultipartFile fName = files.get(i);
            if(fName.isEmpty()) break;

            UUID uid = UUID.randomUUID();
            String oFileName = fName.getOriginalFilename();
            String saveFileName = uid + "-" + oFileName;
            imgHandler.writeFile(fName, saveFileName, urlPath);

            // VO의 img 필드에 파일 경로 설정
            switch (i) {
                case 0: imgVo.setImg1(saveFileName); break;
                case 1: imgVo.setImg2(saveFileName); break;
                case 2: imgVo.setImg3(saveFileName); break;
                case 3: imgVo.setImg4(saveFileName); break;
                case 4: imgVo.setImg5(saveFileName); break;
                default: break; // 최대 5개까지만 처리
            }
        }
        //db저장
        memberDao.setProduct_img(imgVo);
    }

    @Override
    public int getProductImgIdByImg1(String img1) {
        return memberDao.getProductImgIdByImg1(img1);
    }

    @Override
    public String setProduct_img_detail_saveName(MultipartFile product_img_detail) throws IOException {

        if((!product_img_detail.isEmpty())){
            UUID uid = UUID.randomUUID();
            String oFileName = product_img_detail.getOriginalFilename();
            String saveFileName = uid + "-" + oFileName;
            return saveFileName;
        }
        else return "redirect:/message/no_product_detail";
    }

    @Override
    public void setProduct(ProductVo productVo, MultipartFile product_img_detail) throws IOException {
        String urlPath = "product_detail";
        String saveFileName = productVo.getProduct_img_detail();
        ImgHandler imgHandler = new ImgHandler();
        imgHandler.writeFile(product_img_detail, saveFileName, urlPath);

        memberDao.setProduct(productVo);
    }

    @Override
    public void pwdSet(String encodePwd, String mid) {
        memberDao.pwdSet(encodePwd, mid);
    }

    @Override
    public String findEmailByMid(String mid) {
        return memberDao.findEmailByMid(mid);
    }

    @Override
    public List<Integer> getProductIdsByMemberId(int memberId) {
        return memberDao.getProductIdsByMemberId(memberId);
    }

    @Override
    public List<OrderVo> getOrderVoByProductId(int productId) {
        return memberDao.getOrderVoByProductId(productId);
    }

    @Override
    public List<OrderVo> getOrderVoByMemberId(int memberId) {
        return memberDao.getOrderVoByMemberId(memberId);
    }

    @Override
    public void updateOrderStatusByProductOrderId(int productOrderId, String orderStatus) {
        memberDao.updateOrderStatusByProductOrderId(productOrderId, orderStatus);
    }

    @Override
    public int getProductIdByProductOrderId(int productOrderId) {
        return memberDao.getProductIdByProductOrderId(productOrderId);
    }

    @Override
    public void setRefundMessage(int memberId, String refundTextarea, int productId) {
        memberDao.setRefundMessage(memberId, refundTextarea, productId);
    }

    @Override
    public void updateOrderStatusSwitchRefund(String refundProcessingOrderStatusStr, int productOrderId) {
        memberDao.updateOrderStatusSwitchRefund(refundProcessingOrderStatusStr, productOrderId);
    }

    @Override
    public String getOrderStatusByProductOrderId(int productOrderId) {
        return memberDao.getOrderStatusByProductOrderId(productOrderId);
    }

    @Override
    public int getRefundMessageIdByMemberIdAndProductId(int memberId, int productId) {
        return memberDao.getRefundMessageIdByMemberIdAndProductId(memberId, productId);
    }

    @Override
    public void setProductOrderRefundMsgId(int refundMessageId) {
        memberDao.setProductOrderRefundMsgId(refundMessageId);
    }

    @Override
    public void setRefundRefuseMessage(String refundRefuseTextarea, int refundMessageId) {
        memberDao.setRefundRefuseMessage(refundRefuseTextarea, refundMessageId);
    }

    @Override
    public int getRefundMessageIdByProductOrderId(int productOrderId) {
        return memberDao.getRefundMessageIdByProductOrderId(productOrderId);
    }

    @Override
    public void updateOrderStatusSwitchRefundRefuse(String refundRefuseOrderStatusStr, int productOrderId) {
        memberDao.updateOrderStatusSwitchRefundRefuse(refundRefuseOrderStatusStr, productOrderId);
    }

    @Override
    public String getRefundReasonByrefundMessageId(int refundMessageId) {
        return memberDao.getRefundReasonByrefundMessageId(refundMessageId);
    }

    @Override
    public String getRefundRefuseMessageByRefundMessageId(int refundMessageId) {
        return memberDao.getRefundRefuseMessageByRefundMessageId(refundMessageId);
    }

    @Override
    public boolean checkWishListExist(int memberId) {
        boolean check;
        int count =  memberDao.checkWishListExist(memberId);

        if(count == 1) check = false;
        else check = true;

        return check;
    }

    @Override
    public String getWishListProductIds(int memberId) {
        return memberDao.getWishListProductIds(memberId);
    }

    @Override
    public void insertWishList(int memberId, String productIdJson) {
        memberDao.insertWishList(memberId, productIdJson);
    }

    @Override
    public void updateWishList(int memberId, String productIdJson) {
        memberDao.updateWishList(memberId, productIdJson);
    }

    @Override
    public List<ProductVo> getProductForWishList(int wishListProductId) {
        return memberDao.getProductForWishList(wishListProductId);
    }

    @Override
    public void insertReview(String reviewText, int reviewProductOrderId, int starRatingValue) {
        memberDao.insertReviewNoImg(reviewText, reviewProductOrderId, starRatingValue);
    }

    @Override
    public void insertReview(String reviewText, int reviewProductOrderId, int starRatingValue, String reviewFile) {
        memberDao.insertReviewYesImg(reviewText, reviewProductOrderId, starRatingValue, reviewFile);
    }

    @Override
    public void updateMemberDiscountPoint50(int memberId) {
        memberDao.updateMemberDiscountPoint50(memberId);
    }

    @Override
    public void updateMemberDiscountPoint100(int memberId) {
        memberDao.updateMemberDiscountPoint100(memberId);
    }

    @Override
    public String getReviewByProductOrderId(int reviewProductOrderId) {
        return memberDao.getReviewByProductOrderId(reviewProductOrderId);
    }

    @Override
    public int getMemberIdByOrderId(int reviewProductOrderId) {
        return memberDao.getMemberIdByOrderId(reviewProductOrderId);
    }

    @Override
    public List<ReviewVo> getReviewList(int memberId) {
        return memberDao.getReviewList(memberId);
    }

    @Override
    public int getReviewId(int reviewProductOrderId) {
        return memberDao.getReviewId(reviewProductOrderId);
    }

    @Override
    public void updateReviewReplyAndReplyContent(int reviewId, String reviewReplyText) {
        memberDao.updateReviewReplyAndReplyContent(reviewId, reviewReplyText);
    }

    @Override
    public String getReplyContentByProductOrderId(int reviewProductOrderId) {
        return memberDao.getReplyContentByProductOrderId(reviewProductOrderId);
    }

    @Override
    public String getProductQContent(int productQId) {
        return memberDao.getProductQContent(productQId);
    }

    @Override
    public List<ProductQVo> getProductQ(int memberId) {
        return memberDao.getProductQ(memberId);
    }

    @Override
    public void updateQnaReplyContent(int productQId, String qnaReplyContent) {
        memberDao.updateQnaReplyContent(productQId, qnaReplyContent);
    }

    @Override
    public int checkExistProductQReplyContent(int productQId) {
        return memberDao.checkExistProductQReplyContent(productQId);
    }

    @Override
    public List<ProductQVo> getProductQViewList(int memberId) {
        return memberDao.getProductQViewList(memberId);
    }

    @Override
    public String getProductQReplyContent(int productQId) {
        return memberDao.getProductQReplyContent(productQId);
    }

    @Override
    public StringBuilder madeHtmlReplyContent(String replyContent, int productQId, int qnaViewCount) {
        StringBuilder htmlReplyContent = new StringBuilder();
        htmlReplyContent.append("<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 20px auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px; background-color: #f9f9f9;'>")
                .append("<h3 style='color: #333; border-bottom: 2px solid #007BFF; padding-bottom: 10px; margin-bottom: 20px;'>문의답변</h3>")
                .append("<p style='font-size: 16px; line-height: 1.6; color: #555;'>"+replyContent+"</p>")
                .append("<div style='text-align: right; margin-top: 20px;'>")
                .append("<button onclick='qnaViewClose("+qnaViewCount+")' style='padding: 10px 20px; font-size: 14px; color: #fff; background-color: #007BFF; border: none; border-radius: 4px; cursor: pointer;'>닫기</button>")
                .append("</div>")
                .append("</div>");
        return htmlReplyContent;
    }


}
