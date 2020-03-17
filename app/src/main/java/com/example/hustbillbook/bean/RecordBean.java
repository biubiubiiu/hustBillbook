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

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (this == obj)
            return true;

        if (obj instanceof RecordBean){
            RecordBean r = (RecordBean)obj;
            if (this.isExpense == r.isExpense
                && this.recordAccount == r.recordAccount
                && this.recordMoney.equals(r.recordMoney)
                && this.recordType == r.recordType
                && this.recordTitle.equals(r.recordTitle)
                && this.recordDate.equals(r.recordDate))
                return true;
            return false;
        }
        return false;
    }
}
