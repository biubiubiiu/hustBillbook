package com.example.hustbillbook;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.hustbillbook.activity.AddRecordActivity;
import com.example.hustbillbook.activity.ChartsActivity;
import com.example.hustbillbook.activity.ViewAccountsActivity;
import com.example.hustbillbook.adaptor.RecordRecycleAdaptor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private RecordRecycleAdaptor mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SingleCommonData.initData(this);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        RecyclerView recordList = findViewById(R.id.rv_main);

        mAdapter = new RecordRecycleAdaptor(this, SingleCommonData.getRecordList());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setSmoothScrollbarEnabled(true);
        recordList.setLayoutManager(layoutManager);
//        recordList.setHasFixedSize(true);
//        recordList.setItemAnimator(new DefaultItemAnimator());
        recordList.setAdapter(mAdapter);

        mAdapter.setOnClickListener(new RecordRecycleAdaptor.OnClickListener() {
            @Override
            // 单击修改记录，跳转到下一活动
            public void OnClick(final int index) {
                Intent intent = new Intent(MainActivity.this, AddRecordActivity.class);
                intent.putExtra("handleType", 3);
                intent.putExtra("index", index);
                startActivity(intent);
            }

            @Override
            // 长按 RecyclerView 中元素删除记录
            public void OnLongClick(final int index) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Delete Record");
                builder.setMessage("Are you sure to delete the record?");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SingleCommonData.removeRecord(index);
                        mAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.create().show();
            }
        });

        // 添加新记录，跳转到下一活动
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddRecordActivity.class);
                intent.putExtra("handleType", 1);//对应第一种情况，简单的添加
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, ViewAccountsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_chart) {
            Intent intent = new Intent(MainActivity.this, ChartsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    // 在主页点击返回键后提示退出
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Quit")
                .setMessage("Are you sure to quit")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        System.exit(0);
                    }
                }).setNegativeButton("Cancel", null);
        builder.create().show();
    }
}
