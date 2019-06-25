package com.example.hustbillbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.hustbillbook.bean.RecordBean;

public class DataBaseHelper extends SQLiteOpenHelper {

    // 数据表header
    public static final String RECORD_TYPE = "record_type";
    public static final String RECORD_TITLE = "record_title";
    public static final String RECORD_DATE = "record_date"; // 格式: yy-mm-dd
    public static final String RECORD_MONEY = "record_money";
    // 数据表名
    public static final String RECORD_TABLE = "record_table";
    public static final String ACCOUNT_TABLE = "account_table";

    public DataBaseHelper(Context context) {
        super(context, "hust_billbook", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // 创建record_table数据表
        sqLiteDatabase.execSQL("create table if not exists record_table(" +
                "inc_id integer primary key, " +
                "record_type integer, " +
                "record_title varchar, " +
                "record_date varchar, " +
                "record_money varchar)");
        //TODO 创建account_table数据表
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
        // 将键值对集合插入到record_table数据表中
        database.insert(RECORD_TABLE, null, cv);
    }

    // 查询一条记录
    public Cursor getAllRecords() {
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

    // 删除record_table数据表
    public void deleteAllRecords() {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(RECORD_TABLE, null, null);
    }

    // 删除数据库中的一条记录
    public void deleteOneRecord(int id) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("DELETE FROM " + RECORD_TABLE + " WHERE inc_id= " + id);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
