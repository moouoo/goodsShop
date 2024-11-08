package com.spring.goodsShop.etc;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> exceptionHandler(){
        return ResponseEntity.status(400).body("url창 타입 미스 매치 오류 발생");
    }

//      모든오류에 걸림
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> exceptionHandler(){
//        return ResponseEntity.status(400).body("에러남");
//    }
}
