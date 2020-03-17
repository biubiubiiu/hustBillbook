package com.example.hustbillbook.adaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hustbillbook.MainActivity;
import com.example.hustbillbook.R;
import com.example.hustbillbook.DataRepository;
import com.example.hustbillbook.bean.RecordBean;
import com.example.hustbillbook.bean.TypeViewBean;
import com.example.hustbillbook.tools.ImageUtils;

import java.util.List;

public class RecordRecycleAdaptor extends RecyclerView.Adapter<RecordRecycleAdaptor.ViewHolder> {

    private List<RecordBean> mList;   // 数据源
    private final MainActivity mContext;
    private final LayoutInflater mInflater;     // 布局装载器对象

    private OnClickListener mListener;

    public RecordRecycleAdaptor(MainActivity mContext, List<RecordBean> list) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        this.mList = list;
    }

    public void setOnClickListener(OnClickListener listener) {
        if (mListener == null)
            mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recordlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 获取相应索引的Bean对象
        RecordBean bean = mList.get(position);
        // 设置控件的对应属性值
        String[] array = bean.recordDate.split("-");
        holder.mTvRecordDate_monthAndDay.setText(mContext.getString(R.string.month_day, array[1], array[2]));
        holder.mTvRecordDate_year.setText(array[0]);

        DataRepository r = DataRepository.getInstance();
        TypeViewBean typeViewBean = r.findType(bean.recordType);
        holder.mTvRecordType_image.setImageResource(ImageUtils.getImageId(mContext, typeViewBean.getTypeImg()));
        holder.mTvRecordType_text.setText(typeViewBean.getTypeName());

        holder.mTvRecordTitle.setText(bean.recordTitle);
        if (bean.isExpense)
            holder.mTcRecordMoney.setText(mContext.getString(R.string.negative_number, bean.recordMoney));
        else
            holder.mTcRecordMoney.setText(mContext.getString(R.string.positive_number, bean.recordMoney));
    }

    @Override
    public int getItemCount() {
        return (mList == null ? 0 : mList.size());
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private final TextView mTvRecordDate_monthAndDay;
        private final TextView mTvRecordDate_year;
        private final ImageView mTvRecordType_image;
        private final TextView mTvRecordType_text;
        private final TextView mTvRecordTitle;
        private final TextView mTcRecordMoney;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTvRecordDate_monthAndDay = itemView.findViewById(R.id.tv_date_month_and_day);
            mTvRecordDate_year = itemView.findViewById(R.id.tv_date_year);
            mTvRecordType_image = itemView.findViewById(R.id.iv_type_image);
            mTvRecordType_text = itemView.findViewById(R.id.tv_type_second_text);
            mTvRecordTitle = itemView.findViewById(R.id.tv_title);
            mTcRecordMoney = itemView.findViewById(R.id.tv_cost);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.OnClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            mListener.OnLongClick(getAdapterPosition());
            return false;
        }
    }

    public interface OnClickListener {
        void OnClick(int index);

        void OnLongClick(int index);
    }
}
