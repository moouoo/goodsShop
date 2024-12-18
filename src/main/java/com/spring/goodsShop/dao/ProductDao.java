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
}
