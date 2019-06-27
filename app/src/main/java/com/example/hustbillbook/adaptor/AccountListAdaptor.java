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
import com.example.hustbillbook.SingleCommonData;
import com.example.hustbillbook.TmpRepository;
import com.example.hustbillbook.bean.AccountBean;
import com.example.hustbillbook.bean.TypeViewBean;
import com.example.hustbillbook.tools.ImageUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AccountListAdaptor extends ArrayAdapter<AccountBean> {

    private int resourceId;
    private Context mContext;

    public AccountListAdaptor(Context context, int textViewResourceId, List<AccountBean> objects){
        super(context, textViewResourceId, objects);
        mContext = context;
        resourceId = textViewResourceId;
    }

    @NonNull
    public View getView(int position, View convertView, @NotNull ViewGroup parent){
        AccountBean accountBean = getItem(position); //获取当前实例
        TmpRepository r = TmpRepository.getInstance();
        View view;
        TypeViewBean typeViewBean = r.findType(accountBean.accountType);

        if (position != SingleCommonData.getAccountList().size() - 1) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_accountlist, parent, false);

            ImageView accountImage = view.findViewById(R.id.account_image);//账户icon
            TextView accountName = view.findViewById(R.id.account_name);//账户名称
            TextView accountNote = view.findViewById(R.id.account_note);//账户备注
            TextView accountMoney = view.findViewById(R.id.account_money);//账户金额

            accountName.setText(accountBean.accountName);  //设置账户名称
            accountNote.setText(accountBean.accountTitle);  //设置账户备注
            accountMoney.setText(String.valueOf(accountBean.accountMoney));//设置账户余额

            accountImage.setImageResource(ImageUtils.getImageId(mContext, typeViewBean.getTypeImg()));
        } else {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_add_account, parent, false);

            ImageView accountImage = view.findViewById(R.id.image_item_addaccount);
            TextView accountName = view.findViewById(R.id.text_item_addaccount);

            accountImage.setImageResource(ImageUtils.getImageId(mContext, typeViewBean.getTypeImg()));
            accountName.setText(accountBean.accountName);
        }

        return view;
    }
}
