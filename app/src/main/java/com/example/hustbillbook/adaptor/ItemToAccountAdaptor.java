package com.example.hustbillbook.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hustbillbook.R;
import com.example.hustbillbook.DataRepository;
import com.example.hustbillbook.TreeNode;
import com.example.hustbillbook.bean.TypeViewBean;
import com.example.hustbillbook.tools.ImageUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemToAccountAdaptor extends ArrayAdapter<TreeNode> {

    private final int resourceId;
    private final Context mContext;

    public ItemToAccountAdaptor(Context context, int textViewResourceId, List<TreeNode> objects){
        super(context, textViewResourceId, objects);
        mContext = context;
        resourceId = textViewResourceId;
    }

    @NotNull
    public View getView(int position, View convertView, @NotNull ViewGroup parent){
        TreeNode treeNode = getItem(position);
        View view;

        if (treeNode.isParent){ //是父节点
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);

            ImageView status = view.findViewById(R.id.isExpand_Specific);
            TextView textMonth = view.findViewById(R.id.month_detail_Specific);
            TextView textExpense = view.findViewById(R.id.expense_detail_Specific);
            TextView textIncome = view.findViewById(R.id.income_detail_Specific);

            if (treeNode.isExpand)
                status.setImageResource(R.drawable.img_zhankai);
            else
                status.setImageResource(R.drawable.img_shouqi);

            textMonth.setText(treeNode.month);
            textExpense.setText(treeNode.expense);
            textIncome.setText(treeNode.income);
        }
        else if (treeNode.visible){  //是子节点
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_record_withoutyear, parent, false);

            TextView day = view.findViewById(R.id.day_new);
            ImageView type_image = view.findViewById(R.id.type_second_image_new);
            TextView type_text = view.findViewById(R.id.type_second_text_new);
            TextView title = view.findViewById(R.id.title_new);
            TextView money = view.findViewById(R.id.cost_new);

            day.setText(treeNode.record.recordDate.split("-")[2] + "日");
            title.setText(treeNode.record.recordTitle);
            if (treeNode.record.isExpense)
                money.setText("- " + treeNode.record.recordMoney);
            else
                money.setText("+ " + treeNode.record.recordMoney);


            DataRepository r = DataRepository.getInstance();
            TypeViewBean typeViewBean = r.findType(treeNode.record.recordType);
            type_image.setImageResource(ImageUtils.getImageId(mContext, typeViewBean.getTypeImg()));
            type_text.setText(typeViewBean.getTypeName());
        }
        else {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_blank, parent, false);
        }

        return view;

    }



}
