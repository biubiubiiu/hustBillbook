package com.example.hustbillbook.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hustbillbook.R;
import com.example.hustbillbook.SingleCommonData;
import com.example.hustbillbook.bean.AccountBean;

public class AddAccountActivity extends AppCompatActivity implements View.OnClickListener{
    final int MAX_INTEGER_LENGTH = 8;
    final int MAX_DECIMAL_LENGTH = 2;
    //设置账户金额整数部分最大8位，小数部分最大2位

    private Toast toast;

    private EditText accName;
    private EditText accMoney;
    private EditText accNote;

    //用于设置图片的背景是否变黑
    private TextView type_cash;
    private TextView type_card;
    private TextView type_credit;
    private TextView type_alipay;
    private TextView type_wechatpay;
    private TextView type_jdpay;
    private TextView type_qita;

    private ImageView btn_back;

    private String name;
    private String note;
    private String money;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        accName = findViewById(R.id.et_accountName);
        accMoney = findViewById(R.id.et_accountMoney);
        accNote = findViewById(R.id.et_accountNote);

        type_cash = findViewById(R.id.tv_xianjin);
        type_card = findViewById(R.id.tv_chuxuka);
        type_credit = findViewById(R.id.tv_xinyongka);
        type_alipay = findViewById(R.id.tv_zhifubao);
        type_wechatpay = findViewById(R.id.tv_weixinzhifu);
        type_jdpay = findViewById(R.id.tv_jingdongjinrong);
        type_qita = findViewById(R.id.tv_qita);

        btn_back = findViewById(R.id.btn_back);

        type_cash.setOnClickListener(this);
        type_card.setOnClickListener(this);
        type_credit.setOnClickListener(this);
        type_alipay.setOnClickListener(this);
        type_wechatpay.setOnClickListener(this);
        type_jdpay.setOnClickListener(this);
        type_qita.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        //默认选中现金账户类别
        type_cash.setSelected(true);
        //设置类别
        type = AccountBean.Type.XIANJIN.getId();

        //设置确认按钮的监听事件
        Button ok = findViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {  //设置确认健点击事件
            @Override
            public void onClick(View view) {
                if (accName.getText().toString().length() == 0) {         //账户名不为空
                    toast = Toast.makeText(getApplicationContext(), "请输入账户名称！", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (accName.getText().toString().length() > 20) {   //账户名长度不超过20个字符
                    toast = Toast.makeText(getApplicationContext(), "账户名不超过20个字符！", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (accMoney.getText().toString().length() == 0) {  //账户金额不为空
                    toast = Toast.makeText(getApplicationContext(), "请输入账户金额！", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    name = accName.getText().toString();
                    money = accMoney.getText().toString();
                    note = accNote.getText().toString();

                    //type值已取得
                    AccountBean accountBean = new AccountBean();
                    accountBean.accountName = name;
                    accountBean.accountMoney = money;
                    accountBean.accountTitle = note;
                    accountBean.accountType = type;

                    SingleCommonData.addAccount(accountBean);

                    toast = Toast.makeText(getApplicationContext(), "账户新建成功！", Toast.LENGTH_SHORT);
                    toast.show();

                    //结束活动，返回调用者
                    finish();
                }
            }
        });

        //账户金额输入检查函数
        accMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String curr = accMoney.getText().toString();

                if (curr.equals(".")) {   //一开始只输入一个小数点，自动补全为0.
                    editable.replace(0, 1, "0.");
                }
                if (curr.equals("00")) {  //不允许连续输入0
                    editable.delete(1, 2);
                }
                if (curr.length() >= 2 && curr.charAt(0) == '0' && curr.charAt(1) != '.') {//删掉整数部分开头的0
                    editable.delete(0, 1);
                }

                int dotIndex = curr.indexOf(".");//小数点所在位置
                if (dotIndex == -1) {    //此时没有小数，判断整数部分
                    if (curr.length() > MAX_INTEGER_LENGTH)
                        editable.delete(curr.length() - 1, curr.length());
                } else {
                    if (curr.length() - 1 - dotIndex > MAX_DECIMAL_LENGTH)
                        editable.delete(dotIndex + 3, dotIndex + 4);
                }
            }
        });
    }

    //先将全部设为false，再将被选中的设为true
    private void setStatusAllFalse() {
        type_cash.setSelected(false);
        type_card.setSelected(false);
        type_credit.setSelected(false);
        type_alipay.setSelected(false);
        type_wechatpay.setSelected(false);
        type_jdpay.setSelected(false);
        type_qita.setSelected(false);
    }

    //新增处理返回键
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            AddAccountActivity.this.setResult(RESULT_CANCELED);
            AddAccountActivity.this.finish();
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_xianjin:
                setStatusAllFalse();
                type_cash.setSelected(true);
                type = AccountBean.Type.XIANJIN.getId();
                break;
            case R.id.tv_chuxuka:
                setStatusAllFalse();
                type_card.setSelected(true);
                type = AccountBean.Type.CHUXUKA.getId();
                break;
            case R.id.tv_xinyongka:
                setStatusAllFalse();
                type_credit.setSelected(true);
                type = AccountBean.Type.XINYONGKA.getId();
                break;
            case R.id.tv_zhifubao:
                setStatusAllFalse();
                type_alipay.setSelected(true);
                type = AccountBean.Type.ZHIFUBAO.getId();
                break;
            case R.id.tv_weixinzhifu:
                setStatusAllFalse();
                type_wechatpay.setSelected(true);
                type = AccountBean.Type.WEIXINZHIFU.getId();
                break;
            case R.id.tv_jingdongjinrong:
                setStatusAllFalse();
                type_jdpay.setSelected(true);
                type = AccountBean.Type.JINGDONG.getId();
                break;
            case R.id.tv_qita:
                setStatusAllFalse();
                type_qita.setSelected(true);
                type = AccountBean.Type.QITA.getId();
                break;
            case R.id.btn_back:
                this.finish();
                break;
        }
    }
}