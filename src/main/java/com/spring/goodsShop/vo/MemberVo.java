package com.spring.goodsShop.vo;

import lombok.Data;

@Data
public class MemberVo {
    private int id;
    private String mid;
    private String pwd;
    private String email;
    private String account_num;
    private int level;
}
