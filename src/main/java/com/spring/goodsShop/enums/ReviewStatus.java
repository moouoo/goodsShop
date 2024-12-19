package com.spring.goodsShop.enums;

public enum ReviewStatus {
    REVIEW_PENDING("리뷰전"),
    REVIEW_COMPLETED("리뷰완료");

    private final String reviewStatusStr;

    // 생성자
    ReviewStatus(String reviewStatusStr) {
        this.reviewStatusStr = reviewStatusStr;
    }

    // 설명 반환 메서드
    public String getReviewStatusStr() {
        return reviewStatusStr;
    }
}
