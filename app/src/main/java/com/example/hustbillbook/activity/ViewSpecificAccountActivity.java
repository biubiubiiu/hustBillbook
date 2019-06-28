package com.example.hustbillbook.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hustbillbook.DataBaseHelper;
import com.example.hustbillbook.DataRepository;
import com.example.hustbillbook.R;
import com.example.hustbillbook.SingleCommonData;
import com.example.hustbillbook.TreeNode;
import com.example.hustbillbook.adaptor.ItemToAccountAdaptor;
import com.example.hustbillbook.bean.AccountBean;
import com.example.hustbillbook.bean.RecordBean;

import java.util.ArrayList;
import java.util.List;

public class ViewSpecificAccountActivity extends AppCompatActivity implements View.OnClickListener{

    private int numberOfAccountBean;//账户编号

    private String year;//展示数据所在的年份
    List<TreeNode> list;//存储所有记录
    private ItemToAccountAdaptor adapter;
    private double totalExp;
    private double totalInc;

    private TextView accountName;
    private TextView accountMoney;
    private TextView totalExpense;
    private TextView totalIncome;
    private TextView showYear;
    private ListView listView;

    private TextView btn_edit;
    private ImageView btn_back;

    private AccountBean currentAccount;

    public static final int RESULT_MODIFY = 10;
    public static final int RESULT_DELETE = 11;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_specific_account);

        accountName = findViewById(R.id.accountName_Specific);
        accountMoney = findViewById(R.id.restMoney_Specific);
        totalExpense = findViewById(R.id.expense_Specific);
        totalIncome = findViewById(R.id.income_Specific);
        showYear = findViewById(R.id.year_Specific);
        listView = findViewById(R.id.listView2);

        btn_edit = findViewById(R.id.setting_Specific);
        btn_back = findViewById(R.id.btn_back);

        btn_edit.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        Intent intent = getIntent();
        numberOfAccountBean = intent.getExtras().getInt("account_id");

        currentAccount = SingleCommonData.getAccountList().get(numberOfAccountBean);
        //获取当前账户

        accountName.setText(currentAccount.accountName);
        accountMoney.setText(currentAccount.accountMoney);

        //TODO 关于年year的初始设置
        year = "2019";
        showYear.setText(year);

        //数据初始化
        list = new ArrayList<>();
        initData();
        adapter = new ItemToAccountAdaptor(ViewSpecificAccountActivity.this, R.layout.item_month, list);
        listView.setAdapter(adapter);

        totalExpense.setText(String.valueOf(totalExp));
        totalIncome.setText(String.valueOf(totalInc));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                TreeNode treeNode = list.get(position);

                if (treeNode.isParent) { //是父节点
                    if (treeNode.isExpand) { //是否展开
                        treeNode.isExpand = false;
                        setChildrenStatus(position, false);//是展开，点击后收起，子节点不可见
                    } else {
                        treeNode.isExpand = true;
                        setChildrenStatus(position, true);
                    }
                } else {
                    //TODO 是子节点
                }

                //更新listView
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void setChildrenStatus(int position, boolean status) {
        position++;

        while (position < list.size() && !list.get(position).isParent) {
            TreeNode t = list.get(position);
            t.visible = status;

            position++;
        }
    }

    private void initData() {
        totalExp = 0;
        totalInc = 0;

        short flag = 1;//因为记录在SingleCommonData中已经按序排列，所以以此来防止无意义的循环
        int position = 0;//记录父节点存放的位置
        String currmonth = "1";
        double expense = 0;
        double income = 0;
        int total = SingleCommonData.getRecordList().size();

        for (int i = 0; i < total; i++) {  //读入该年所有记录
            if (flag == 3) break;

            RecordBean r = SingleCommonData.getRecordList().get(i);
            String month = r.recordDate.split("-")[1];
            //TODO 账户信息判断，是否为当前账户
            if (r.recordDate.split("-")[0].equals(year)) {
                if (1 == flag) flag = 2;//已进入对应年份

                if (!month.equals(currmonth)) {
                    //已经遍历完一个月的记录
                    do {
                        if (expense != 0 || income != 0) {
                            TreeNode node = new TreeNode(currmonth, String.valueOf(expense), String.valueOf(income));
                            list.add(position, node);//将每月title存入指定位置，即当月所有记录的开始处

                            position = list.size();//更新position位置
                            expense = 0;
                            income = 0;
                        }
                        currmonth = String.valueOf(Integer.valueOf(currmonth) + 1);
                    } while (!currmonth.equals(month));
                }

                if (r.isExpense) {
                    expense += Double.valueOf(r.recordMoney);
                    totalExp += Double.valueOf(r.recordMoney);
                } else {
                    income += Double.valueOf(r.recordMoney);
                    totalInc += Double.valueOf(r.recordMoney);
                }

                TreeNode node = new TreeNode(r, i);
                list.add(node);
            } else if (flag == 2)
                flag = 3;//已遍历完该年所有记录，可以退出
        }

        if (expense != 0 || income != 0) {
            list.add(position, new TreeNode(currmonth, String.valueOf(expense), String.valueOf(income)));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 0) {
            switch (resultCode) {
                case RESULT_MODIFY:
                    String newName = data.getStringExtra("name");
                    String newBalance = data.getStringExtra("balance");

                    currentAccount.accountName = newName;
                    currentAccount.accountMoney = newBalance;

                    accountName.setText(newName);
                    accountMoney.setText(newBalance);

                    adapter.notifyDataSetChanged();
                    break;

                case RESULT_DELETE:
                    DataBaseHelper dataBaseHelper = new DataBaseHelper(this);

                    SingleCommonData.syncAccountLIst(dataBaseHelper);

                    int index = SingleCommonData.getAccountList().indexOf(currentAccount);
                    dataBaseHelper.deleteOneAccount(index);
                    SingleCommonData.removeAccount(index);
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_back:
                this.finish();
                break;

            case R.id.setting_Specific:
                Intent intent = new Intent(ViewSpecificAccountActivity.this, EditAccountActivity.class);
                intent.putExtra("name", currentAccount.accountName);
                intent.putExtra("balance", currentAccount.accountMoney);

                int typeId = currentAccount.accountType;
                String type = AccountBean.getTypeName(typeId);

                intent.putExtra("type", type);
                startActivityForResult(intent, 0);
                break;
        }
    }
}
