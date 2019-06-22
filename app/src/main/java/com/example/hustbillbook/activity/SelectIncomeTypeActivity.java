package com.example.hustbillbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hustbillbook.R;

public class SelectIncomeTypeActivity extends AppCompatActivity {
    private int kind;

    private Button button_gongzi;
    private Button button_linghuaqian;
    private Button button_jianzhi;
    private Button button_shenghuofei;
    private Button button_hongbao;
    private Button button_touzishouyi;
    private Button button_jiangjin;
    private Button button_qita;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_income_type);

        Intent intent = getIntent();
        kind = intent.getExtras().getInt("kind");

        button_gongzi = (Button)findViewById(R.id.button16);
        button_gongzi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kind = 50;
                goToAddActivity();
            }
        });

        button_linghuaqian = (Button)findViewById(R.id.button17);
        button_linghuaqian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kind = 51;
                goToAddActivity();
            }
        });

        button_jianzhi = (Button)findViewById(R.id.button18);
        button_jianzhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kind = 52;
                goToAddActivity();
            }
        });

        button_shenghuofei = (Button)findViewById(R.id.button19);
        button_shenghuofei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kind = 53;
                goToAddActivity();
            }
        });

        button_hongbao = (Button)findViewById(R.id.button20);
        button_hongbao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kind = 54;
                goToAddActivity();
            }
        });

        button_touzishouyi = findViewById(R.id.button21);
        button_touzishouyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kind = 55;
                goToAddActivity();
            }
        });

        button_jiangjin = findViewById(R.id.button22);
        button_jiangjin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kind = 56;
                goToAddActivity();
            }
        });

        button_qita = findViewById(R.id.button23);
        button_qita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kind = 57;
                goToAddActivity();
            }
        });

    }

    private void goToAddActivity(){
        Intent intent =  new Intent();

        intent.putExtra("kind", kind);

        SelectIncomeTypeActivity.this.setResult(RESULT_OK, intent);

        SelectIncomeTypeActivity.this.finish();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            Intent intent =  new Intent();

            intent.putExtra("kind", kind);

            SelectIncomeTypeActivity.this.setResult(RESULT_OK, intent);

            SelectIncomeTypeActivity.this.finish();
        }
        return false;
    }
}
