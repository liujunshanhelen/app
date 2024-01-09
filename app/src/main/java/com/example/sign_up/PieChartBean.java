package com.example.sign_up;

import android.graphics.Color;

public class PieChartBean {
    private String valuer;      //说明
    private Float angle;    //占的大小
    private int color;      //颜色值

    public PieChartBean(String valuer, Float angle, int color) {
        this.valuer = valuer;
        this.angle = angle;
        this.color = color;
    }

    public String getValuer() {
        return valuer;
    }

    public int getColor() {
        return color;
    }

    public Float getAngle() {
        return angle;
    }
}
