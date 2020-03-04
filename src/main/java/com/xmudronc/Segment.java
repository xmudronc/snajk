package com.xmudronc;

public class Segment {
    private Boolean bottom;
    private Boolean prevbottom;
    private Integer x;
    private Integer y;
    private Integer prevx;
    private Integer prevy;
    private Segment prev;
    private Segment next;

    public Segment() {
        this.bottom = false;
        this.prevbottom = true;
        this.x = 2;
        this.y = 1;
        this.prevx = 2;
        this.prevy = 1;
        this.prev = null;
        this.next = null;
    }

    public Segment(Boolean bottom, Integer x, Integer y) {
        this.bottom = bottom;
        this.prevbottom = !bottom;
        this.x = x;
        this.y = y;
        this.prevx = x;
        this.prevy = y;
        this.prev = null;
        this.next = null;
    }

    public Boolean getBottom() {
        return this.bottom;
    }

    public Boolean getPrevBottom() {
        return this.prevbottom;
    }

    public Integer getX() {
        return this.x;
    }

    public Integer getY() {
        return this.y;
    }

    public Integer getPrevX() {
        return this.prevx;
    }

    public Integer getPrevY() {
        return this.prevy;
    }

    public Segment getPrev() {
        return this.prev;
    }

    public Segment getNext() {
        return this.next;
    }

    public void setBottom(Boolean bottom) {
        this.prevbottom = this.bottom;
        this.bottom = bottom;
    }

    public void setX(Integer x) {
        this.prevx = this.x;
        this.x = x;
    }

    public void setY(Integer y) {
        this.prevy = this.y;
        this.y = y;
    }

    public void setPrev(Segment prev) {
        this.prev = prev;
    }

    public void setNext(Segment next) {
        this.next = next;
    }
}