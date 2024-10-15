package com.spring.goodsShop.dao;

import com.spring.goodsShop.vo.ProductVo;
import com.spring.goodsShop.vo.Product_imgVo;

import java.util.List;

public interface ProductDao {
    List<ProductVo> getProduct();

    List<Product_imgVo> getProductImg();
}
