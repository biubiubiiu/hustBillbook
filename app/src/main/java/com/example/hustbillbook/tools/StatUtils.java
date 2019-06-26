package com.example.hustbillbook.tools;

import com.example.hustbillbook.bean.RecordBean;
import com.example.hustbillbook.bean.TypeRankBean;
import com.example.hustbillbook.bean.TypeViewBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class StatUtils {

    /**
     * 按类别合并数据
     * @param data -- List<RecordBean>
     * @return 支出和收入数据
     * 支出数据为 result[0]
     * 收入数据为 result[1]
     */
    public static List<TypeRankBean>[] mergeByType(List<RecordBean> data) {
        List<TypeRankBean>[] result = new ArrayList[2];
        return result;
    }

    /**
     * 按日期合并数据
     * @param data -- List<RecordBean>
     * @return 合并后的支出和收入数据
     * 支出数据为 result[0]
     * 收入数据为 result[1]
     */
    public static void mergeByDate(List<RecordBean> data,
                                   Map<String, Float> tableExpense,
                                   Map<String, Float> tableIncome) {

        if (data != null) {
            tableExpense.clear();
            tableIncome.clear();
            for (int i = 0; i < data.size(); i++) {
                RecordBean recordBean = data.get(i);
                String recordDate = recordBean.recordDate;

                // 支出用正数表示，收入用负数表示
                float recordMoney = Float.valueOf(recordBean.recordMoney);

                if (recordBean.isExpense) {
                    if (!tableExpense.containsKey(recordDate)) {
                        tableExpense.put(recordDate, recordMoney);
                    } else {
                        float originMoney = tableExpense.get(recordDate);
                        tableExpense.put(recordDate, originMoney + recordMoney);
                    }
                } else {
                    if (!tableIncome.containsKey(recordDate)) {
                        tableIncome.put(recordDate, recordMoney);
                    } else {
                        float originMoney = tableIncome.get(recordDate);
                        tableIncome.put(recordDate, originMoney + recordMoney);
                    }
                }
            }
        }
    }
}
