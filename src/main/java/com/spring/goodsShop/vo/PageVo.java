package com.spring.goodsShop.vo;

import lombok.Data;

@Data
public class PageVo {
    private int totalCount; // 총 갯수
    private int totalPageCount;      // 총 페이지
    private int curPageNum;     // 현재 페이지 번호
    private int onePageCount;   // 한 페이지 당 표시할 글 갯수 ?
    private int displayPageCount;   // 한번에 표시할 페이지 갯수 ?
    private int startPageNum;   // 시작페이지 번호
    private int endPageNum;     // 마지막페이지 번호
    private int curBlock;       // pageNum1~5, 1 block, pageNum6~10, 2 block

    private String part;
}
