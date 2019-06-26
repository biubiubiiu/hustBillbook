package com.example.hustbillbook.bean;

/*
 账单类别
 仅在视图展示时生效
 不参与数据记录
 */
public class TypeViewBean {

    private int id;     // 此处的id与RecordBean类中的id对应
    private String typeName;
    private String typeImg;

    public TypeViewBean(int id, String typeName, String typeImg) {
        this.id = id;
        this.typeName = typeName;
        this.typeImg = typeImg;
    }

    public int getId() {
        return id;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getTypeImg() {
        return typeImg;
    }

}
