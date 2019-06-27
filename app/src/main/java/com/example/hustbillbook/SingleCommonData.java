package com.example.hustbillbook;

import android.content.Context;
import android.database.Cursor;

import com.example.hustbillbook.bean.AccountBean;
import com.example.hustbillbook.bean.RecordBean;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/*
 使用单例模式设计
 存放了多个活动类需要访问和修改的公共数据
 */
public class SingleCommonData {

    // 存放所有账单记录
    private static List<RecordBean> recordBeanList = new ArrayList<>();

    // 存放所有账户
    private static List<AccountBean> accountBeanList = new ArrayList<>();

    // 让构造函数为private，这样该类就不会被实例化
    private SingleCommonData() {}

    // 获取账单记录
    @Contract(pure = true)
    public static List<RecordBean> getRecordList() {
        return recordBeanList;
    }

    // 获取所有账户
    @Contract(pure = true)
    public static List<AccountBean> getAccountList(){
        return accountBeanList;
    }

    public static void removeRecord(int positon) {
        recordBeanList.remove(positon);
    }

    public static void removeAccount(int position){
        accountBeanList.remove(position);
    }

    public static void addRecord(RecordBean recordBean) { recordBeanList.add(recordBean); }

    public static void addAccount(@NotNull AccountBean accountBean){
        if (accountBean.accountName.equals("添加账户'"))
            accountBeanList.add(accountBean);
        else
            accountBeanList.add(accountBeanList.size() - 1, accountBean);
    }

    public static void initData(Context context) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        // 每次启动时删除数据库中所有内容，测试时使用
        dataBaseHelper.deleteAllRecords();
        dataBaseHelper.deleteAllAccounts();
        // 生成假数据，测试时使用

        // 通过数据库将查询数据，插入到List容器中
        Cursor cursor = dataBaseHelper.getAllRecords();
        // 先清空一下数据，小心驶得万年船
        recordBeanList.clear();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                RecordBean recordBean = new RecordBean();
                recordBean.recordType = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.RECORD_TYPE));
                recordBean.recordTitle = cursor.getString(cursor.getColumnIndex(DataBaseHelper.RECORD_TITLE));
                recordBean.recordDate= cursor.getString(cursor.getColumnIndex(DataBaseHelper.RECORD_DATE));
                recordBean.recordMoney = cursor.getString(cursor.getColumnIndex(DataBaseHelper.RECORD_MONEY));
                recordBean.isExpense = cursor.getString(cursor.getColumnIndex(DataBaseHelper.RECORD_ISEXPENSE)).equals("1");
                recordBean.recordAccount = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.RECORD_ACCOUNT));
                recordBeanList.add(recordBean);
            }
            cursor.close();
        }

        //查询数据库将账户信息添加到List容器中
        cursor = dataBaseHelper.getAllAccounts();
        accountBeanList.clear();
        if (cursor!=null){
            while (cursor.moveToNext()){
                AccountBean accountBean = new AccountBean();
                accountBean.accountType = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.ACCOUNT_TYPE));
                accountBean.accountName = cursor.getString(cursor.getColumnIndex(DataBaseHelper.ACCOUNT_NAME));
                accountBean.accountMoney = cursor.getString(cursor.getColumnIndex(DataBaseHelper.ACCOUNT_MONEY));
                accountBean.accountTitle = cursor.getString(cursor.getColumnIndex(DataBaseHelper.ACCOUNT_TITLE));

                accountBeanList.add(accountBean);
            }
            cursor.close();
        }

        // 添加账户
        AccountBean addAccountBean = new AccountBean();
        addAccountBean.accountType = AccountBean.Type.ADDACCOUNT.getId();
        addAccountBean.accountName = "添加账户";
        addAccountBean.accountMoney = "";
        addAccountBean.accountTitle = "";

        //始终将添加账户加在最后
        accountBeanList.add(addAccountBean);
    }
}
