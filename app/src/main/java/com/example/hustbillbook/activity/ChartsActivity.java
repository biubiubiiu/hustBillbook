package com.example.hustbillbook.activity;

import com.example.hustbillbook.R;
import com.example.hustbillbook.SingleCommonData;
import com.example.hustbillbook.bean.RecordBean;
import com.example.hustbillbook.tools.CalenderUtils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.ColumnChartView;

public class ChartsActivity extends Activity implements View.OnClickListener{

    private ColumnChartView mChart;
    private Map<String, Float> table = new TreeMap<>();   // 使用TreeMap，避免手动排序
    private ColumnChartData mData;

    private float maxValue; //用于保存Y轴的最大值，用于设置Y轴的上下限
    List<Column> columnList;//柱子列表
    List<AxisValue> mAxisXValues;

    private TextView weekTv;
    private TextView monthTv;
    private TextView yearTv;

    private enum Page {
        Week, Month, Year
    }
    private Page currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_columnchart);

        mChart = findViewById(R.id.columnchart);
        mData = new ColumnChartData();
        columnList = new ArrayList<>();
        mAxisXValues = new ArrayList<>();

        weekTv = findViewById(R.id.tv_week_chart);
        monthTv = findViewById(R.id.tv_month_chart);
        yearTv = findViewById(R.id.tv_year_chart);

        weekTv.setOnClickListener(this);
        monthTv.setOnClickListener(this);
        yearTv.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        List<RecordBean> allData = SingleCommonData.getRecordList();

        weekTv.setSelected(true);
        currentPage = Page.Week;
        generateValue(allData);
        generateWeekChart();

        Log.d("chart", "chart activity started!");
    }

    // 对数据进行初步处理
    // 从list中读取数据，按日期合并花费，存放到table中
    private void generateValue(List<RecordBean> allData) {
        if (allData != null) {
            for (int i = 0; i < allData.size(); i++) {
                RecordBean recordBean = allData.get(i);
                String recordDate = recordBean.recordDate;

                // 支出用正数表示，收入用负数表示
                float recordMoney = Float.valueOf(recordBean.recordMoney);
                recordMoney *= (recordBean.isExpense ? 1 : -1);

                if (!table.containsKey(recordDate)) {
                    table.put(recordDate, recordMoney);
                } else {
                    float originMoney = table.get(recordDate);
                    table.put(recordDate, originMoney + recordMoney);
                }
            }
        }
    }

    // 生成账单周报
    public void generateWeekChart() {
        // 先清空图表内容
        clearData();
        // 获取当前周所有日期
        List<String> weekDays= CalenderUtils.getWeekDays();
        // 填充坐标轴
        fillWeeklyAxisXValues(weekDays);
        // 填充坐标数据
        fillPointValues(weekDays, false);
        // 生成图表
        generateChart();
    }

    // 生成月度账单分析
    public void generateMonthChart() {
        clearData();
        List<String> monthDays = CalenderUtils.getMonthDays();
        fillMonthlyAxisXValues(monthDays);
        fillPointValues(monthDays, false);
        generateChart();
    }

    // 生成年度账单分析
    public void generateYearChart() {
        clearData();
        List<String> yearDays = CalenderUtils.getYearDays();
        fillYearlyAxisXValues();
        fillPointValues(yearDays, true);
        generateChart();
    }

    private void generateChart() {
        Axis axisX = new Axis(mAxisXValues);//x轴
        Axis axisY = new Axis();//y轴

        //是否显示网格线
        axisY.setHasLines(true);
        axisY.hasLines();
        axisY.setTextColor(Color.BLACK);
        axisY.setTextSize(12);
        axisY.setMaxLabelChars(6);

        axisX.setTextColor(Color.BLACK);
        axisX.setTextSize(12);
        axisX.setHasLines(false);

        List<AxisValue> values = new ArrayList<>();
        if (maxValue == 0){
            int[] dataY = {-100,-75,-50,-25,0,25,50,75,100};
            for (int value: dataY) {
                values.add(new AxisValue(value));
            }
            axisY.setValues(values);
            maxValue = 100;
        } else {
            int m = (int)Math.ceil(maxValue/1.0);
            maxValue = m;
            m = -m;
            int[] dataY = { m, m*3/4, m*2/4, m/4, 0, Math.abs(m/4), Math.abs(m*2/4), Math.abs(m*3/4), Math.abs(m) };
            for (int value: dataY)
                values.add(new AxisValue(value));
            axisY.setValues(values);
        }

        mData = new ColumnChartData(columnList);
        mData.setAxisXBottom(axisX);
        mData.setAxisYLeft(axisY);
        mData.setFillRatio(0.5F);//参数即为柱子宽度的倍数，范围只能在0到1之间
        //设置显示的数据背景、字体颜色
        mData.setValueLabelsTextColor(Color.BLACK);// 设置数据文字颜色
        mData.setValueLabelBackgroundEnabled(false);
        mData.setStacked(true);
        mChart.setColumnChartData(mData);

        Viewport v = new Viewport(mChart.getMaximumViewport());
        v.top = maxValue;
        v.bottom = -maxValue;
        mChart.setMaximumViewport(v);
        mChart.setCurrentViewport(v);
    }

    private void fillWeeklyAxisXValues(@NotNull List<String> days) {
        // 获取今天日期
        String today = CalenderUtils.getCurrentDate();
        for (int i = 0; i < days.size(); i++) {
            String day = days.get(i);
            if (day.equals(today)) {
                mAxisXValues.add(new AxisValue(i).setLabel("今天"));
            } else {
                mAxisXValues.add(new AxisValue(i).setLabel(
                        day.substring(5).replace('-', '.')));
            }
        }
    }

    private void fillMonthlyAxisXValues(@NotNull List<String> days) {
        for (int i = 0; i < days.size(); i++) {
            if (i % 5 == 0) {
                String day = days.get(i).split("-")[2];
                mAxisXValues.add(new AxisValue(i).setLabel(day));
            }
        }
    }

    private void fillYearlyAxisXValues() {
        for (int i = 1; i <= 12; i++) {
            if (i == 1 || i % 3 == 0) {
                String value = i + "月";
                mAxisXValues.add(new AxisValue(i - 1).setLabel(value));
            }
        }
    }

    private void fillPointValues(List<String> days, boolean isYearReport){
        float currentValue;//记录当前值，与maxValue比较
        List<SubcolumnValue> subcolumnValueList;

        if (!isYearReport) {
            for (int i = 0; i < days.size(); i++){
                subcolumnValueList = new ArrayList<>();
                SubcolumnValue subIncome = new SubcolumnValue();
                SubcolumnValue subExpense = new SubcolumnValue();

                currentValue = table.getOrDefault(days.get(i) ,(float)0.0);
                if (Math.abs(currentValue) > maxValue)
                    maxValue = currentValue;

                // 支出，为正，显示 在上方
                if (currentValue >= 0) {
                    subExpense.setValue(currentValue);
                    subExpense.setColor(Color.RED);
                } else {    // 收入显示在下方
                    subIncome.setValue(-currentValue);
                    subIncome.setColor(Color.GREEN);
                }

                subcolumnValueList.add(subExpense);
                subcolumnValueList.add(subIncome);

                columnList.add(new Column(subcolumnValueList));
            }
        } else {
            float sumExpense = 0;
            float sumIncome = 0;
            String currMonth = "1";

            for (int i = 0; i < days.size(); i++) {
                String day = days.get(i);
                String month = day.split("-")[1];
                // 如果已经统计完一个月份的数据
                if (!month.equals(currMonth) || i == days.size() - 1) {
                    // 添加新列
                    List<SubcolumnValue> list = new ArrayList<>();
                    SubcolumnValue subExpense = new SubcolumnValue();
                    SubcolumnValue subIncome = new SubcolumnValue();

                    subExpense.setValue(sumExpense);
                    subExpense.setColor(Color.RED);
                    list.add(subExpense);

                    subIncome.setValue(-sumIncome);
                    subIncome.setColor(Color.GREEN);
                    list.add(subIncome);

                    maxValue = Math.max(maxValue, Math.max(sumExpense, -sumIncome));

                    Column column = new Column(list);
                    columnList.add(column);

                    // 处理下一个月份
                    currMonth = month;
                    sumExpense = 0;
                    sumIncome = 0;
                }
                sumExpense += table.getOrDefault(day, (float)0.0);
                sumIncome += table.getOrDefault(day, (float)0.0);
            }
        }
    }

    private void clearData() {
        maxValue = 0;
        columnList.clear();
        mAxisXValues.clear();
    }

    // TODO 使用继承方式来去除switch代码
    @Override
    public void onClick(View view) {
        weekTv.setSelected(false);
        monthTv.setSelected(false);
        yearTv.setSelected(false);
        switch (view.getId()) {
            case R.id.tv_week_chart:
                if (currentPage != Page.Week) generateWeekChart();
                currentPage = Page.Week;
                weekTv.setSelected(true);
                break;
            case R.id.tv_month_chart:
                if (currentPage != Page.Month) generateMonthChart();
                currentPage = Page.Month;
                monthTv.setSelected(true);
                break;
            case R.id.tv_year_chart:
                if (currentPage != Page.Year) generateYearChart();
                currentPage = Page.Year;
                yearTv.setSelected(true);
                break;
        }
    }
}
