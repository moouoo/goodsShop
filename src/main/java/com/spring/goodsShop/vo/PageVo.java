package com.spring.goodsShop.vo;

import lombok.Data;

@Data
public class PageVo {
    private int totalPageCount; // 총 페이지
    private int pageNum;        // 현재 페이지 번호
    private int startIndexNum;  // 시작페이지 번호 - 1
    private int onePageCount;   // 한 페이지에 나올 갯수

    private int blockSize;
    private int curBlock;

    private int totalCount;

}
