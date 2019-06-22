package com.example.hustbillbook.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hustbillbook.R;

public class AddAccountActivity extends AppCompatActivity {
    private Toast toast;
    private int screenHeight;

    private EditText accountName;
    private EditText accountMoney;
    private EditText accountNote;

    private LinearLayout type_cash;
    private LinearLayout type_card;
    private LinearLayout type_credit;
    private LinearLayout type_alipay;
    private LinearLayout type_wechatpay;
    private LinearLayout type_jdpay;
    private LinearLayout type_qita;

    private Button ok;

    private String name;
    private String note;
    private double money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        WindowManager wm = (WindowManager) AddAccountActivity.this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        float density = dm.density;
        screenHeight = (int) (height/density);
        //获取屏幕高度

        accountName = findViewById(R.id.editText5);
        accountMoney = findViewById(R.id.editText6);
        accountNote = findViewById(R.id.editText7);

        type_cash = findViewById(R.id.linearLayout1);
        type_card = findViewById(R.id.linearLayout2);
        type_credit = findViewById(R.id.linearLayout3);
        type_alipay = findViewById(R.id.linearLayout4);
        type_wechatpay = findViewById(R.id.linearLayout5);
        type_jdpay = findViewById(R.id.linearLayout6);
        type_qita = findViewById(R.id.linearLayout7);

        ok = findViewById(R.id.button24);

        ok.setOnClickListener(new View.OnClickListener() {  //设置确认健点击事件
            @Override
            public void onClick(View view) {
                if(accountName.getText().toString().length() == 0){
                    toast = Toast.makeText(getApplicationContext(), "请输入账户名称！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, screenHeight / 4);      //设置toast位置

                    LinearLayout linearLayout = (LinearLayout) toast.getView();
                    TextView messageTextView = (TextView)linearLayout.getChildAt(0);
                    messageTextView.setTextSize(20);

                    toast.show();
                }
            }
        });



    }
}
