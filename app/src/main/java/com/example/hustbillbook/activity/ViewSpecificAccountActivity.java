package com.example.hustbillbook.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hustbillbook.R;
import com.example.hustbillbook.SingleCommonData;
import com.example.hustbillbook.TreeNode;
import com.example.hustbillbook.adaptor.ItemToAccountAdaptor;
import com.example.hustbillbook.bean.AccountBean;
import com.example.hustbillbook.bean.RecordBean;
import com.example.hustbillbook.tools.CalendarUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewSpecificAccountActivity extends AppCompatActivity implements View.OnClickListener {

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

    private FloatingActionButton fab;

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
        fab = findViewById(R.id.fab_Specific);
        btn_edit = findViewById(R.id.setting_Specific);
        btn_back = findViewById(R.id.btn_back_Specific);

        btn_edit.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        final Intent intent = getIntent();
        numberOfAccountBean = Objects.requireNonNull(intent.getExtras()).getInt("account_id");

        currentAccount = SingleCommonData.accountAt(numberOfAccountBean);
        //获取当前账户

        accountName.setText(currentAccount.accountName);
        accountMoney.setText(currentAccount.accountMoney);


        year = CalendarUtils.getCurrentDate().split("-")[0];
        showYear.setText(year);

        //数据初始化
        list = new ArrayList<>();
        initData();
        adapter = new ItemToAccountAdaptor(ViewSpecificAccountActivity.this, R.layout.item_month, list);
        listView.setAdapter(adapter);

        totalExpense.setText(String.valueOf(totalExp));
        totalIncome.setText(String.valueOf(totalInc));

        //单击监听
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
                    /*modified on 6/30*/
                    //单击修改
                    //此时不仅要将记录原有内容展示出来，也要设置账户属性不可更改
                    //因此addAccount需要新增一种情况
                    Intent intent1 = new Intent(ViewSpecificAccountActivity.this, AddRecordActivity.class);
                    intent1.putExtra("handleType", 4);
                    intent1.putExtra("index", SingleCommonData.getRecordList().indexOf(treeNode.record));

                    if (treeNode.record.isExpense)
                        totalExp -= Double.valueOf(treeNode.record.recordMoney);
                    else
                        totalInc -= Double.valueOf(treeNode.record.recordMoney);
                    //先将本地记录删掉，修改成功后重新载入
                    list.remove(position);

                    startActivityForResult(intent1, 2);
                }

                //更新listView
                adapter.notifyDataSetChanged();
            }
        });

        /*
        *
        * modified on 6/30
         */
        //长按监听
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                if (!list.get(position).isParent){      //如果是子节点，则执行长按删除操作
                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewSpecificAccountActivity.this);
                    builder.setTitle("Delete Record");
                    builder.setMessage("Are you sure to delete the record?");
                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //从本地list和数据库中删除记录
                            TreeNode toBeDeleted = list.get(position);
                            TreeNode parent = null;
                            for (int j=position; j>=0; j--){
                                if (list.get(j).isParent) {
                                    parent = list.get(j);
                                    break;
                                }
                            }

                            //修改收支统计信息
                            if (toBeDeleted.record.isExpense) {
                                totalExp -= Double.valueOf(toBeDeleted.record.recordMoney);
                                parent.expense = String.valueOf(Double.valueOf(parent.expense) - Double.valueOf(toBeDeleted.record.recordMoney));
                                totalExpense.setText(String.valueOf(totalExp));
                            }
                            else {
                                totalInc -= Double.valueOf(toBeDeleted.record.recordMoney);
                                parent.income = String.valueOf(Double.valueOf(parent.expense) - Double.valueOf(toBeDeleted.record.recordMoney));
                                totalIncome.setText(String.valueOf(totalInc));
                            }


                            SingleCommonData.removeRecord(SingleCommonData.getRecordList().indexOf(toBeDeleted.record));


                            //从list中删除
                            //需检查是否是该月下的唯一记录，如果是，则同时删除父节点
                            if ( ( position == list.size() - 1 && list.get(position-1).isParent )
                                 || ( list.get(position-1).isParent && list.get(position+1).isParent )){

                                list.remove(position);
                                list.remove(position-1);
                            }
                            else {
                                list.remove(position);
                            }

                            accountMoney.setText(SingleCommonData.accountAt(numberOfAccountBean).accountMoney);
                            //更新listView
                            adapter.notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("Cancel", null);
                    builder.create().show();
                }

                return true;//若返回False，则是当长按时，既调用onItemLongClick，同时调用onItemLongClick后
                            //还会调用onItemClick，就是说会同时调用onItemLongClick，和onItemClick，
                            //若返回true，则只调用onItemLongClick
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ViewSpecificAccountActivity.this, AddRecordActivity.class);
                intent1.putExtra("handleType", 2);//第二种情况，对应账户下的添加，账户名不可改
                intent1.putExtra("accountNum", numberOfAccountBean);
                startActivityForResult(intent1, 1);
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

            RecordBean r = SingleCommonData.recordAt(i);
            String month = r.recordDate.split("-")[1];

            if (r.recordDate.split("-")[0].equals(year)) {
                if (1 == flag) flag = 2;//已进入对应年份

                if (r.recordAccount != numberOfAccountBean)
                    continue;   //如果不是当前账户下的，则跳过

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

                TreeNode node = new TreeNode(r);
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
                    String newNote = data.getStringExtra("note");

                    //更新显示
                    accountName.setText(newName);
                    accountMoney.setText(newBalance);

                    // 更新本地 list 和数据库
                    AccountBean newAccount = new AccountBean(newName,
                            currentAccount.accountType,
                            newBalance,
                            newNote);

                    SingleCommonData.updateAccount(currentAccount, newAccount);
                    currentAccount = newAccount;

                    adapter.notifyDataSetChanged();
                    break;

                case RESULT_DELETE:
                    int index = SingleCommonData.getAccountList().indexOf(currentAccount);

                    /*modified on 6/30*/
                    if (index == 0){    //初始账户不可删除
                        Toast.makeText(this, "此账户不可删除！", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        SingleCommonData.removeAccount(index);

                        //弹出提示，返回查看账户界面
                        Toast.makeText(this, "账户删除成功！", Toast.LENGTH_SHORT).show();

                        this.finish();
                        //Intent intent = new Intent(ViewSpecificAccountActivity.this, ViewAccountsActivity.class);
                        //startActivity(intent);
                    }

                    break;

                default:
                    break;
            }
        } else{     //将修改记录和新增记录合并到一起处理
                    //新增记录的请求码是1，修改记录的请求码是2
            /*modified on 6/30*/
            if (resultCode == RESULT_OK || requestCode == 2) {

                int index = data.getIntExtra("index", 0);
                handleNewRecord(index);
                //更新listView
                adapter.notifyDataSetChanged();

            }

        }
    }

    private void handleNewRecord(int indexInLocalList) {

        // TODO 由于添加记录后，本地 list 会调用 sync 方法保持有序，因此这一步会出现问题
        // TODO 请尝试在 AddRecordActivity 返回时传递新记录的 index 回来（indexOf方法即可，不必担心相同记录的问题）
        /*modified on 6/30*/

        RecordBean recordBean = SingleCommonData.recordAt(indexInLocalList);
        //获取新添加的记录
        if (!recordBean.recordDate.split("-")[0].equals(year))
            return;//不在当前年，直接返回

        if (recordBean.isExpense) {
            totalExp += Double.valueOf(recordBean.recordMoney);
            totalExpense.setText(String.valueOf(totalExp));
        } else {
            totalInc += Double.valueOf(recordBean.recordMoney);
            totalIncome.setText(String.valueOf(totalInc));
        }

        accountMoney.setText(SingleCommonData.accountAt(numberOfAccountBean).accountMoney);

        int index;//记录插在list的哪个位置

        String presentMonth = recordBean.recordDate.split("-")[1];//这条记录所在的月份
        String presentDay = recordBean.recordDate.split("-")[2];//所在日

        if (list.size() == 0) {
            TreeNode tN;

            if (recordBean.isExpense)
                tN = new TreeNode(presentMonth, recordBean.recordMoney, "0");
            else
                tN = new TreeNode(presentMonth, "0", recordBean.recordMoney);

            list.add(tN);
            list.add(new TreeNode(recordBean));
        } else {
            for (int i = 0; i < list.size(); i++) {
                TreeNode node = list.get(i);
                if (node.isParent && node.month.equals(presentMonth)) {   //找到所在月的父节点
                    if (recordBean.isExpense)
                        node.expense = String.valueOf(Double.valueOf(node.expense) + Double.valueOf(recordBean.recordMoney));
                    else
                        node.income = String.valueOf(Double.valueOf(node.income) + Double.valueOf(recordBean.recordMoney));

                    index = i;
                    do {
                        index++;
                    } while (index < list.size() && !list.get(index).isParent && presentDay.compareTo(list.get(index).record.recordDate.split("-")[2]) > 0);

                    list.add(index, new TreeNode(recordBean));

                    break;
                } else if (node.isParent && node.month.compareTo(presentMonth) > 0) {  //说明没有这个月，需要添加一个
                    TreeNode tN;

                    if (recordBean.isExpense)
                        tN = new TreeNode(presentMonth, recordBean.recordMoney, "0");
                    else
                        tN = new TreeNode(presentMonth, "0", recordBean.recordMoney);

                    list.add(i, tN);
                    list.add(i + 1, new TreeNode(recordBean));

                    break;
                } else if (i == list.size() - 1) {
                    TreeNode tN;

                    if (recordBean.isExpense)
                        tN = new TreeNode(presentMonth, recordBean.recordMoney, "0");
                    else
                        tN = new TreeNode(presentMonth, "0", recordBean.recordMoney);

                    list.add(i + 1, tN);
                    list.add(i + 2, new TreeNode(recordBean));

                    break;
                }
            }
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_back_Specific:
                this.finish();
                break;

            case R.id.setting_Specific:
                Intent intent = new Intent(ViewSpecificAccountActivity.this, EditAccountActivity.class);
                intent.putExtra("name", currentAccount.accountName);
                intent.putExtra("balance", currentAccount.accountMoney);
                intent.putExtra("note", currentAccount.accountTitle);

                int typeId = currentAccount.accountType;
                String type = AccountBean.getTypeName(typeId);

                intent.putExtra("type", type);
                startActivityForResult(intent, 0);
                break;
        }
    }
}
