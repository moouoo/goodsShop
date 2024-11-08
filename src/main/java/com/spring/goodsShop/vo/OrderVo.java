package com.spring.goodsShop.vo;

import com.spring.goodsShop.enums.OrderStatus;
import lombok.Data;

@Data
public class OrderVo {
    private int id;
    private int member_id;
    private int product_id;
    private String design;
    private int amount;
    private String address;
    private String order_date;
    private String status;
}
