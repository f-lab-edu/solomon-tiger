package com.tigger.product.performance.enums;

public enum PerformanceFlag implements EnumModel {
    NORMAL("0"),
    CANCEL("1"),
    END("2");

    private String value;

    PerformanceFlag(String value) {
        this.value = value;
    }

    @Override
    public String getKey() {
        return name();
    }

    @Override
    public String getValue() {
        return value;
    }
}
