package com.spring.goodsShop.vo;

import lombok.Data;

@Data
public class ReviewVo {
    private int id;
    private int order_id;
    private int stars;
    private String review_img;
    private String content;
    private String write_date;
    private int reply; // 0 댓글 안쓴거(디폴트), 1 댓글쓴거
    private String replyContent;

    private String mid;
    private String productName;
    private String design;
}
