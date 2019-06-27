package com.example.hustbillbook.bean;

import org.jetbrains.annotations.Contract;

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

    public enum Type{
        CHUXUKA (50),
        XINYONGKA (51),
        ZHIFUBAO (52),
        WEIXINZHIFU (53),
        XIANJIN (54),
        JINGDONG (55),
        QITA (56),
        ADDACCOUNT (57);

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