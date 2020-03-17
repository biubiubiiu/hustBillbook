package com.example.hustbillbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hustbillbook.R;

public class EditAccountActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText accountName;
    private EditText accountBalance;
    private EditText accountType;
    private EditText accountNote;

    private ImageView btn_back;
    private ImageView btn_confirm;

    private TextView btn_delete;
    private String oldName;
    private String oldType;
    private String oldBalance;
    private String oldNote;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        accountName = findViewById(R.id.et_new_accoutName);
        accountBalance = findViewById(R.id.et_new_accoutBalance);
        accountType = findViewById(R.id.et_new_accoutType);
        accountNote = findViewById(R.id.et_new_accountNote);

        btn_back = findViewById(R.id.btn_back);
        btn_confirm = findViewById(R.id.iv_confirm);

        btn_delete = findViewById(R.id.tv_delete);

        btn_back.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
        btn_delete.setOnClickListener(this);

        Intent intent = getIntent();
        oldName = intent.getStringExtra("name");
        oldType = intent.getStringExtra("type");
        oldBalance = intent.getStringExtra("balance");
        oldNote = intent.getStringExtra("note");

        accountName.setText(oldName);
        accountType.setText(oldType);
        accountBalance.setText(oldBalance);
        accountNote.setText(oldNote);

        //账户金额输入检查函数
        accountBalance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String curr = accountBalance.getText().toString();

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
                    if (curr.length() > 8)
                        editable.delete(curr.length() - 1, curr.length());
                } else {
                    if (curr.length() - 1 - dotIndex > 2)
                        editable.delete(dotIndex + 3, dotIndex + 4);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                this.finish();
                break;
            case R.id.iv_confirm:
                commit();
                break;
            case R.id.tv_delete:
                deleteAccount();
                break;
        }
    }

    private void commit() {
        try {
            double d = Double.valueOf(accountBalance.getText().toString());
        } catch (Exception e) {
            Toast.makeText(this, "请输入合法的金额！", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("balance", accountBalance.getText().toString());

        if (accountName.getText().toString().isEmpty()) {
            intent.putExtra("name", oldName);
        } else {
            intent.putExtra("name", accountName.getText().toString());
        }

        intent.putExtra("note", accountNote.getText().toString());

        setResult(ViewSpecificAccountActivity.RESULT_MODIFY, intent);
        this.finish();
    }

    private void deleteAccount() {
        setResult(ViewSpecificAccountActivity.RESULT_DELETE);
        this.finish();
    }
}
