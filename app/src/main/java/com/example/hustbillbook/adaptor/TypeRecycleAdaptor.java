package com.example.hustbillbook.adaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hustbillbook.R;
import com.example.hustbillbook.activity.AddRecordActivity;
import com.example.hustbillbook.bean.TypeViewBean;
import com.example.hustbillbook.tools.ImageUtils;

import java.util.List;

/*
 参照了gihub项目 cocoBill 的写法
 */
public class TypeRecycleAdaptor extends RecyclerView.Adapter<TypeRecycleAdaptor.ViewHolder> {

    private final AddRecordActivity mContext;
    private final LayoutInflater mInflater;
    private final List<TypeViewBean> mData;

    private OnClickListener mListener;

    public TypeRecycleAdaptor(AddRecordActivity mContext, List<TypeViewBean> mData) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        this.mData = mData;
    }

    public void setOnClickListener(OnClickListener listener) {
        if (mListener == null)
            mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_types, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(mData.get(position).getTypeName());
        holder.img.setImageDrawable(ImageUtils.getDrawable(mContext, mData.get(position).getTypeImg()));
    }

    @Override
    public int getItemCount() {
        return (mData == null ? 0 : mData.size());
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView title;
        private final ImageView img;

        ViewHolder(@NonNull View view) {
            super(view);

            title = view.findViewById(R.id.item_tb_type_tv);
            img = view.findViewById(R.id.item_tb_type_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.OnClick(getAdapterPosition());
        }
    }

    /**
     * 自定义分类选择接口
     */
    public interface OnClickListener {
        void OnClick(int index);
    }
}
