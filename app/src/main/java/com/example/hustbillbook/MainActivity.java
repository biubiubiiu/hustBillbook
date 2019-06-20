package com.example.hustbillbook;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<RecordBean> mRecordBeanList;
    private DataBaseHelper mDataBaseHelper;
    private RecordListAdaptor mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDataBaseHelper = new DataBaseHelper(this);
        mRecordBeanList = new ArrayList<>();
        
        initTestData();

        ListView recordList = findViewById(R.id.lv_main);
        mAdapter = new RecordListAdaptor(this, mRecordBeanList);
        recordList.setAdapter(mAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            // 点击fab按钮后，转移到viewDialog中
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                // 创建viewDialog窗口
                View viewDialog = inflater.inflate(R.layout.view_add_new_record, null);
                // 根据id找到对应的控件
                // 需要声明为final，否则无法在内部类viewDialog中引用
                final EditText title = viewDialog.findViewById(R.id.et_record_title);
                final EditText money = viewDialog.findViewById(R.id.et_record_money);
                final DatePicker date = viewDialog.findViewById(R.id.dp_record_date);
                builder.setView(viewDialog);
                builder.setTitle("New Cost");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    // 设置回调事件
                    @Override
                    // 将新数据插入到数据库中
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RecordBean recordBean = new RecordBean();
                        recordBean.recordTitle = title.getText().toString();
                        recordBean.recordMoney = money.getText().toString();
                        // date.getMonth()返回值为0-11
                        recordBean.recordDate = date.getYear() + "-" + (date.getMonth() + 1)
                                + "-" + date.getDayOfMonth();
                        // 将新数据写入数据库
                        mDataBaseHelper.insertRecord(recordBean);
                        // 将新数据加入list
                        mRecordBeanList.add(recordBean);
                        // 更新ListView的数据
                        mAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.create().show();
            }
        });
    }

    // 【测试用】生成假数据并插入到数据库中
    private void initTestData() {
        // 先清空数据表中的数据
        mDataBaseHelper.deleteDatabase();
        // 生成假数据
        for (int i = 0; i < 2; i++) {
            RecordBean recordBean = new RecordBean();
            recordBean.recordDate = "6-19";
            recordBean.recordTitle = "test" + i;
            recordBean.recordMoney = "1000";
            //            mRecordBeanList.add(recordBean；
            // 将假数据插入到数据库中
            mDataBaseHelper.insertRecord(recordBean);
        }
        // 通过数据库将查询数据，插入到List容器中
        Cursor cursor = mDataBaseHelper.getAllCostData();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                RecordBean recordBean = new RecordBean();
                recordBean.recordTitle = cursor.getString(cursor.getColumnIndex(DataBaseHelper.RECORD_TITLE));
                recordBean.recordDate= cursor.getString(cursor.getColumnIndex(DataBaseHelper.RECORD_DATE));
                recordBean.recordMoney = cursor.getString(cursor.getColumnIndex(DataBaseHelper.RECORD_MONEY));
                mRecordBeanList.add(recordBean);
            }
            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_chart) {
            Intent intent = new Intent(MainActivity.this, ChartsActivity.class);
            // key: record_list
            // value: mRecordBeanList
            // 将mRecordBeanList序列化并传递到intent activity
            intent.putExtra("record_list", (Serializable) mRecordBeanList);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
