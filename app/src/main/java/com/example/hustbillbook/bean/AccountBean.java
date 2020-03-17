package com.example.hustbillbook.bean;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/*
 账户信息基类
 accountName: 账户名
 accountType: 账户类型（现金、银行卡....)
 accountMoney: 账户余额
 accountNote: 账户备注信息
 */
public class AccountBean {

    public String accountName;
    public int accountType;
    public String accountMoney;
    public String accountTitle;

    public AccountBean() {
    }

    public AccountBean(String accountName, int accountType, String accountMoney, String accountTitle) {
        this.accountName = accountName;
        this.accountType = accountType;
        this.accountMoney = accountMoney;
        this.accountTitle = accountTitle;
    }

    public enum Type {
        CHUXUKA(50),
        XINYONGKA(51),
        ZHIFUBAO(52),
        WEIXINZHIFU(53),
        XIANJIN(54),
        JINGDONG(55),
        QITA(56),
        ADDACCOUNT(57);

        Type(int id) {
            this.id = id;
        }

        private final int id;

        @Contract(pure = true)
        public int getId() {
            return this.id;
        }
    }

    @NotNull
    @Contract(pure = true)
    public static String getTypeName(int id) {
        switch (id) {
            case 50:
                return "储蓄卡";
            case 51:
                return "信用卡";
            case 52:
                return "支付宝";
            case 53:
                return "微信支付";
            case 54:
                return "现金";
            case 55:
                return "京东金融";
            case 56:
                return "其他";
            default:
                return "";
        }
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null)
            return false;

        if (this == obj)
            return true;

        if (obj instanceof AccountBean){
            AccountBean a = (AccountBean)obj;
            if (this.accountName.equals(a.accountName)
                && this.accountTitle.equals(a.accountTitle)
                && this.accountMoney.equals(a.accountMoney)
                && this.accountType == a.accountType)
                return true;
            return false;
        }
        return false;
    }
}