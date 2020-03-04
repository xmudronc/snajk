package com.xmudronc;

public class Segment {
    private Integer x;
    private Integer y;
    private Integer prevx;
    private Integer prevy;
    private Segment prev;
    private Segment next;

    public Segment() {
        this.x = 5;
        this.y = 2;
        this.prevx = 3;
        this.prevy = 2;
        this.prev = null;
        this.next = null;
    }

    public Segment(Integer x, Integer y) {
        this.x = x;
        this.y = y;
        this.prevx = null;
        this.prevy = null;
        this.prev = null;
        this.next = null;
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

    public void addSegment(Segment newSeg) {
        if (next != null) {
            this.next.addSegment(newSeg);
        } else {
            this.next = newSeg;
            this.next.setPrev(this);
            this.next.setX(this.next.getPrev().getPrevX());
            this.next.setY(this.next.getPrev().getPrevY());
        }
    }
}