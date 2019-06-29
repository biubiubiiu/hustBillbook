package com.example.hustbillbook.adaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hustbillbook.R;
import com.example.hustbillbook.activity.ChartsActivity;
import com.example.hustbillbook.bean.TypeRankBean;
import com.example.hustbillbook.tools.ImageUtils;

import java.util.List;

public class RankRecycleAdaptor extends RecyclerView.Adapter<RankRecycleAdaptor.ViewHolder> {

    private final ChartsActivity mContext;
    private final LayoutInflater mInflater;
    private List<TypeRankBean> mData;

    public RankRecycleAdaptor(ChartsActivity mContext) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_ranking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.typeName.setText(mData.get(position).getTypeView().getTypeName());
        holder.img.setImageDrawable(ImageUtils.getDrawable(mContext, mData.get(position).getTypeView().getTypeImg()));
        holder.number.setText(String.valueOf(mData.get(position).getNumber()));
        holder.ratio.setText(mContext.getString(R.string.number_percentage, mData.get(position).getRatio()*100));
        holder.bar.setProgress((int)(mData.get(position).getRatio()*100));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<TypeRankBean> mData) {
        this.mData = mData;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView typeName;
        private final TextView number;
        private final TextView ratio;
        private final ImageView img;
        private final ProgressBar bar;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            typeName = itemView.findViewById(R.id.tv_rank_type);
            number = itemView.findViewById(R.id.tv_rank_number);
            ratio = itemView.findViewById(R.id.tv_rank_ratio);
            img = itemView.findViewById(R.id.iv_rank_type_image);
            bar = itemView.findViewById(R.id.pb_rank_ratio);
        }
    }
}
