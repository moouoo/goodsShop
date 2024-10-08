package com.spring.goodsShop.vo;

import lombok.Data;

@Data
public class ProductVo {
    private int id; //pk
    private int sub_category_id; //fk
    private String brand;
    private String product_name;
    private int member_id;  //fk
    private int product_img_id; //fk
    private String product_img_detail;
    private int price;
    private int stock;
    private String Rdate;
    private String design; //json
}
