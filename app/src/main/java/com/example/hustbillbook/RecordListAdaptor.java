package com.example.hustbillbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class RecordListAdaptor extends BaseAdapter {

    private List<RecordBean> mList;   // 数据源
    private Context mContext;       // 要使用当前Adapter的界面对象
    private LayoutInflater mLayoutInflater;     // 布局装载器对象

    // 通过构造方法将数据源与数据适配器关联起来
    public RecordListAdaptor(Context context, List<RecordBean> list) {
        mContext = context;
        mList = list;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    // ListView需要显示的数据数量
    public int getCount() {
        return mList.size();
    }

    @Override
    // 指定的索引对应的数据源
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    // 指定的索引对应的数据项ID
    public long getItemId(int i) { return i;}

    @Override
    // 返回每一项的显示内容
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            // 将布局文件转换成View对象
            view = mLayoutInflater.inflate(R.layout.item_recordlist, null);
            // 找到item布局文件中对应的控件
            viewHolder.mTvRecordTitle = view.findViewById(R.id.tv_title);
            viewHolder.mTvRecordDate = view.findViewById(R.id.tv_date);
            viewHolder.mTcRecordMoney = view.findViewById(R.id.tv_cost);
            // @see SDK:
            // Tags are essentially an extra piece of information that can be associated with a view
            //
            // setTag 方法实际上是存储一部分View信息
            // 以减少创建多个View的性能消耗
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        // 获取相应索引的Bean对象
        RecordBean bean = mList.get(i);
        // 设置控件的对应属性值
        viewHolder.mTvRecordTitle.setText(bean.recordTitle);
        viewHolder.mTvRecordDate.setText(bean.recordDate);
        viewHolder.mTcRecordMoney.setText(bean.recordMoney);
        return view;
    }

    // Tag
    private static class ViewHolder {

        public TextView mTvRecordTitle;
        public TextView mTvRecordDate;
        public TextView mTcRecordMoney;
    }
}
