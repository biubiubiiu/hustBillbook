package com.example.hustbillbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hustbillbook.R;
import com.example.hustbillbook.SingleCommonData;
import com.example.hustbillbook.adaptor.AccountRecycleAdaptor;
import com.example.hustbillbook.bean.AccountBean;

import java.util.List;


public class SelectAccountActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<AccountBean> accountBeanList;
    AccountRecycleAdaptor adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_account);

        recyclerView = findViewById(R.id.page_account_recycle);

        accountBeanList = SingleCommonData.getAccountList();

        adapter = new AccountRecycleAdaptor(SelectAccountActivity.this, accountBeanList);
        adapter.setOnClickListener(new AccountRecycleAdaptor.OnClickListener() {
            @Override
            public void OnClick(int index) {
                if (index == accountBeanList.size() - 1) {
                    Intent intent = new Intent(SelectAccountActivity.this, AddAccountActivity.class);
                    startActivity(intent);//将请求码设为0，添加新账户
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("position", index);

                    //账户选择完成，返回
                    SelectAccountActivity.this.setResult(RESULT_OK, intent);
                    SelectAccountActivity.this.finish();
                }
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            SelectAccountActivity.this.finish();
        }
        return false;
    }
}
