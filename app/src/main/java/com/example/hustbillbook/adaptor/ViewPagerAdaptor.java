package com.example.hustbillbook.adaptor;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class ViewPagerAdaptor extends PagerAdapter {

    protected final List<View> viewList;

    public ViewPagerAdaptor(List<View> viewList) {
        this.viewList = viewList;
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(viewList.get(position));
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
