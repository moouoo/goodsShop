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

    public String getAccount_num() {
        return account_num != null ? account_num : "등록하지 않았습니다."; // 기본값 설정
    }
}
