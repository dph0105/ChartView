package com.penn.chartview;

/**
 * @author: DaDing
 * @description: 图表的点（x,y）
 * @create: 2018-11-13 11:43
 **/
public class Point {

    private float x;
    private float y;

    public Point() {
    }

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
