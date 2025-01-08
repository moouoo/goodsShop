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

        if(part.equals("allProduct")) totalCount = productService.getProductAllTotalPageCount();
        else if (part.equals("mainCategoryProduct")) totalCount = productService.getMainCategoryProductTotalPageCount();
        else if (part.equals("subCategoryProduct")) totalCount = productService.getSubCategoryProductTotalPageCount();
        else if (part.equals("searchProduct")) totalCount = productService.getSearchProductTotalPageCount();
        else if (part.equals("best100Product")) totalCount = productService.getBest100ProductTotalPageCount();
        else if (part.equals("newProduct")) totalCount = productService.getNewProductTotalPageCount();
        else if (part.equals("notice")) totalCount = productService.getNoticeTotalPageCount();

        int totalPageCount = totalCount % onePageCount == 0 ? totalCount / onePageCount : totalCount / onePageCount + 1;

        int startIndexNum;
        if(pageNum == 0 || pageNum == 1) startIndexNum = 1;
        else startIndexNum = (pageNum - 1) * onePageCount;
        // select * from product order by id desc limit #{startIndexNum}, #{onePageCount};

        pageVo.setTotalPageCount(totalPageCount);
        pageVo.setTotalCount(totalCount);
        pageVo.setStartPageNum(startIndexNum);
        pageVo.setOnePageCount(onePageCount);

        // 1~5 pageNum = 1 block, 6~10 pageNum = 2 block




        return pageVo;
    }
}
