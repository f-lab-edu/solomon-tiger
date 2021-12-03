package com.tigger.product.performance.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NonExistentPerformanceException extends RuntimeException {

    public NonExistentPerformanceException() {
        super("존재하지 않는 공연입니다");
    }
}
