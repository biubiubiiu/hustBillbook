package com.example.hustbillbook;

import android.content.Context;
import android.database.Cursor;

import com.example.hustbillbook.bean.AccountBean;
import com.example.hustbillbook.bean.RecordBean;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
 使用单例模式设计
 存放了多个活动类需要访问和修改的公共数据
 */
public class SingleCommonData {

    // 存放所有账单记录
    private static final List<RecordBean> recordBeanList = new ArrayList<>();
    private static final Map<RecordBean, Integer> recordMappingTbl = new HashMap<>();

    // 存放所有账户
    private static final List<AccountBean> accountBeanList = new ArrayList<>();
    private static final Map<AccountBean, Integer> accountMappingTbl = new HashMap<>();

    private static DataBaseHelper dataBaseHelper;

    // 让构造函数为private，这样该类就不会被实例化
    private SingleCommonData() {
    }

    // 获取账单记录
    public static List<RecordBean> getRecordList() {
        return recordBeanList;
    }

    public static DataBaseHelper getDataBaseHelper() {
        return dataBaseHelper;
    }

    // 获取所有账户
    public static List<AccountBean> getAccountList() {
        return accountBeanList;
    }

    /*
     * Note: 将删除数据库的操作下移到 Data 类中，使逻辑更清晰
     */

    // 请不要以 RecordBean 类型作为函数参数，尽管这在使用上会带来些许便利，但在某些场合下会产生反效果
    public static void removeRecord(int position) {
        RecordBean bean = recordBeanList.get(position);

        //删除记录时需同时更新对应账户的余额
        /*modifiec on 6/30*/
        double d = Double.valueOf(accountAt(bean.recordAccount).accountMoney);
        if (bean.isExpense)
            d += Double.valueOf(bean.recordMoney);
        else
            d -= Double.valueOf(bean.recordMoney);
        updateAccountMoney(bean.recordAccount, d);


        recordBeanList.remove(position);
        int index = recordMappingTbl.get(bean);
        dataBaseHelper.deleteOneRecord(index);
        syncRecord();
    }

    public static void removeAccount(int position) {
        AccountBean bean = accountBeanList.get(position);
        accountBeanList.remove(position);
        int index = accountMappingTbl.get(bean);
        dataBaseHelper.deleteOneAccount(index);
        syncAccount();

        /*modified on 6/30*/
        //要把recordList下该账户的记录也删掉
        //需要用迭代器一边遍历一边删除
        Iterator<RecordBean> it = recordBeanList.iterator();
        while (it.hasNext()){
            if (it.next().recordAccount == position)
                it.remove();
        }

    }

    public static void addRecord(@NotNull RecordBean recordBean) {
        recordBeanList.add(recordBean);
        dataBaseHelper.insertRecord(recordBean);
        syncRecord();
    }

    public static void addAccount(@NotNull AccountBean accountBean) {
        accountBeanList.add(accountBeanList.size() - 1, accountBean);
        dataBaseHelper.insertAccount(accountBean);
        syncAccount();
    }

    public static void updateRecord(RecordBean old, RecordBean now) {
        int index = recordMappingTbl.get(old);
        int position = recordBeanList.indexOf(old);
        recordBeanList.set(position, now);
        dataBaseHelper.updateRecordInfo(now.recordDate,
                now.recordMoney,
                now.recordTitle,
                now.isExpense,
                now.recordType,
                now.recordAccount,
                index);
    }

    public static void updateAccount(AccountBean old, AccountBean now) {
        int index = accountMappingTbl.get(old);
        int position = accountBeanList.indexOf(old);
        accountBeanList.set(position, now);
        dataBaseHelper.updateAccountInfo(now.accountName,
                now.accountMoney,
                now.accountTitle,
                index);
    }

    public static void updateAccountMoney(int accountNum, double money) {
        AccountBean bean=  accountBeanList.get(accountNum);
        bean.accountMoney = String.valueOf(money);
        int index = accountMappingTbl.get(bean);
        dataBaseHelper.updateAccountMoney(index, String.valueOf(money));
    }

    public static RecordBean recordAt(int position) {
        return recordBeanList.get(position);
    }

    public static AccountBean accountAt(int position) {
        return accountBeanList.get(position);
    }

    public static void initData(Context context) {
        dataBaseHelper = new DataBaseHelper(context);
        //------------------------------------------------------------------------------------------
        // 每次启动时删除数据库中所有内容，测试时使用
        //dataBaseHelper.deleteAllRecords();
        //dataBaseHelper.deleteAllAccounts();
        //------------------------------------------------------------------------------------------

        // 通过数据库将查询数据，插入到List容器中，并更新地址映射表
        syncRecord();

        // 查询数据库将账户信息添加到List容器中，并更新地址映射表
        syncAccount();

        if (accountBeanList.size() == 0) {
            //如果没有账户信息，则添加一个初始账户
            AccountBean accountBean = new AccountBean();
            accountBean.accountName = "现金";
            accountBean.accountMoney = "0";
            accountBean.accountTitle = "现金账户";
            accountBean.accountType = AccountBean.Type.XIANJIN.getId();

            accountBeanList.add(accountBean);
            dataBaseHelper.insertAccount(accountBean);
        }
    }

    public static void syncRecord() {
        Cursor cursor = dataBaseHelper.getSortedRecords();

        // 先清空一下数据，小心驶得万年船
        recordBeanList.clear();
        recordMappingTbl.clear();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                RecordBean recordBean = new RecordBean();
                recordBean.recordType = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.RECORD_TYPE));
                recordBean.recordTitle = cursor.getString(cursor.getColumnIndex(DataBaseHelper.RECORD_TITLE));
                recordBean.recordDate = cursor.getString(cursor.getColumnIndex(DataBaseHelper.RECORD_DATE));
                recordBean.recordMoney = cursor.getString(cursor.getColumnIndex(DataBaseHelper.RECORD_MONEY));
                recordBean.isExpense = cursor.getString(cursor.getColumnIndex(DataBaseHelper.RECORD_ISEXPENSE)).equals("1");
                recordBean.recordAccount = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.RECORD_ACCOUNT));

                recordBeanList.add(recordBean);
                recordMappingTbl.put(recordBean, cursor.getInt(cursor.getColumnIndex(DataBaseHelper.INC_ID)));
            }
            cursor.close();
        }
    }

    public static void syncAccount() {
        Cursor cursor = dataBaseHelper.getSortedAccounts();

        accountBeanList.clear();
        accountMappingTbl.clear();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                AccountBean accountBean = new AccountBean();
                accountBean.accountType = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.ACCOUNT_TYPE));
                accountBean.accountName = cursor.getString(cursor.getColumnIndex(DataBaseHelper.ACCOUNT_NAME));
                accountBean.accountMoney = cursor.getString(cursor.getColumnIndex(DataBaseHelper.ACCOUNT_MONEY));
                accountBean.accountTitle = cursor.getString(cursor.getColumnIndex(DataBaseHelper.ACCOUNT_TITLE));

                accountBeanList.add(accountBean);
                accountMappingTbl.put(accountBean, cursor.getInt(cursor.getColumnIndex(DataBaseHelper.ACCOUNT_ID)));
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
