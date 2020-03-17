package com.example.hustbillbook;

import com.example.hustbillbook.bean.RecordBean;

public class TreeNode {
    //将recordBean和title统一处理

    public final boolean isParent;//是否为父节点

    //父节点对应的参数
    public boolean isExpand;//是否展开
    public String month;//月份
    public String expense;//流出
    public String income;//流入

    //子节点对应的参数
    public RecordBean record;//一条记录
    /*modified on 6/30*/
    //子节点的这个属性将被删除，直接使用indexOf方法
    //public int numInDatabase;//方便从数据库中删除
    public boolean visible;//是否展示

    public TreeNode(String mon, String exp, String inc){
        this.isParent = true;
        this.isExpand = true;//初始时为展开
        this.month = mon;
        this.expense = exp;
        this.income = inc;
    }

    public TreeNode(RecordBean r){
        this.isParent = false;
        this.record = r;
        //this.numInDatabase = i;
        this.visible = true;//初始时可见
    }
}
