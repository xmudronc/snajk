package com.xmudronc;

public enum Symbol {
    BLOCK("\u2588\u2588"),
    FOOD("\u2588\u2588"),
    EMPTY("  ");

    public final String value;

    private Symbol(String value) {
        this.value = value;
    }
}
