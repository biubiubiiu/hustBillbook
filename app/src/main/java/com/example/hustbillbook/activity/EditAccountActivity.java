package com.example.hustbillbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hustbillbook.R;

public class EditAccountActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText accountName;
    private EditText accountBalance;
    private EditText accountType;

    private ImageView btn_back;
    private ImageView btn_confirm;

    private TextView btn_delete;
    private String oldName;
    private String oldType;
    private String oldBalance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        accountName = findViewById(R.id.et_new_accoutName);
        accountBalance = findViewById(R.id.et_new_accoutBalance);
        accountType = findViewById(R.id.et_new_accoutType);

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

        accountName.setText(oldName);
        accountType.setText(oldType);
        accountBalance.setText(oldBalance);
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

        setResult(ViewSpecificAccountActivity.RESULT_MODIFY, intent);
        this.finish();
    }

    private void deleteAccount() {
        setResult(ViewSpecificAccountActivity.RESULT_DELETE);
        this.finish();
    }
}
