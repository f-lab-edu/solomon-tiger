package com.tigger.product.performance.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DupPerformanceException extends RuntimeException{
    public DupPerformanceException() {
        super("중복된 공연이 존재합니다");
    }
}
