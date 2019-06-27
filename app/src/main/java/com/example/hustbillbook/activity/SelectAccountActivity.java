package com.example.hustbillbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hustbillbook.R;
import com.example.hustbillbook.SingleCommonData;
import com.example.hustbillbook.adaptor.AccountListAdaptor;


public class SelectAccountActivity extends AppCompatActivity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_account);

        listView = findViewById(R.id.listView1);

        AccountListAdaptor adapter = new AccountListAdaptor(SelectAccountActivity.this, R.layout.item_accountlist, SingleCommonData.getAccountList());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;

                if (position == SingleCommonData.getAccountList().size() - 1){
                    intent = new Intent(SelectAccountActivity.this, AddAccountActivity.class);

                    startActivityForResult(intent, 0);//将请求码设为0，添加新账户
                }
                else {
                    intent = new Intent();
                    intent.putExtra("position", position);

                    SelectAccountActivity.this.setResult(RESULT_OK, intent);
                    SelectAccountActivity.this.finish();
                    //账户选择完成，返回
                }

            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 0){
            AccountListAdaptor adapter = new AccountListAdaptor(SelectAccountActivity.this, R.layout.item_accountlist, SingleCommonData.getAccountList());
            listView.setAdapter(adapter);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0){

            SelectAccountActivity.this.setResult(RESULT_CANCELED);

            SelectAccountActivity.this.finish();
        }
        return false;
    }

}
