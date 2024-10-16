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
}
