package com.spring.goodsShop.service;

import com.spring.goodsShop.vo.*;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    List<ProductVo> getProduct();

    List<Product_imgVo> getProductImg();

    int getMainCategoryIdByTitle(String title);

    List<ProductVo> getProductBySubcategoyId(int id);

    String getSubcategoryTitle(int id);

    int getProductImgIdByProductName(String productName);

    List<Product_imgVo> getProductImgByProductImgId(int productImgId);

    String getproductDesignByProductId(int productId);

    List<ProductVo> getProductByProductId(int productId);

    ProductVo getProductOneByProductId(int productId);

    List<CouponVo> getCouponVos(String level, int product_mainCategory, int product_subCategory);

    int getMainCategoryIdBySubCategoryId(int productSubCategory);

    boolean getCouponCheck(int couponId);

    BigDecimal getCouponRate(int couponId);

    int getMemberDiscountPointByMid(String mid);

    void setOrderOne(OrderVo order);

    void setPaymentOne(PaymentVo payment);

    String getProductImg1ByProductImgId(int productImgId);

    List<ProductVo> getProductVoOfNameAndBrandByProductId(List<Integer> productId);

    int getProductImgIdByProductId(int productId);

    int getProductPriceByProductId(int productId);

    String getProductNameByProductId(int productId);

    String getOrderMemberNameByMemberId(int memberId);

    List<ProductVo> getProductDESCLimit10();

    List<ProductVo> getProductDESC();

    int getProductSalesCount(int productId);

    void updateAddSalesCount(int productId, int addSalesCount);

    List<ProductVo> getProductBest100();

    List<ProductVo> getOrderProduct(int memberId);

    void updateProductOrderReviewStatus(String reviewStatus, int reviewProductOrderId);

    List<ReviewVo> getReview(int productId);

    int getReviewCountAll(int productId);

    void insertProductQ(int memberId, int productId, String askAboutProductContent, String askAboutProductTitle);

    List<ProductQVo> getProductQ(int productId);

    int getProductSubCategoryId(int productId);

    int getProductQCountAll(int productId);

    List<SearchVo> getSearchResult(String search);

    int getSearchResultCount(String search);

    List<ProductVo> getProductPagination(int startIndexNum, int onePageCount);

    int getProductAllTotalCount();

    List<ProductVo> getProductPaginationBySubcategoyId(int id, int startIndexNum, int onePageCount);

    int getMainCategoryProductTotalCount(int subCategoryId);

    int getSearchProductTotalCount(String search);

    List<SearchVo> getSearchResultPagination(String search, int startIndexNum, int onePageCount);

    List<ProductVo> getProductDESCPagination(int startIndexNum, int onePageCount);

    void updateProductStock(int productId, int amount);
}
