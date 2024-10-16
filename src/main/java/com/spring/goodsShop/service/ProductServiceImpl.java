package com.spring.goodsShop.service;

import com.spring.goodsShop.dao.ProductDao;
import com.spring.goodsShop.vo.ProductVo;
import com.spring.goodsShop.vo.Product_imgVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    ProductDao productDao;

    @Override
    public List<ProductVo> getProduct() {
        return productDao.getProduct();
    }

    @Override
    public List<Product_imgVo> getProductImg() {
        return productDao.getProductImg();
    }

    @Override
    public int getMainCategoryIdByTitle(String title) {
        return productDao.getMainCategoryIdByTitle(title);
    }

    @Override
    public List<ProductVo> getProductBySubcategoyId(int id) {
        return productDao.getProductBySubcategoyId(id);
    }
}
