package com.spring.goodsShop.dao;

import com.spring.goodsShop.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberDao {
    int midCheck(@Param("mid") String mid);

    int emailCheck(@Param("email") String email);

    void memberJoin(@Param("vo") MemberVo vo);

    MemberVo loginCheck(@Param("mid") String mid);

    String findMid(@Param("email") String email);

    int emailPwdCheck(@Param("mid") String mid, @Param("email") String email);

    int emailCheck2(@Param("email") String email);

    void setPwd(@Param("pwd") String pwd, @Param("mid") String mid);

    List<MemberVo> getMember();

    void memberDelete(@Param("mid") String mid, @Param("email") String email);

    MemberVo getMember2(@Param("mid") String mid, @Param("email") String email);

    List<SubcategoryVo> getSubCategoriesByMainCategory(@Param("email") String mainCategory);

    void setAccount_num(@Param("account_num") String account_num, @Param("mid") String mid);

    void updateLevel(@Param("level") int level);

    int getMemberIdBymid(@Param("mid") String mid);

    void setProduct_img(@Param("imgVo") Product_imgVo imgVo);

    int getProductImgIdByImg1(@Param("img1") String img1);

    void setProduct(@Param("productVo") ProductVo productVo);

    void pwdSet(@Param("encodePwd") String encodePwd, @Param("mid") String mid);

    String findEmailByMid(@Param("mid") String mid);

    List<Integer> getProductIdsByMemberId(@Param("memberId") int memberId);

    List<OrderVo> getOrderVoByProductId(@Param("productId") int productId);

    List<OrderVo> getOrderVoByMemberId(@Param("memberId") int memberId);

    void updateOrderStatusByProductOrderId(@Param("productOrderId") int productOrderId, @Param("orderStatus") String orderStatus);

    int getProductIdByProductOrderId(@Param("productOrderId") int productOrderId);

    void setRefundMessage(@Param("memberId") int memberId, @Param("refundTextarea") String refundTextarea, @Param("productId") int productId);

    void updateOrderStatusSwitchRefund(@Param("refundProcessingOrderStatusStr") String refundProcessingOrderStatusStr, @Param("productOrderId") int productOrderId);

    String getOrderStatusByProductOrderId(@Param("productOrderId") int productOrderId);

    int getRefundMessageIdByMemberIdAndProductId(@Param("memberId") int memberId, @Param("productId") int productId);

    void setProductOrderRefundMsgId(@Param("refundMessageId") int refundMessageId);

    void setRefundRefuseMessage(@Param("refundRefuseTextarea") String refundRefuseTextarea, @Param("refundMessageId") int refundMessageId);

    int getRefundMessageIdByProductOrderId(@Param("productOrderId") int productOrderId);

    void updateOrderStatusSwitchRefundRefuse(@Param("refundRefuseOrderStatusStr") String refundRefuseOrderStatusStr, @Param("productOrderId") int productOrderId);

    String getRefundReasonByrefundMessageId(@Param("refundMessageId") int refundMessageId);

    String getRefundRefuseMessageByRefundMessageId(@Param("refundMessageId") int refundMessageId);

    int checkWishListExist(@Param("memberId") int memberId);

    String getWishListProductIds(@Param("memberId") int memberId);

    void insertWishList(@Param("memberId") int memberId, @Param("productIdJson") String productIdJson);

    void updateWishList(@Param("memberId") int memberId, @Param("productIdJson") String productIdJson);

    List<ProductVo> getProductForWishList(@Param("wishListProductId") int wishListProductId);

    void insertReviewNoImg(@Param("reviewText") String reviewText, @Param("reviewProductOrderId") int reviewProductOrderId, @Param("starRatingValue") int starRatingValue);

    void insertReviewYesImg(@Param("reviewText") String reviewText, @Param("reviewProductOrderId") int reviewProductOrderId, @Param("starRatingValue") int starRatingValue, @Param("saveFileName") String saveFileName);

    void updateMemberDiscountPoint50(@Param("memberId") int memberId);

    void updateMemberDiscountPoint100(@Param("memberId") int memberId);

    String getReviewByProductOrderId(@Param("reviewProductOrderId") int reviewProductOrderId);

    int getMemberIdByOrderId(@Param("reviewProductOrderId") int reviewProductOrderId);

    List<ReviewVo> getReviewList(@Param("memberId") int memberId);

    int getReviewId(@Param("reviewProductOrderId") int reviewProductOrderId);

    void updateReviewReplyAndReplyContent(@Param("reviewId") int reviewId, @Param("reviewReplyText") String reviewReplyText);

    String getReplyContentByProductOrderId(@Param("reviewProductOrderId") int reviewProductOrderId);

    String getProductQContent(@Param("productQId") int productQId);

    List<ProductQVo> getProductQ(@Param("memberId") int memberId);

    void updateQnaReplyContent(@Param("productQId") int productQId, @Param("qnaReplyContent") String qnaReplyContent);

    int checkExistProductQReplyContent(@Param("productQId") int productQId);

    List<ProductQVo> getProductQViewList(@Param("memberId") int memberId);

    String getProductQReplyContent(@Param("productQId") int productQId);
}
