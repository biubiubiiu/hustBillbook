package com.example.hustbillbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hustbillbook.R;
import com.example.hustbillbook.adaptor.AccountListAdaptor;
import com.example.hustbillbook.bean.AccountBean;

import java.util.ArrayList;
import java.util.List;

public class SelectAccountActivity extends AppCompatActivity {
    //
    private List<AccountBean> accountList = new ArrayList<>();
    //待修改

    private int numberOfAccountBean;//账户编号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_account);

        //
        initAccountBeans(); //初始化账户数据

        AccountListAdaptor adapter = new AccountListAdaptor(SelectAccountActivity.this, R.layout.item_accountlist, accountList);

        ListView listView = findViewById(R.id.listView1);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =  new Intent();
                intent.putExtra("position", position);

                SelectAccountActivity.this.setResult(RESULT_OK, intent);
                SelectAccountActivity.this.finish();
                //账户选择完成，返回
            }
        });

        LinearLayout layout = findViewById(R.id.addAccountButton);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectAccountActivity.this, AddAccountActivity.class);
                startActivity(intent);
            }
        });


    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0){

            SelectAccountActivity.this.setResult(RESULT_CANCELED);

            SelectAccountActivity.this.finish();
        }
        return false;
    }

    //
    private void initAccountBeans(){
        for(int i=0; i<10 ; i++){
            AccountBean chinaBank = new AccountBean("中国银行储蓄卡", 1, 100, "工资卡");
            accountList.add(chinaBank);

            AccountBean cash = new AccountBean("现金", 2, 50.00, "钱");
            accountList.add(cash);
        }
    }
}
