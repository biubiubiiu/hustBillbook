package com.example.hustbillbook.bean;

import org.jetbrains.annotations.NotNull;

public class TypeRankBean{

    private final TypeViewBean typeView;
    private float ratio;    // [0,1]
    private final float number;   // 具体数据

    public TypeRankBean(@NotNull TypeViewBean typeViewBean, float ratio, float number) {
        this.typeView = typeViewBean;
        this.ratio = ratio;
        this.number = number;
    }

    public TypeViewBean getTypeView() {
        return typeView;
    }

    public float getRatio() {
        return ratio;
    }

    public float getNumber() {
        return number;
    }

    public void setRatio(float ratio) { this.ratio = ratio; }
}
