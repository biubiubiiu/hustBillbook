package com.example.hustbillbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hustbillbook.R;
import com.example.hustbillbook.SingleCommonData;
import com.example.hustbillbook.adaptor.AccountRecycleAdaptor;
import com.example.hustbillbook.bean.AccountBean;

import java.util.List;

public class ViewAccountsActivity extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private List<AccountBean> accountBeanList;
    private AccountRecycleAdaptor adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_accounts);

        TextView title = findViewById(R.id.textView5);
        title.setText("查看账户");

        recyclerView = findViewById(R.id.page_account_recycle);

        ImageView btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        accountBeanList = SingleCommonData.getAccountList();

        adapter = new AccountRecycleAdaptor(ViewAccountsActivity.this, accountBeanList);
        adapter.setOnClickListener(new AccountRecycleAdaptor.OnClickListener() {
            @Override
            public void OnClick(int position) {
                Intent intent;

                //如果点击了最后一项，则表示选择添加账户
                if (position == SingleCommonData.getAccountList().size() - 1) {
                    intent = new Intent(ViewAccountsActivity.this, AddAccountActivity.class);
                    startActivityForResult(intent, 0);// 请求码设为0
                } else {
                    intent = new Intent(ViewAccountsActivity.this, ViewSpecificAccountActivity.class);
                    intent.putExtra("account_id", position);
                    startActivity(intent);
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_back) {
            this.finish();
        }
    }
}
