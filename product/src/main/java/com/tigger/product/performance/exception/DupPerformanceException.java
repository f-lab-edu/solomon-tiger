package com.tigger.product.performance.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DupPerformanceException extends RuntimeException{
    public DupPerformanceException(String msg) {
        log.error(msg);
    }
}
