package com.example.hustbillbook;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

public class ChartsActivity extends Activity {

    private LineChartView mChart;
    private Map<String, Integer> table = new TreeMap<>();   // 使用TreeMap，避免手动排序
    private LineChartData mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linechart);
        mChart = findViewById(R.id.chart);
        mData = new LineChartData();
        List<RecordBean> allDate = (List<RecordBean>) getIntent().getSerializableExtra("record_list");
        generateValue(allDate);
        generateData();
    }

    // 对数据进行初步处理
    // 从list中读取数据，以日期为key存放到table中
    private void generateValue(List<RecordBean> allDate) {
        if (allDate != null) {
            for (int i = 0; i < allDate.size(); i++) {
                RecordBean recordBean = allDate.get(i);
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

    // 配置数据
    public void generateData() {
        List<Line> lines = new ArrayList<>();
        List<PointValue> values = new ArrayList<>();
        int indexX = 0;
        for (Integer value: table.values()) {
            values.add(new PointValue(indexX, value));
            indexX++;
        }

        Line line = new Line(values);
        line.setColor(ChartUtils.COLORS[0]);
        line.setShape(ValueShape.CIRCLE);
        line.setPointColor(ChartUtils.COLORS[1]);
        lines.add(line);

        mData = new LineChartData(lines);
        mChart.setLineChartData(mData);
    }
}
