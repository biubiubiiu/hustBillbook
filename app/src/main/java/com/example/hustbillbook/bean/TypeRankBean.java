package com.example.hustbillbook.bean;

public class TypeRankBean extends TypeViewBean implements Comparable{

    private float ratio;    // [0,1]
    private float number;   // 具体数据

    public TypeRankBean(int id, String typeName, String typeImg, float ratio, float number) {
        super(id, typeName, typeImg);
        this.ratio = ratio;
        this.number = number;
    }

    public float getRatio() {
        return ratio;
    }

    public float getNumber() {
        return number;
    }

    @Override
    public int compareTo(Object o) {
        if (number == ((TypeRankBean) o).number)
            return 0;
        else if (number < ((TypeRankBean) o).number)
            return -1;
        else
            return 1;
    }
}
