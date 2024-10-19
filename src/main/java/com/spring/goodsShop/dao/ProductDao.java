package com.spring.goodsShop.dao;

import com.spring.goodsShop.vo.ProductVo;
import com.spring.goodsShop.vo.Product_imgVo;
import org.apache.ibatis.annotations.Param;

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
}
