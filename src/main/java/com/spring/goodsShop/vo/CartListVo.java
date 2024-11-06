package com.spring.goodsShop.vo;

import lombok.Data;

@Data
public class CartListVo {
    private String product_name;
    private String design;
    private int price;
    private int amount;
    private String img;

}
