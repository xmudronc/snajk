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

    @Override
    public boolean equals(Object o) {  
        if (o == this) { 
            return true; 
        } 
        
        if (!(o instanceof Point)) { 
            return false; 
        } 
         
        Point c = (Point) o; 
        if (this.getX() == c.getX() && this.getY() == c.getY()) {
            return true;
        } else {
            return false;
        }
    } 
}