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

    private String mid;
}
