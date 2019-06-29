package com.example.hustbillbook.tools;


import android.util.SparseArray;

import com.example.hustbillbook.DataRepository;
import com.example.hustbillbook.bean.RecordBean;
import com.example.hustbillbook.bean.TypeRankBean;
import com.example.hustbillbook.bean.TypeViewBean;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StatUtils {

    /**
     * 按日期范围筛选数据
     * @param data  所有数据
     * @param keys  日期范围，需要与 recordBean 中的日期表示一致
     * @return  筛选结果
     */
    public static List<RecordBean> filterByDate(@NotNull List<RecordBean> data, List<String> keys) {
        List<RecordBean> result = new ArrayList<>();
        for (RecordBean bean : data) {
            if (keys.contains(bean.recordDate)) {
                result.add(bean);
            }
        }
        return result;
    }

    /**
     * 按类别合并数据
     *
     * @param data    所有数据
     * @param expense 花费
     * @param income  支出
     */
    public static void mergeByType(@NotNull List<RecordBean> data,
                                   List<TypeRankBean> expense,
                                   List<TypeRankBean> income) {

        // 先按类型合并数据
        Map<Integer, Float> expenseMap = new LinkedHashMap<>();
        Map<Integer, Float> incomeMap = new LinkedHashMap<>();

        for (RecordBean bean : data) {
            if (bean.isExpense) {
                float origin = expenseMap.getOrDefault(bean.recordType, (float) 0);
                expenseMap.put(bean.recordType, origin + Float.valueOf(bean.recordMoney));
            } else {
                float origin = incomeMap.getOrDefault(bean.recordType, (float) 0);
                incomeMap.put(bean.recordType, origin + Float.valueOf(bean.recordMoney));
            }
        }

        // 对 ArrayMap 按值进行排序
        expenseMap = MapUtils.sortByValue(expenseMap);
        incomeMap = MapUtils.sortByValue(incomeMap);

        // 构造对应的 TypeRanked 类型对象
        consBeanHelper(expenseMap, expense);
        consBeanHelper(incomeMap, income);
    }

    private static void consBeanHelper(@NotNull Map<Integer, Float> map, @NotNull List<TypeRankBean> list) {
        DataRepository r = DataRepository.getInstance();
        SparseArray<TypeViewBean> array = r.getAllRecordTypes();
        Iterator iter = map.entrySet().iterator();
        float sum = 0;

        list.clear();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            list.add(new TypeRankBean(array.get((Integer) entry.getKey()),
                    0,
                    (float) entry.getValue()));
            sum += (float) entry.getValue();
        }

        for (TypeRankBean item : list) {
            item.setRatio(item.getNumber() / sum);
        }
    }

    /**
     * 按日期合并数据
     *
     * @param data         所有数据
     * @param tableExpense <日期-支出>
     * @param tableIncome  <日期-收入>
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
