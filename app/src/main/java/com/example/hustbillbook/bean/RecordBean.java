package com.example.hustbillbook.bean;


/*
 账单记录基类
 recordType: 类别
 recordTitle: 备注
 recordDate: 日期
 recordMoney: 金额
 */

public class RecordBean {

    public boolean isExpense = true;
    public int recordType;
    public String recordTitle;
    public String recordDate;
    public String recordMoney;
    public int recordAccount;

}
