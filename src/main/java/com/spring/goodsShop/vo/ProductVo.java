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
    private int sales_count;

    // wishList에서 join을 통한 상품이미지를 가져오기 위한 장치
    // productNew.html에서 이용해보기 join이용해서
    private String img1;

    // 리뷰할수있는 상품가져올때 사용.
    private String orderDesign;
    private int orderPrice;
    private String reviewStatus;


    public String getFormattedPrice() {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.KOREA);
        return numberFormat.format(this.price) + "원";
    }

    public String getFormattedOrderPrice(){
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.KOREA);
        return numberFormat.format(this.orderPrice) + "원";
    }

    public String getFormatOneProductPrice(int amount){
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.KOREA);
        return numberFormat.format(this.price * amount) + "원";
    }
}
