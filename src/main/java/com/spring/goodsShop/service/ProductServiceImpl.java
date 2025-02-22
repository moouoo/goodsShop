package com.spring.goodsShop.service;

import com.spring.goodsShop.dao.ProductDao;
import com.spring.goodsShop.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    @Override
    public List<CouponVo> getCouponVos(String level, int product_mainCategory, int product_subCategory) {
        List<CouponVo> couponList = new ArrayList<>();

        // 모든 쿠폰을 가져온다
        List<CouponVo> couponVoList = productDao.getCouponAll();

        // 쿠폰만료일을 구분하기위한 오늘의 날짜 가져오기
        LocalDate today = LocalDate.now();

        // 적용한 쿠폰 막는 코드 작성 + 사용한 쿠폰 막는 코드 작성



        //쿠폰의 코드분석
        for (int i = 0; i < couponVoList.size(); i++) {
            CouponVo couponVo = couponVoList.get(i);
            String dbDateTime = couponVo.getEnd_date();  // 쿠폰의 만료일을 가져옴
            // 올바른 포맷 정의
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime endDateTime = LocalDateTime.parse(dbDateTime, formatter);
            // LocalDate로 변환하여 비교
            LocalDate endDate = endDateTime.toLocalDate();

            String temCode =  couponVo.getCoupon_code();
            String[] temCodeData = temCode.split("-");
            String code1 = temCodeData[0].toString(); // all, none : 모두 혹은 아닌지
            String code2 = temCodeData[1].toString(); // all, 숫자 : 대분류
            String code3 = temCodeData[2].toString(); // all, 숫자 : 소분류
            String code4 = temCodeData[3].toString(); // level1, level2 : 등급

            if(endDate.isAfter(today) || endDate.equals(today)){
                if (level.equals(code4)) {  // 사용자 등급에 맞는것만 리스트에 넣는다.
                    if (code1.equals("all")) {  // 첫번째 코드가 all이면 쿠폰을 리스트에 전부 담는다.
                        couponList.add(couponVo);
                    }
                    else if (!code2.equals("all") && !code3.equals("all")) {    // 첫번째 코드가 all이 아니면 두번째, 세번쨰 코드의 all을 확인하여 둘 다 아닐 경우.
                        if (product_mainCategory == Integer.parseInt(code2) && product_subCategory == Integer.parseInt(code3)) { // 대/소분류id와 비교하여 같은거를 리스트에 담는다.
                            couponList.add(couponVo);
                        }
                    }
                    else if (!code2.equals("all") && code3.equals("all")) { // 대분류가 all이 아니고 소분류가 all인 경우
                        if (product_mainCategory == Integer.parseInt(code2)) { // 대분류id와 비교하여 같은거를 리스트에 담는다.
                            couponList.add(couponVo);
                        }
                    }
                    else if (code2.equals("all") && !code3.equals("all")) { // 소분류가 all이고 대분류가 all이 아닌경우
                        if (product_subCategory == Integer.parseInt(code3)) { // 소분류id와 비교하여 같은것을 리스트에 넣는다.
                            couponList.add(couponVo);
                        }
                    }
                }
            }
        }
        return couponList;
    }

    @Override
    public int getMainCategoryIdBySubCategoryId(int productSubCategory) {
        return productDao.getMainCategoryIdBySubCategoryId(productSubCategory);
    }

    @Override
    public boolean getCouponCheck(int couponId) {
        boolean check;

        int count = productDao.getCouponCheck(couponId);

        if(count == 1) check = true;
        else check = false;
        
        return check;
    }

    @Override
    public BigDecimal getCouponRate(int couponId) {
        return productDao.getCouponRate(couponId);
    }

    @Override
    public int getMemberDiscountPointByMid(String mid) {
        return productDao.getMemberDiscountPointByMid(mid);
    }

    @Override
    public void setOrderOne(OrderVo order) {
        productDao.setOrderOne(order);
    }

    @Override
    public void setPaymentOne(PaymentVo payment) {
        productDao.setPaymentOne(payment);
    }

    @Override
    public String getProductImg1ByProductImgId(int productImgId) {
        return productDao.getProductImg1ByProductImgId(productImgId);
    }

    @Override
    public List<ProductVo> getProductVoOfNameAndBrandByProductId(List<Integer> productId) {
        return productDao.getProductVoOfNameAndBrandByProductId(productId);
    }

    @Override
    public int getProductImgIdByProductId(int productId) {
        return productDao.getProductImgIdByProductId(productId);
    }

    @Override
    public int getProductPriceByProductId(int productId) {
        return productDao.getProductPriceByProductId(productId);
    }

    @Override
    public String getProductNameByProductId(int productId) {
        return productDao.getProductNameByProductId(productId);
    }

    @Override
    public String getOrderMemberNameByMemberId(int memberId) {
        return productDao.getOrderMemberNameByMemberId(memberId);
    }

    @Override
    public List<ProductVo> getProductDESCLimit10() {
        return productDao.getProductDESCLimit10();
    }

    @Override
    public List<ProductVo> getProductDESC() {
        return productDao.getProductDESC();
    }

    @Override
    public int getProductSalesCount(int productId) {
        return productDao.getProductSalesCount(productId);
    }

    @Override
    public void updateAddSalesCount(int productId, int addSalesCount) {
        productDao.updateAddSalesCount(productId, addSalesCount);
    }

    @Override
    public List<ProductVo> getProductBest100() {
        return productDao.getProductBest100();
    }

    @Override
    public List<ProductVo> getOrderProduct(int memberId) {
        return productDao.getOrderProduct(memberId);
    }

    @Override
    public void updateProductOrderReviewStatus(String reviewStatus, int reviewProductOrderId) {
        productDao.updateProductOrderReviewStatus(reviewStatus, reviewProductOrderId);
    }

    @Override
    public List<ReviewVo> getReview(int productId) {
        return productDao.getReview(productId);
    }

    @Override
    public int getReviewCountAll(int productId) {
        return productDao.getReviewCountAll(productId);
    }

    @Override
    public void insertProductQ(int memberId, int productId, String askAboutProductContent, String askAboutProductTitle) {
        productDao.insertProductQ(memberId, productId, askAboutProductContent, askAboutProductTitle);
    }

    @Override
    public List<ProductQVo> getProductQ(int productId) {
        return productDao.getProductQ(productId);
    }

    @Override
    public int getProductSubCategoryId(int productId) {
        return productDao.getProductSubCategoryId(productId);
    }

    @Override
    public int getProductQCountAll(int productId) {
        return productDao.getProductQCountAll(productId);
    }

    @Override
    public List<SearchVo> getSearchResult(String search) {
        return productDao.getSearchResult(search);
    }

    @Override
    public int getSearchResultCount(String search) {
        return productDao.getSearchResultCount(search);
    }

    @Override
    public List<ProductVo> getProductPagination(int startIndexNum, int onePageCount) {
        return productDao.getProductPagination(startIndexNum, onePageCount);
    }

    @Override
    public int getProductAllTotalCount() {
        return productDao.getProductAllTotalCount();
    }

    @Override
    public List<ProductVo> getProductPaginationBySubcategoyId(int id, int startIndexNum, int onePageCount) {
        return productDao.getProductPaginationBySubcategoyId(id, startIndexNum, onePageCount);
    }

    @Override
    public int getMainCategoryProductTotalCount(int subCategoryId) {
        return productDao.getMainCategoryProductTotalCount(subCategoryId);
    }

    @Override
    public int getSearchProductTotalCount(String search) {
        return productDao.getSearchProductTotalCount(search);
    }

    @Override
    public List<SearchVo> getSearchResultPagination(String search, int startIndexNum, int onePageCount) {
        return productDao.getSearchResultPagination(search, startIndexNum, onePageCount);
    }

    @Override
    public List<ProductVo> getProductDESCPagination(int startIndexNum, int onePageCount) {
        return productDao.getProductDESCPagination(startIndexNum, onePageCount);
    }

    @Override
    public void updateProductStock(int productId, int amount) {
        productDao.updateProductStock(productId, amount);
    }

}
