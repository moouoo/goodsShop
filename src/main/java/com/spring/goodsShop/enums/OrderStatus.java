package com.spring.goodsShop.enums;

public enum OrderStatus {
    PREPARING("배송전"),
    SHIPPING("배송중"),
    DELIVERED("배송완료"),
    REFUND_PROCESSING("환불처리중");

    private final String orderStatusStr;

    // 생성자
    OrderStatus(String orderStatusStr) {
        this.orderStatusStr = orderStatusStr;
    }

    // 설명 반환 메서드
    public String getOrderStatusStr() {
        return orderStatusStr;
    }
}