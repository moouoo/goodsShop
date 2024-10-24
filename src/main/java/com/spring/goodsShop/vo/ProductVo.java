package com.spring.goodsShop.vo;

import lombok.Data;

import java.text.NumberFormat;
import java.util.Locale;

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

    public String getFormattedPrice() {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.KOREA);
        return numberFormat.format(this.price) + "원";
    }

    public String getFormatOneProductPrice(int amount){
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.KOREA);
        return numberFormat.format(this.price * amount) + "원";
    }
}
