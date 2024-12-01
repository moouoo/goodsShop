package com.spring.goodsShop.enums;

public enum PaymentStatus {
    PENDING("대기 중"),
    PROCESSING("처리 중"),
    COMPLETED("완료됨"),
    CANCELED("취소됨"),
    REFUNDED("환불 완료"),
    RETURNED("반품됨"),
    AWAITING_PAYMENT("결제 대기 중"),
    PAID("결제 완료"),
    SHIPPED("배송 중"),
    DELIVERED("배송 완료");

    private String displayName;

    PaymentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
