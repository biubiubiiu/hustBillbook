package com.example.hustbillbook.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hustbillbook.R;
import com.example.hustbillbook.TmpRepository;
import com.example.hustbillbook.bean.RecordBean;
import com.example.hustbillbook.bean.TypeViewBean;
import com.example.hustbillbook.tools.ImageUtils;

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
            view = mLayoutInflater.inflate(R.layout.item_recordlist, viewGroup, false);
            // 找到item布局文件中对应的控件
            viewHolder.mTvRecordDate_monthAndDay = view.findViewById(R.id.tv_date_month_and_day);
            viewHolder.mTvRecordDate_year = view.findViewById(R.id.tv_date_year);
            viewHolder.mTvRecordType_image = view.findViewById(R.id.iv_type_image);
            viewHolder.mTvRecordType_text = view.findViewById(R.id.tv_type_second_text);
            viewHolder.mTvRecordTitle = view.findViewById(R.id.tv_title);
            viewHolder.mTcRecordMoney = view.findViewById(R.id.tv_cost);
            // @see SDK:
            // Tags are essentially an extra piece of information that can be associated with a view
            // setTag 方法实际上是存储一部分View信息
            // 以减少创建多个View的性能消耗
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        // 获取相应索引的Bean对象
        RecordBean bean = mList.get(i);
        // 设置控件的对应属性值
        String[] array = bean.recordDate.split("-");
        viewHolder.mTvRecordDate_monthAndDay.setText(view.getContext().getString(R.string.month_day, array[1], array[2]));
        viewHolder.mTvRecordDate_year.setText(array[0]);

        TmpRepository r = TmpRepository.getInstance();
        TypeViewBean typeViewBean = r.findType(bean.recordType);
        viewHolder.mTvRecordType_image.setImageResource(ImageUtils.getImageId(mContext, typeViewBean.getTypeImg()));
        viewHolder.mTvRecordType_text.setText(typeViewBean.getTypeName());

        viewHolder.mTvRecordTitle.setText(bean.recordTitle);
        viewHolder.mTcRecordMoney.setText(bean.recordMoney);

        return view;
    }

    // Tag
    private static class ViewHolder {
        TextView mTvRecordDate_monthAndDay;
        TextView mTvRecordDate_year;
        ImageView mTvRecordType_image;
        TextView mTvRecordType_text;
        TextView mTvRecordTitle;
        TextView mTcRecordMoney;
    }
}
