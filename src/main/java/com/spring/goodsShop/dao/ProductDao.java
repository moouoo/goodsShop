package com.spring.goodsShop.dao;

import com.spring.goodsShop.vo.*;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDao {
    List<ProductVo> getProduct();

    List<Product_imgVo> getProductImg();

    int getMainCategoryIdByTitle(@Param("title") String title);

    List<ProductVo> getProductBySubcategoyId(@Param("id") int id);

    String getSubcategoryTitle(@Param("id") int id);

    int getProductImgIdByProductName(@Param("productName") String productName);

    List<Product_imgVo> getProductImgByProductImgId(@Param("productImgId") int productImgId);

    String getproductDesignByProductId(@Param("productId") int productId);

    List<ProductVo> getProductByProductId(@Param("productId") int productId);

    ProductVo getProductOneByProductId(@Param("productId") int productId);

    List<CouponVo> getCouponAll();

    int getMainCategoryIdBySubCategoryId(@Param("productSubCategory") int productSubCategory);

    int getCouponCheck(@Param("couponId") int couponId);

    BigDecimal getCouponRate(@Param("couponId") int couponId);

    int getMemberDiscountPointByMid(@Param("mid") String mid);

    void setOrderOne(@Param("order") OrderVo order);

    void setPaymentOne(@Param("payment") PaymentVo payment);

    String getProductImg1ByProductImgId(@Param("productImgId") int productImgId);

    List<ProductVo> getProductVoOfNameAndBrandByProductId(@Param("productId") List<Integer> productId);

    int getProductImgIdByProductId(@Param("productId") int productId);

    int getProductPriceByProductId(@Param("productId") int productId);

    String getProductNameByProductId(@Param("productId") int productId);

    String getOrderMemberNameByMemberId(@Param("memberId") int memberId);

    List<ProductVo> getProductDESCLimit10();

    List<ProductVo> getProductDESC();

    int getProductSalesCount(@Param("productId") int productId);

    void updateAddSalesCount(@Param("productId") int productId, @Param("addSalesCount") int addSalesCount);

    List<ProductVo> getProductBest100();

    List<ProductVo> getOrderProduct(@Param("memberId") int memberId);

    void updateProductOrderReviewStatus(@Param("reviewStatus") String reviewStatus, @Param("reviewProductOrderId") int reviewProductOrderId);

    List<ReviewVo> getReview(@Param("productId") int productId);

    int getReviewCountAll(@Param("productId") int productId);

    void insertProductQ(@Param("memberId") int memberId, @Param("productId") int productId, @Param("askAboutProductContent")  String askAboutProductContent, @Param("askAboutProductTitle") String askAboutProductTitle);

    List<ProductQVo> getProductQ(@Param("productId") int productId);

    int getProductSubCategoryId(@Param("productId") int productId);

    int getProductQCountAll(@Param("productId") int productId);

    List<SearchVo> getSearchResult(@Param("search") String search);

    int getSearchResultCount(@Param("search") String search);

    int getProductAllTotalCount();

    List<ProductVo> getProductPagination(@Param("startIndexNum") int startIndexNum, @Param("onePageCount") int onePageCount);

    List<ProductVo> getProductPaginationBySubcategoyId(@Param("id") int id, @Param("startIndexNum") int startIndexNum, @Param("onePageCount") int onePageCount);

    int getMainCategoryProductTotalCount(@Param("subCategoryId") int subCategoryId);

    int getSearchProductTotalCount(@Param("search") String search);

    List<SearchVo> getSearchResultPagination(@Param("search") String search, @Param("startIndexNum") int startIndexNum, @Param("onePageCount") int onePageCount);

    List<ProductVo> getProductDESCPagination(@Param("startIndexNum") int startIndexNum, @Param("onePageCount") int onePageCount);

    void updateProductStock(@Param("productId") int productId, @Param("amount") int amount);
}
