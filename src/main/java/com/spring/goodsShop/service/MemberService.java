package com.spring.goodsShop.service;

import com.spring.goodsShop.vo.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MemberService {

    boolean midCheck(String mid);

    boolean emailCheck(String email);

    int sendMail(String email);

    void memberJoin(MemberVo vo);

    MemberVo loginCheck(String mid);

    String findMid(String email);

    boolean emailMidCheck(String mid, String email);

    boolean emailMidCheck(String email);

    void setPwd(String pwd, String mid);

    List<MemberVo> getMember();

    void memberDelete(String mid, String email);

    MemberVo getMember(String mid, String email);

    List<SubcategoryVo> getSubCategoriesByMainCategory(String mainCategory);

    void setAccount_num(String account_num, String mid);

    void updateLevel(int level, String mid);

    int getMemberIdBymid(String mid);

    void setProduct_img(Product_imgVo imgVo, List<MultipartFile> files) throws IOException;

    int getProductImgIdByImg1(String img1);

    String setProduct_img_detail_saveName(MultipartFile product_img_detail) throws IOException;

    void setProduct(ProductVo productVo, MultipartFile product_img_detail) throws IOException;

    void pwdSet(String encodePwd, String mid);

    String findEmailByMid(String mid);

    List<Integer> getProductIdsByMemberId(int memberId);

    List<OrderVo> getOrderVoByProductId(int productId);

    List<OrderVo> getOrderVoByMemberId(int memberId);

    void updateOrderStatusByProductOrderId(int productOrderId, String orderStatus);

    int getProductIdByProductOrderId(int productOrderId);

    void setRefundMessage(int memberId, String refundTextarea, int productId);

    void updateOrderStatusSwitchRefund(String refundProcessingOrderStatusStr, int productOrderId);

    String getOrderStatusByProductOrderId(int productOrderId);

    int getRefundMessageIdByMemberIdAndProductId(int memberId, int productId);

    void setProductOrderRefundMsgId(int refundMessageId);

    void setRefundRefuseMessage(String refundRefuseTextarea, int refundMessageId);

    int getRefundMessageIdByProductOrderId(int productOrderId);

    void updateOrderStatusSwitchRefundRefuse(String refundRefuseOrderStatusStr, int productOrderId);

    String getRefundReasonByrefundMessageId(int refundMessageId);

    String getRefundRefuseMessageByRefundMessageId(int refundMessageId);

    boolean checkWishListExist(int memberId);

    String getWishListProductIds(int memberId);

    void insertWishList(int memberId, String productIdJson);

    void updateWishList(int memberId, String productIdJson);

    List<ProductVo> getProductForWishList(int wishListProductId);

    void insertReview(String reviewText, int reviewProductOrderId, int starRatingValue);

    void insertReview(String reviewText, int reviewProductOrderId, int starRatingValue, String saveFileName);

    void updateMemberDiscountPoint50(int memberId);

    void updateMemberDiscountPoint100(int memberId);

    String getReviewByProductOrderId(int reviewProductOrderId);

    int getMemberIdByOrderId(int reviewProductOrderId);

    List<ReviewVo> getReviewList(int memberId);

    int getReviewId(int reviewProductOrderId);

    void updateReviewReplyAndReplyContent(int reviewId, String reviewReplyText);

    String getReplyContentByProductOrderId(int reviewProductOrderId);

    String getProductQContent(int productQId);

    List<ProductQVo> getProductQ(int memberId);

    void updateQnaReplyContent(int productQId, String qnaReplyContent);

    int checkExistProductQReplyContent(int productQId);

    List<ProductQVo> getProductQViewList(int memberId);

    String getProductQReplyContent(int productQId);

    StringBuilder madeHtmlReplyContent(String replyContent, int productQId, int qnaViewCount);
}
