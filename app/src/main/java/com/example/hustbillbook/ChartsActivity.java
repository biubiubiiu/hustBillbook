package com.example.hustbillbook;

import com.example.hustbillbook.Utils.CalenderUtils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

public class ChartsActivity extends Activity implements View.OnClickListener{

    private LineChartView mChart;
    private Map<String, Integer> table = new TreeMap<>();   // 使用TreeMap，避免手动排序
    private LineChartData mData;

    List<Line> lines;
    List<PointValue> values;
    List<AxisValue> mAxisXValues;

    private TextView weekTv;
    private TextView monthTv;
    private TextView yearTv;

    private enum Page {
        Week, Month, Year;
    }
    private Page currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linechart);

        mChart = findViewById(R.id.linechart);
        mData = new LineChartData();
        List<RecordBean> allData = (List<RecordBean>) getIntent().getSerializableExtra("record_list");
        lines = new ArrayList<>();
        values = new ArrayList<>();
        mAxisXValues = new ArrayList<>();

        weekTv = findViewById(R.id.tv_week_chart);
        monthTv = findViewById(R.id.tv_month_chart);
        yearTv = findViewById(R.id.tv_year_chart);

        weekTv.setOnClickListener(this);
        monthTv.setOnClickListener(this);
        yearTv.setOnClickListener(this);

        weekTv.setSelected(true);
        currentPage = Page.Week;
        generateValue(allData);
        generateWeekChart();
    }

    // 对数据进行初步处理
    // 从list中读取数据，按日期合并花费，存放到table中
    private void generateValue(List<RecordBean> allData) {
        if (allData != null) {
            for (int i = 0; i < allData.size(); i++) {
                RecordBean recordBean = allData.get(i);
                String recordDate = recordBean.recordDate;
                int recordMoney = Integer.parseInt(recordBean.recordMoney);
                if (!table.containsKey(recordDate)) {
                    table.put(recordDate, recordMoney);
                } else {
                    int originMoney = table.get(recordDate);
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
        Line line = new Line(values);
        line.setColor(ChartUtils.COLORS[0]);
        line.setShape(ValueShape.CIRCLE);
        line.setPointColor(ChartUtils.COLORS[1]);
        line.setPointRadius(5);
        lines.add(line);

        Axis axisX = new Axis();
        axisX.setTextColor(Color.BLACK);
        axisX.setValues(mAxisXValues);
        axisX.setHasLines(false);

        mData = new LineChartData(lines);
        mData.setAxisXBottom(axisX);

        mChart.setLineChartData(mData);
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

    private final void fillPointValues(List<String> days, boolean isYearReport){
        if (!isYearReport) {
            for (int i = 0; i < days.size(); i++) {
                String day = days.get(i);
                // getOrDefault支持API24及以上版本
                values.add(new PointValue(i, table.getOrDefault(day, 0)));
            }
        } else {
            int sum = 0;
            String currMonth = "1";
            for (int i = 0; i < days.size(); i++) {
                String day = days.get(i);
                String month = day.split("-")[1];
                // 如果已经统计完一个月份的数据
                if (!month.equals(currMonth)) {
                    // 添加数据
                    values.add(new PointValue(Integer.parseInt(currMonth), sum));
                    // 处理下一个月份
                    currMonth = month;
                    sum = 0;
                }
                sum += table.getOrDefault(day, 0);
            }
            values.add(new PointValue(Integer.parseInt(currMonth), sum));
        }
    }

    private final void clearData() {
        lines.clear();
        values.clear();
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
