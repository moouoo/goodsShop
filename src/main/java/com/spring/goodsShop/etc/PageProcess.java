package com.spring.goodsShop.etc;

import com.spring.goodsShop.service.ProductService;
import com.spring.goodsShop.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PageProcess {

    @Autowired
    ProductService productService;

    public PageVo pageProcess(String part, int pageNum, int onePageCount){
        PageVo pageVo = new PageVo();

        /*
           part 부분은
           productAll, mainCategoryProduct, subCategoryProduct,
           searchProduct, best100Product, newProduct, notice
        */
        int totalCount = 0;

        if(part.equals("allProduct")) totalCount = productService.getProductAllTotalCount();
//        else if (part.equals("mainCategoryProduct")) totalCount = productService.getMainCategoryProductTotalCount();
//        else if (part.equals("subCategoryProduct")) totalCount = productService.getSubCategoryProductTotalCount();
//        else if (part.equals("searchProduct")) totalCount = productService.getSearchProductTotalCount();
//        else if (part.equals("best100Product")) totalCount = productService.getBest100ProductTotalCount();
//        else if (part.equals("newProduct")) totalCount = productService.getNewProductTotalCount();
//        else if (part.equals("notice")) totalCount = productService.getNoticeTotalCount();

        int totalPageCount = totalCount % onePageCount == 0 ? (totalCount / onePageCount) : (totalCount / onePageCount) + 1;
        int startIndexNum = (pageNum - 1) * onePageCount;

        pageVo.setOnePageCount(onePageCount);
        pageVo.setStartIndexNum(startIndexNum);
        pageVo.setTotalPageCount(totalPageCount);
        pageVo.setPageNum(pageNum);

//        int curBlock = 1;
//        int blockSize = onePageCount;
//
//        if(blockSize >= totalPageCount) blockSize = totalPageCount;
//        else blockSize = 4;
//
//        if(pageNum >= 5 && pageNum <= 8){
//            curBlock = 5;
//            blockSize = 8;
//            if(totalPageCount < blockSize){
//                blockSize = curBlock;
//            }
//        }
//        else if (pageNum >= 9 && pageNum <= 12) {
//            curBlock = 9;
//            blockSize = 12;
//            if(totalPageCount < blockSize){
//                blockSize = curBlock;
//            }
//        }


        // 원하는 페이징 블럭 수 == blockSize
        // 그에 따른 페이징으로 넘어갈 수 조건문 if(pageNum >= ?)
        // 한페이지에 몇개의 상품을 보여줄것인가? -> control의 @RequestParam(name="onePageCount", defaultValue = "3", required=false) int onePageCount
        int curBlock = 1;
        int blockSize = 4;
        if(pageNum >= 5){
            blockSize = ((pageNum - 1) / blockSize) * blockSize + blockSize;
            curBlock = ((pageNum - 1) / blockSize) * blockSize + 1;

            if (totalPageCount < blockSize) {
                blockSize = curBlock;
            }
        }

        if(onePageCount >= totalPageCount){
            blockSize = totalPageCount;
        }

        pageVo.setCurBlock(curBlock);
        pageVo.setBlockSize(blockSize);


        return pageVo;
    }
}
