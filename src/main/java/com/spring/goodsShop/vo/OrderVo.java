package com.spring.goodsShop.vo;

import com.spring.goodsShop.enums.OrderStatus;
import lombok.Data;

@Data
public class OrderVo {
    private int id;
    private int member_id; // 구입자 고유번호
    private int product_id; // 구매한 상품번호
    private String design; // 구매한 상품의 디자인
    private int amount; // 구매한 상품의 양
    private String address; // 구매자 주소
    private String order_date; // 구입시간
    private String status; // 결재상

    // 판매자정보는 삼품번호를 이용한다.
}
