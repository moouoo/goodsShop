package com.spring.goodsShop.vo;

import com.spring.goodsShop.enums.OrderStatus;
import lombok.Data;

import java.util.List;

@Data
public class OrderVo {
    private int id;
    private int member_id; // 구입자 고유번호
    private int product_id; // 구매한 상품번호
    private String design; // 구매한 상품의 디자인
    private int amount; // 구매한 상품의 양
    private String address; // 구매자 주소
    private String order_date; // 구입시간
    private String status; // 결재상태
    private int price;
    private String productName; // 구매한상품 이름
    private String orderStaus; // 배송전, 배송중, 배송완료, 환불처리중

    private String orderMemberName; // 구입자 이름
    // 주문번호(orderVo,id), 상품이름([디자인]상품이름), 구입한가격, 구입날짜, orderStatus

}
