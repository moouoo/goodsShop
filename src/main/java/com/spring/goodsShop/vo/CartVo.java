package com.spring.goodsShop.vo;

import lombok.Data;

@Data
public class CartVo {
    private int productId;
    private String design;
    private int price;
    private int amount;

    public CartVo(int productId, String design, int amount, int price){
        this.productId = productId;
        this.design = design;
        this.amount = amount;
        this.price = price;
    }
}
