package com.example.hustbillbook.bean;


import org.jetbrains.annotations.Contract;


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

    public enum Type {
        //        CANYIN(R.string.canyin),  // do not use this method
        CANYIN(1),
        DIANYING(2),
        FANGZU(3),
        HONGBAO(4),
        HUAFEI(5),
        JIAOTONG(6),
        LINGSHI(7),
        QITA(8),
        SHENGHUOYONGPIN(9),
        SHUCAI(10),
        SHUIGUO(11),
        TAOBAO(12),
        YIFU(13),
        YILIAO(14),
        ZHUANZHANG(15),
        GONGZI(16),
        JIANGJIN(17),
        JIANZHI(18),
        LINGHUAQIAN(19),
        SHENGHUOFEI(20),
        TOUZI(21);

        Type(int id) {
            this.id = id;
        }

        private final int id;

        @Contract(pure = true)
        public int getId() {
            return this.id;
        }
    }

}
