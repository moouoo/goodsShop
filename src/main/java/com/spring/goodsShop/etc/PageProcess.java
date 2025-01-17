package com.spring.goodsShop.etc;

import com.spring.goodsShop.service.AdminService;
import com.spring.goodsShop.service.ProductService;
import com.spring.goodsShop.vo.PageVo;
import com.spring.goodsShop.vo.ProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PageProcess {

    @Autowired
    ProductService productService;

    @Autowired
    AdminService adminService;

    public PageVo pageProcess(String part, int pageNum, int onePageCount, List<ProductVo> product_list, String search){
        PageVo pageVo = new PageVo();

        /*
           part 부분은
           productAll, mainCategoryProduct, subCategoryProduct,
           searchProduct, best100Product, newProduct, notice
        */
        int totalCount = 0;

        if(part.equals("allProduct")) totalCount = productService.getProductAllTotalCount();
        else if (part.equals("mainCategoryProduct")) totalCount = product_list.size();
        else if (part.equals("subCategoryProduct")) totalCount = product_list.size();
        else if (part.equals("searchProduct")) totalCount = productService.getSearchProductTotalCount(search);
        else if (part.equals("best100Product")) totalCount = product_list.size();
        else if (part.equals("newProduct")) totalCount = productService.getProductAllTotalCount();
        else if (part.equals("notice")) totalCount = adminService.getNoticeTotalCount();

        int totalPageCount = totalCount % onePageCount == 0 ? (totalCount / onePageCount) : (totalCount / onePageCount) + 1;
        int startIndexNum = (pageNum - 1) * onePageCount;

        pageVo.setOnePageCount(onePageCount);
        pageVo.setStartIndexNum(startIndexNum);
        pageVo.setTotalPageCount(totalPageCount);
        pageVo.setPageNum(pageNum);

        // 원하는 페이징 블럭 수 == blockSize
        // 그에 따른 페이징으로 넘어갈 수 조건문 if(pageNum >= ?)
        // 한페이지에 몇개의 상품을 보여줄것인가? -> control의 @RequestParam(name="onePageCount", defaultValue = "3", required=false) int onePageCount

//        int curBlock = 1;
//        int blockSize = 4;
//
//        if(blockSize >= totalPageCount) blockSize = totalPageCount;
//
//        if(pageNum >= 5 && pageNum <= 8){
//            curBlock = 5;
//            blockSize = 8;
//            if(totalPageCount < blockSize){
//                blockSize = totalPageCount;
//            }
//        }
//        else if (pageNum >= 9 && pageNum <= 12) {
//            curBlock = 9;
//            blockSize = 12;
//            if(totalPageCount < blockSize){
//                blockSize = totalPageCount;
//            }
//        }
//        pageVo.setCurBlock(curBlock);
//        pageVo.setBlockSize(blockSize);
//
//        return pageVo;

        int curBlock = 1;
        int blockSize = 4;

        if(pageNum >= 5){
            curBlock = ((pageNum - 1) / blockSize) * blockSize + 1;
            blockSize = ((pageNum - 1) / blockSize) * blockSize + blockSize;
        }

        if (totalPageCount < blockSize) blockSize = totalPageCount;

        pageVo.setCurBlock(curBlock);
        pageVo.setBlockSize(blockSize);

        return pageVo;
    }

    public List<ProductVo> categoryPagination(List<ProductVo> product_listImsi, int onePageCount, int startIndexNum, int pageNum) {
        List<ProductVo> product_list = new ArrayList<>();

        // 처음 1페이지 처리, 삼품갯수가 20개 초과인. 혹은 딱 20개인.
        if((onePageCount > startIndexNum && onePageCount < product_listImsi.size()) || onePageCount == product_listImsi.size()){
            product_list = product_listImsi.subList(startIndexNum, onePageCount);
        }

        // 중간페이지치리
        else if((onePageCount <= startIndexNum && product_listImsi.size() / startIndexNum >= 1)){
            if((product_listImsi.size() - startIndexNum) < onePageCount) product_list = product_listImsi.subList(startIndexNum, product_listImsi.size());
            else product_list = product_listImsi.subList(startIndexNum, (pageNum * onePageCount));

        }
        // 마지막페이지 처리
        else {
            product_list = product_listImsi.subList(startIndexNum, product_listImsi.size());
            System.out.println("여기옴?");
        }

        return product_list;
    }
}
