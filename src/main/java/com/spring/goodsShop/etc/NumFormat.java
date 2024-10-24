package com.spring.goodsShop.etc;

import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class NumFormat {
    public String nemberFormat(int number){
        java.text.NumberFormat numberFormat = java.text.NumberFormat.getInstance(Locale.KOREA);
        String formatNum = numberFormat.format(number) + "원";
        return formatNum;
    }
}
