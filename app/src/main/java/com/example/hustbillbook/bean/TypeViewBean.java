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

    public TypeViewBean() {
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

    public void setId(int id) {
        this.id = id;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setTypeImg(String typeImg) {
        this.typeImg = typeImg;
    }
}
