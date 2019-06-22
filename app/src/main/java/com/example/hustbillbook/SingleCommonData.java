package com.example.hustbillbook;

import android.content.Context;
import android.database.Cursor;

import com.example.hustbillbook.bean.RecordBean;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

/*
 使用单例模式设计
 存放了多个活动类需要访问和修改的公共数据
 */
public class SingleCommonData {

    // 存放所有账单记录
    private static List<RecordBean> recordBeanList = new ArrayList<>();

    // 让构造函数为private，这样该类就不会被实例化
    private SingleCommonData() {}

    // 获取账单记录
    @Contract(pure = true)
    public static List<RecordBean> getRecordList() {
        return recordBeanList;
    }

    public static void remove(int positon) {
        recordBeanList.remove(positon);
    }

    public static void add(RecordBean recordBean) { recordBeanList.add(recordBean); }

    public static void initData(Context context) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        // 每次启动时删除数据库中所有内容，测试时使用
        dataBaseHelper.deleteAllRecords();
        // 生成假数据，测试时使用
        for (int i = 0; i < 2; i++) {
            RecordBean recordBean = new RecordBean();
            recordBean.recordDate = "2019-6-19";
            recordBean.recordTitle = "test" + i;
            recordBean.recordMoney = "1000";
            //            mRecordBeanList.add(recordBean；
            // 将假数据插入到数据库中
            dataBaseHelper.insertRecord(recordBean);
        }
        // 通过数据库将查询数据，插入到List容器中
        Cursor cursor = dataBaseHelper.getAllRecords();
        // 先清空一下数据，小心驶得万年船
        recordBeanList.clear();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                RecordBean recordBean = new RecordBean();
                recordBean.recordTitle = cursor.getString(cursor.getColumnIndex(DataBaseHelper.RECORD_TITLE));
                recordBean.recordDate= cursor.getString(cursor.getColumnIndex(DataBaseHelper.RECORD_DATE));
                recordBean.recordMoney = cursor.getString(cursor.getColumnIndex(DataBaseHelper.RECORD_MONEY));
                recordBeanList.add(recordBean);
            }
            cursor.close();
        }
    }
}
