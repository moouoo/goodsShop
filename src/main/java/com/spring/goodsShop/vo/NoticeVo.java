package com.spring.goodsShop.vo;

import lombok.Data;

@Data
public class NoticeVo {
    private int id;
    private String title;
    private String content;
    private String wDate;
    private int count;
    private String mid;
}
