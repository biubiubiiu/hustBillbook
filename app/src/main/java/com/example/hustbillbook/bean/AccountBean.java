package com.example.hustbillbook.bean;

/*
 账户信息基类
 accountName: 账户名
 accountType: 账户类型（现金、银行卡....)
 accountMoney: 账户余额
 accountNote: 账户备注信息
 */
public class AccountBean {

    private String accountName;
    private int accountType;
    private double accountMoney;
    private String accountNote;

    public AccountBean(String aN, int aT, double aM, String aNote) {
        this.accountName = aN;
        this.accountType = aT;
        this.accountMoney = aM;
        this.accountNote = aNote;
    }

    public String getAccountNote() {
        return accountNote;
    }

    public void setAccountNote(String accountNote) {
        this.accountNote = accountNote;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public double getAccountMoney() {
        return accountMoney;
    }

    public void setAccountMoney(double accountMoney) {
        this.accountMoney = accountMoney;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}