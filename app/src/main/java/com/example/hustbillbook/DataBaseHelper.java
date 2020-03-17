package com.example.hustbillbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.hustbillbook.bean.AccountBean;
import com.example.hustbillbook.bean.RecordBean;

public class DataBaseHelper extends SQLiteOpenHelper {

    // 账单数据表 header
    public static final String INC_ID = "inc_id";
    public static final String RECORD_TYPE = "record_type";
    public static final String RECORD_TITLE = "record_title";
    public static final String RECORD_DATE = "record_date"; // 格式: yy-mm-dd
    public static final String RECORD_MONEY = "record_money";
    public static final String RECORD_ACCOUNT = "record_account";
    public static final String RECORD_ISEXPENSE = "record_isExpense";

    // 账户数据表 header
    public static final String ACCOUNT_ID = "account_id";
    public static final String ACCOUNT_NAME = "account_name";
    public static final String ACCOUNT_TYPE = "account_type";
    public static final String ACCOUNT_MONEY = "account_money";
    public static final String ACCOUNT_TITLE = "account_title";

    // 数据表名
    private final String RECORD_TABLE = "record_table";
    private final String ACCOUNT_TABLE = "account_table";

    public DataBaseHelper(Context context) {
        super(context, "hust_billbook_2", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // 创建record_table数据表
        sqLiteDatabase.execSQL("create table if not exists record_table(" +
                "inc_id integer primary key, " +    // 主键 账单id
                "record_isExpense boolean, " +      // 是否为支出
                "record_type integer, " +           // 账单类型
                "record_account integer, " +        // 外键 账户id
                "record_title varchar, " +          // 账单标题
                "record_date varchar, " +           // 账单时间
                "record_money varchar)");           // 账单金额

        //创建account_table数据表
        sqLiteDatabase.execSQL("create table if not exists account_table(" +
                "account_id integer primary key, " +
                "account_type integer, " +
                "account_name varchar, " +
                "account_money varchar, " +
                "account_title varchar)");
    }

    // 插入一条记录
    public void insertRecord(RecordBean recordBean) {
        SQLiteDatabase database = getWritableDatabase();
        // 创建一个键值对集合
        ContentValues cv = new ContentValues();
        cv.put(RECORD_TYPE, recordBean.recordType);
        cv.put(RECORD_TITLE, recordBean.recordTitle);
        cv.put(RECORD_DATE, recordBean.recordDate);
        cv.put(RECORD_MONEY, recordBean.recordMoney);
        cv.put(RECORD_ACCOUNT, recordBean.recordAccount);
        cv.put(RECORD_ISEXPENSE, recordBean.isExpense);
        // 将键值对集合插入到record_table数据表中
        database.insert(RECORD_TABLE, null, cv);
    }

    //新建一个账户
    public void insertAccount(AccountBean accountBean) {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(ACCOUNT_TYPE, accountBean.accountType);
        cv.put(ACCOUNT_NAME, accountBean.accountName);
        cv.put(ACCOUNT_MONEY, accountBean.accountMoney);
        cv.put(ACCOUNT_TITLE, accountBean.accountTitle);

        database.insert(ACCOUNT_TABLE, null, cv);
    }

    // 查询排序后记录
    public Cursor getSortedRecords() {
        SQLiteDatabase database = getWritableDatabase();

        // SELECT * FROM record_table ORDER BY record_date ASC
        return database.query(RECORD_TABLE,
                null,
                null,
                null,
                null,
                null,
                RECORD_DATE + " ASC");    // 此处应当有空格，否则应用将无法启动
    }

    // 查询排序后账户
    public Cursor getSortedAccounts() {
        SQLiteDatabase database = getWritableDatabase();

        // SELECT * FROM account_table ORDER BY account_id ASC
        return database.query(ACCOUNT_TABLE,
                null,
                null,
                null,
                null,
                null,
                ACCOUNT_ID + " ASC");
    }

    // 删除record_table中所有记录
    public void deleteAllRecords() {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(RECORD_TABLE, null, null);
    }

    // 删除account_table中所有记录
    public void deleteAllAccounts() {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(ACCOUNT_TABLE, null, null);
    }

    // 删除数据库中的一条记录
    public void deleteOneRecord(int id) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("DELETE FROM " + RECORD_TABLE + " WHERE inc_id= " + id);
    }

    //删除数据库中的一个账户
    public void deleteOneAccount(int id) {
        SQLiteDatabase database = getWritableDatabase();
        /*modified on 6/30*/
        id -= 1;
        //先删除该账户下的所有记录
        database.execSQL("DELETE FROM " + RECORD_TABLE + " WHERE record_account=" + id);

        id += 1;
        //再在账户表中删除该账户
        database.execSQL("DELETE FROM " + ACCOUNT_TABLE + " WHERE account_id= " + id);
    }


    //更新账户余额
    public void updateAccountMoney(int index, String moneyLeft) {
        SQLiteDatabase database = getWritableDatabase();

        database.execSQL("UPDATE account_table SET account_money='" + moneyLeft + "' WHERE account_id= " + index);
    }

    //更新账户信息
    public void updateAccountInfo(String name, String money, String note, int id) {
        SQLiteDatabase database = getWritableDatabase();

        database.execSQL("UPDATE account_table SET account_name='" + name +
                "', account_money='" + money +
                "', account_title='" + note +
                "' WHERE account_id = " + id);

    }

    //更新记录信息
    public void updateRecordInfo(String recordDate,
                                 String recordMoney,
                                 String recordTitle,
                                 boolean isExpense,
                                 int recordType,
                                 int recordAccount,
                                 int id) {

        if (id >= 0) {
            SQLiteDatabase database = getWritableDatabase();

            database.execSQL("UPDATE record_table SET record_date='" + recordDate +
                    "', record_money='" + recordMoney +
                    "', record_title='" + recordTitle +
                    "', record_isExpense='" + isExpense +
                    "', record_type= " + recordType +
                    ", record_account= " + recordAccount +
                    " WHERE inc_id= " + id);

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
