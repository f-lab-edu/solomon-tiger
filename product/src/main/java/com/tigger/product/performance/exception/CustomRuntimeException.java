package com.tigger.product.performance.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomRuntimeException extends RuntimeException {

    public CustomRuntimeException(String msg) {
        super(msg);
    }
}
