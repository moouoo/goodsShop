package com.spring.goodsShop.etc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class PaymentHelper {
    public String orderCodeCreate() {
        // 주문번호 생성
        String uuid = UUID.randomUUID().toString().replace("-", "");
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDay = today.format(formatter).replace("-", "");
        String orderCode = formattedDay + "-" + uuid;
        return orderCode;
    }

    public String impUidCreate(){
        // 아임포트 impUid
        String impUid = "imp47844026";
        return impUid;
    }


}
