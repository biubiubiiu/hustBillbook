package com.example.hustbillbook.adaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hustbillbook.R;
import com.example.hustbillbook.DataRepository;
import com.example.hustbillbook.bean.AccountBean;
import com.example.hustbillbook.bean.TypeViewBean;
import com.example.hustbillbook.tools.ImageUtils;

import java.util.List;

public class AccountRecycleAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final AppCompatActivity mContext;
    private final LayoutInflater mInflater;
    private final List<AccountBean> mData;

    private AccountRecycleAdaptor.OnClickListener mListener;

    public AccountRecycleAdaptor(AppCompatActivity mContext, List<AccountBean> mData) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        this.mData = mData;
    }

    public void setOnClickListener(AccountRecycleAdaptor.OnClickListener listener) {
        if (mListener == null)
            mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder;
        if (viewType == 1) {
            view = mInflater.inflate(R.layout.item_accountlist, parent, false);
            viewHolder = new ViewHolder(view);
        } else {
            view = mInflater.inflate(R.layout.item_add_account, parent, false);
            viewHolder = new ViewHolder_add(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        DataRepository r = DataRepository.getInstance();
        AccountBean accountBean = mData.get(position);
        TypeViewBean typeViewBean = r.findType(accountBean.accountType);

        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).accountName.setText(accountBean.accountName);  //设置账户名称
            ((ViewHolder) holder).accountNote.setText(accountBean.accountTitle);  //设置账户备注
            ((ViewHolder) holder).accountMoney.setText(String.valueOf(accountBean.accountMoney));//设置账户余额
            ((ViewHolder) holder).accountImage.setImageResource(ImageUtils.getImageId(mContext, typeViewBean.getTypeImg()));

        } else if (holder instanceof ViewHolder_add){
            ((ViewHolder_add) holder).accountImage.setImageResource(ImageUtils.getImageId(mContext, typeViewBean.getTypeImg()));
            ((ViewHolder_add) holder).accountName.setText(accountBean.accountName);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mData.size() -1 )
            return 2;
        else
            return 1;
    }

    @Override
    public int getItemCount() {
        return (mData == null ? 0 : mData.size());
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final CardView cardView;
        final ImageView accountImage;
        final TextView accountName;
        final TextView accountNote;
        final TextView accountMoney;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.account_cardview); // cardView
            accountImage = itemView.findViewById(R.id.account_image);//账户icon
            accountName = itemView.findViewById(R.id.account_name);//账户名称
            accountNote = itemView.findViewById(R.id.account_note);//账户备注
            accountMoney = itemView.findViewById(R.id.account_money);//账户金额

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.OnClick(getAdapterPosition());
        }
    }

    class ViewHolder_add extends RecyclerView.ViewHolder implements View.OnClickListener{

        final CardView cardView;
        final ImageView accountImage;
        final TextView accountName;

        public ViewHolder_add(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.addaccount_cardview);
            accountImage = itemView.findViewById(R.id.image_item_addaccount);
            accountName = itemView.findViewById(R.id.text_item_addaccount);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.OnClick(getAdapterPosition());
        }
    }

    public interface OnClickListener {
        void OnClick(int index);
    }
}
