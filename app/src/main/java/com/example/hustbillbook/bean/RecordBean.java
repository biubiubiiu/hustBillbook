package com.example.hustbillbook.bean;

import java.io.Serializable;

/*
 账单记录基类
 recordTitle: 标题
 recordDate: 日期
 recordMoney: 金额
 */

//TODO 是否考虑使用在money前加入'+','-'号的方式来表示收入支出？采用这种方式是否会限制新功能的拓展？
public class RecordBean implements Serializable {

    public String recordTitle;
    public String recordDate;
    public String recordMoney;

}
