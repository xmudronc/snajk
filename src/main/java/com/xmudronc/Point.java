package com.xmudronc;

public class Point {
    private Integer x;
    private Integer y;
    private Integer value;

    public Point(Integer x, Integer y, Integer value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public Point(Integer x, Integer y) {
        this.x = x;
        this.y = y;
        this.value = 500;
    }

    public Integer getX() {
        return this.x;
    }

    public Integer getY() {
        return this.y;
    }

    public Integer getValue() {
        return this.value;
    }
}