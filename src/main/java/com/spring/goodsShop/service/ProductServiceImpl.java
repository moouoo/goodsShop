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

    @Override
    public String getSubcategoryTitle(int id) {
        return productDao.getSubcategoryTitle(id);
    }

    @Override
    public int getProductImgIdByProductName(String productName) {
        return productDao.getProductImgIdByProductName(productName);
    }

    @Override
    public List<Product_imgVo> getProductImgByProductImgId(int productImgId) {
        return productDao.getProductImgByProductImgId(productImgId);
    }

    @Override
    public String getproductDesignByProductId(int productId) {
        return productDao.getproductDesignByProductId(productId);
    }

    @Override
    public List<ProductVo> getProductByProductId(int productId) {
        return productDao.getProductByProductId(productId);
    }

    @Override
    public ProductVo getProductOneByProductId(int productId) {
        return productDao.getProductOneByProductId(productId);
    }

}
