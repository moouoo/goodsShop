package com.spring.goodsShop.service;

import com.spring.goodsShop.vo.ProductVo;
import com.spring.goodsShop.vo.Product_imgVo;

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
}
