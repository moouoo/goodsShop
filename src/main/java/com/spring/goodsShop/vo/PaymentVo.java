package com.spring.goodsShop.vo;

import lombok.Data;

@Data
public class PaymentVo {
    private int id;
    private int order_id; // 주문고유아이디
    private String payMethod; // 결재방법
    private String payment_date; // 결재시간
    private int finalPrice; // 총액
}
