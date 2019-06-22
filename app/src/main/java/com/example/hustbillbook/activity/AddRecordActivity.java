package com.example.hustbillbook.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hustbillbook.DataBaseHelper;
import com.example.hustbillbook.MainActivity;
import com.example.hustbillbook.R;
import com.example.hustbillbook.SingleCommonData;
import com.example.hustbillbook.bean.RecordBean;
import com.example.hustbillbook.tools.CalenderUtils;

import java.util.Calendar;

public class AddRecordActivity extends AppCompatActivity {
    final int EXPENSE = 1;
    final int INCOME = 2;

    private int screenHeight;//屏幕高度
    private Toast toast;

    private EditText editText_date;     //日期输入框
    private ImageView imageView;
    private Button button_ok;
    private EditText editText_money;
    private EditText editText_note;

    // TODO 删去一些临时变量
    private int type;    //类型，默认情况下为支出
    private int kind;    //二级类型

    private DataBaseHelper mDataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        mDataBaseHelper = new DataBaseHelper(this);

        this.type = EXPENSE;
        this.kind = 1;
        //置初值
        WindowManager wm = (WindowManager) AddRecordActivity.this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        float density = dm.density;
        screenHeight = (int) (height/density);
        //获取屏幕高度

        imageView = findViewById(R.id.imageView);     //设置点击图片事件，实现界面跳转
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSelectTypeActivity();
            }
        });

        editText_date = findViewById(R.id.editText4);
        editText_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg();
                    return true;
                }
                return false;
            }
        });
        editText_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickDlg();
                }
            }
        });

        showDateHint();     //设置日期显示当前默认日期

        RadioButton radioButton_expense = findViewById(R.id.expense);
        radioButton_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = EXPENSE;
                imageView.setImageResource(R.drawable.img_canyin);
                kind = 1;   //支出类型从1开始编号，1表示餐饮
            }
        });

        RadioButton radioButton_income = findViewById(R.id.income);
        radioButton_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = INCOME;
                imageView.setImageResource(R.drawable.img_gongzi);
                kind = 50;  //收入类型从50开始编号，50表示工资
            }
        });

        editText_money = findViewById(R.id.editText2);
        editText_note = findViewById(R.id.editText3);

        //ok按钮的事件处理
        button_ok = findViewById(R.id.ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double money = 0;
                if (!editText_money.getEditableText().toString().equals(""))
                    money = Double.parseDouble(editText_money.getEditableText().toString());
                if (money == 0){
                    toast = Toast.makeText(getApplicationContext(), "金额不能为0！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, screenHeight / 2);      //设置toast位置

                    LinearLayout linearLayout = (LinearLayout) toast.getView();
                    TextView messageTextView = (TextView)linearLayout.getChildAt(0);
                    messageTextView.setTextSize(20);

                    toast.show();

                } else{
                    Intent intent = new Intent(AddRecordActivity.this, SelectAccountActivity.class);
                    startActivityForResult(intent, 1);//请求码为1
                }
            }
        });

        //设置金额最多两位小数
        editText_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                String temp = editable.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2)
                    editable.delete(posDot + 3, posDot + 4);
            }
        });



    }

    private void goToSelectTypeActivity(){      //跳转函数，选择类型
        Intent intent;

        if(EXPENSE == type) {
            intent = new Intent(this, SelectExpenseTypeActivity.class);
            intent.putExtra("kind", kind);
        }
        else {
            intent = new Intent(this, SelectIncomeTypeActivity.class);
            intent.putExtra("kind", kind);
        }

        startActivityForResult(intent, 0);   //接受新打开的activity关闭后返回的数据
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case 0:{
                kind = data.getExtras().getInt("kind");
                switch (kind){
                    case 1:imageView.setImageResource(R.drawable.img_canyin);break;
                    case 2:imageView.setImageResource(R.drawable.img_dianying);break;
                    case 3:imageView.setImageResource(R.drawable.img_huafei);break;
                    case 4:imageView.setImageResource(R.drawable.img_jiaotong);break;
                    case 5:imageView.setImageResource(R.drawable.img_yiliao);break;
                    case 6:imageView.setImageResource(R.drawable.img_yifu);break;
                    case 7:imageView.setImageResource(R.drawable.img_fangzu);break;
                    case 8:imageView.setImageResource(R.drawable.img_shuiguo);break;
                    case 9:imageView.setImageResource(R.drawable.img_lingshi);break;
                    case 10:imageView.setImageResource(R.drawable.img_shucai);break;
                    case 11:imageView.setImageResource(R.drawable.img_shenghuoyongpin);break;
                    case 12:imageView.setImageResource(R.drawable.img_taobao);break;
                    case 13:imageView.setImageResource(R.drawable.img_zhuanzhang);break;
                    case 14:imageView.setImageResource(R.drawable.img_hongbao);break;
                    case 15:imageView.setImageResource(R.drawable.img_qita);break;

                    case 50:imageView.setImageResource(R.drawable.img_gongzi);break;
                    case 51:imageView.setImageResource(R.drawable.img_linghuaqian);break;
                    case 52:imageView.setImageResource(R.drawable.img_jianzhi);break;
                    case 53:imageView.setImageResource(R.drawable.img_shenghuofei);break;
                    case 54:imageView.setImageResource(R.drawable.img_hongbao);break;
                    case 55:imageView.setImageResource(R.drawable.img_touzi);break;
                    case 56:imageView.setImageResource(R.drawable.img_jiangjin);break;
                    case 57:imageView.setImageResource(R.drawable.img_qita);break;
                    default:break;
                }
                break;
            }
            case 1:{
                if(RESULT_OK == resultCode){

                    // 开始执行数据持久化操作
                    RecordBean recordBean = new RecordBean();
                    recordBean.recordTitle = editText_note.getText().toString();
                    recordBean.recordDate = editText_date.getText().toString();
                    //TODO 将money改为用double类型存储
                    recordBean.recordMoney = editText_money.getText().toString();

                    // 将新数据写入数据库
                    mDataBaseHelper.insertRecord(recordBean);
                    // 写入本地List
                    SingleCommonData.add(recordBean);

                    toast = Toast.makeText(getApplicationContext(), "记账成功！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, screenHeight / 3);      //设置toast位置

                    LinearLayout linearLayout = (LinearLayout) toast.getView();
                    TextView messageTextView = (TextView)linearLayout.getChildAt(0);
                    messageTextView.setTextSize(20);

                    toast.show();
                    //弹出记账成功提示

                    AddRecordActivity.this.finish();
                    break;
                }
            }
            default:break;
        }
    }

    protected void showDatePickDlg() {              //日期选择函数
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddRecordActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                AddRecordActivity.this.editText_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    //设置日期显示当前日期
    private void showDateHint(){
        String presentDate = CalenderUtils.getCurrentDate();
        this.editText_date.setHint(presentDate);
    }
}
