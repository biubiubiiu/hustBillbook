package com.example.hustbillbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hustbillbook.R;

public class SelectExpenseTypeActivity extends AppCompatActivity {
    private int kind;

    private Button button_canyin;
    private Button button_dianying;
    private Button button_huafei;

    private Button button_jiaotong;
    private Button button_yiliao;
    private Button button_yifu;

    private Button button_fangzu;
    private Button button_shuiguo;
    private Button button_lingshi;

    private Button button_maicai;
    private Button button_shenghuoyongpin;
    private Button button_taobao;

    private Button button_zhuanzhang;
    private Button button_hongbao;
    private Button button_qita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_expense_type);

        Intent intent = getIntent();
        kind = intent.getExtras().getInt("kind");

        button_canyin = findViewById(R.id.button2);
        button_canyin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kind = 1;
                goToAddActivity();
            }
        });

        button_dianying = findViewById(R.id.button3);
        button_dianying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kind = 2;
                goToAddActivity();
            }
        });

        button_huafei = findViewById(R.id.button4);
        button_huafei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kind = 3;
                goToAddActivity();
            }
        });

        button_jiaotong = findViewById(R.id.button5);
        button_jiaotong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kind = 4;
                goToAddActivity();
            }
        });

        button_yiliao = findViewById(R.id.button6);
        button_yiliao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kind = 5;
                goToAddActivity();
            }
        });

        button_yifu = findViewById(R.id.button7);
        button_yifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kind = 6;
                goToAddActivity();
            }
        });

        button_fangzu = findViewById(R.id.button8);
        button_fangzu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kind = 7;
                goToAddActivity();
            }
        });

        button_shuiguo = findViewById(R.id.button9);
        button_shuiguo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kind = 8;
                goToAddActivity();
            }
        });

        button_lingshi = findViewById(R.id.button10);
        button_lingshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kind = 9;
                goToAddActivity();
            }
        });

        button_maicai = findViewById(R.id.button11);
        button_maicai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kind = 10;
                goToAddActivity();
            }
        });

        button_shenghuoyongpin = findViewById(R.id.button12);
        button_shenghuoyongpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kind = 11;
                goToAddActivity();
            }
        });

        button_taobao = findViewById(R.id.button13);
        button_taobao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kind = 12;
                goToAddActivity();
            }
        });

        button_zhuanzhang = findViewById(R.id.button14);
        button_zhuanzhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kind = 13;
                goToAddActivity();
            }
        });

        button_hongbao = findViewById(R.id.button15);
        button_hongbao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kind = 14;
                goToAddActivity();
            }
        });

        button_qita = findViewById(R.id.button16);
        button_qita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kind = 15;
                goToAddActivity();
            }
        });
    }


    private void goToAddActivity(){
        Intent intent =  new Intent();

        intent.putExtra("kind", kind);

        SelectExpenseTypeActivity.this.setResult(RESULT_OK, intent);

        SelectExpenseTypeActivity.this.finish();
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0){
            Intent intent =  new Intent();

            intent.putExtra("kind", kind);

            SelectExpenseTypeActivity.this.setResult(RESULT_OK, intent);

            SelectExpenseTypeActivity.this.finish();
        }
        return false;
    }
}
