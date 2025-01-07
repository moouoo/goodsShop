package com.spring.goodsShop.vo;

import lombok.Data;

@Data
public class ProductQVo {
    private int id;
    private int product_id;
    private int member_id;
    private String title;
    private String content;
    private String wDate;
    private int reply;  // 0: 답글 아직 x, 1:답글완료
    private String replyContent;

    private String mid;
    private String product_name;
}
