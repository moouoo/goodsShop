package com.spring.goodsShop.vo;

import lombok.Data;

@Data
public class PaymentVo {
    private int id;
    private int order_id;
    private String payMethod;
    private String payment_date;
    private int discountPrice;
    private int finalPrice;
}
