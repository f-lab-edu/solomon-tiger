package com.tigger.product.performance.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DupTimeAndVenueException extends RuntimeException {
    public DupTimeAndVenueException(String msg) {
        log.error(msg);
    }
}
