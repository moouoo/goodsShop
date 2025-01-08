package com.spring.goodsShop.vo;

import lombok.Data;

import java.text.NumberFormat;
import java.util.Locale;

@Data
public class SearchVo {
    private int product_id;
    private int sub_category_id;
    private String brand;
    private String product_name;
    private int price;
    private int stock;
    private String img1;

    public String getFormattedPrice() {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.KOREA);
        return numberFormat.format(this.price) + "원";
    }
}
