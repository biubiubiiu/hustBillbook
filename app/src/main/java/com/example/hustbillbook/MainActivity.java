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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.PopupMenu;

public class MainActivity extends AppCompatActivity {

    private DataBaseHelper mDataBaseHelper;
    private RecordRecycleAdaptor mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SingleCommonData.initData(this);

        mDataBaseHelper = new DataBaseHelper(this);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        RecyclerView recordList = findViewById(R.id.rv_main);

        mAdapter = new RecordRecycleAdaptor(this, SingleCommonData.getRecordList());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recordList.setLayoutManager(layoutManager);
        recordList.setAdapter(mAdapter);

        // 单击 RecyclerView 中元素删除记录
        mAdapter.setOnClickListener(new RecordRecycleAdaptor.OnClickListener() {
            @Override
            public void OnClick(final int index) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Delete Record");
                builder.setMessage("Are you sure to delete the record?");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDataBaseHelper.deleteOneRecord(index);
                        SingleCommonData.removeRecord(index);
                        mAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.create().show();
            }
        });

        // 添加新记录
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddRecordActivity.class);
                startActivity(intent);
            }
        });

        ImageView imageView = findViewById(R.id.popupMenu);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(view);
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

    //设置左下角弹出式菜单
    public void showMenu(View v){
        PopupMenu popupMenu = new PopupMenu(this,v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_main,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_chart:
                        Intent intent = new Intent(MainActivity.this, ChartsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_settings:
                        intent = new Intent(MainActivity.this, ViewAccountsActivity.class);
                        startActivity(intent);
                    default:break;
                }
                return true;
            }
        });
        popupMenu.show();
    }
}
