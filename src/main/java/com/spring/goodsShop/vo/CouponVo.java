package com.spring.goodsShop.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CouponVo {
    private int id;
    private String coupon_code;
    private BigDecimal coupon_rate;
    private String end_date;
    private String coupon_name;
    private String coupon_grade;
}
