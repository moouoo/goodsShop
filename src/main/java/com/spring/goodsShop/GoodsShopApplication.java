package com.spring.goodsShop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.spring.goodsShop.dao")
public class GoodsShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoodsShopApplication.class, args);
	}

}
