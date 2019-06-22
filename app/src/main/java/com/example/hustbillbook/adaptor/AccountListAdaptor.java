package com.example.hustbillbook.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.hustbillbook.R;
import com.example.hustbillbook.bean.AccountBean;

import java.util.List;

public class AccountListAdaptor extends ArrayAdapter<AccountBean> {

    private int resourceId;
    public AccountListAdaptor(Context context, int textViewResourceId, List<AccountBean> objects){
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    public View getView(int position, View convertView, ViewGroup parent){
        AccountBean accountBean = getItem(position); //获取当前实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);

        ImageView accountImage = view.findViewById(R.id.account_image);//账户icon
        TextView accountName = view.findViewById(R.id.account_name);//账户名称
        TextView accountNote = view.findViewById(R.id.account_note);//账户备注
        TextView accountMoney = view.findViewById(R.id.account_money);//账户金额

        accountName.setText(accountBean.getAccountName());  //设置账户名称
        accountNote.setText("备注："+ accountBean.getAccountNote());  //设置账户备注
        accountMoney.setText("金额："+ String.valueOf(accountBean.getAccountMoney()));

        switch(accountBean.getAccountType()){
            case 1: accountImage.setImageResource(R.drawable.img_shenghuofei);break;
            case 2:accountImage.setImageResource(R.drawable.img_hongbao);break;
            default:accountImage.setImageResource(R.drawable.img_qita);break;
        }

        return view;
    }
}
