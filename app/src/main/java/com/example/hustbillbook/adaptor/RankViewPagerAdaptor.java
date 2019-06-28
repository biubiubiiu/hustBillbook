package com.example.hustbillbook.adaptor;

import android.view.View;

import androidx.annotation.Nullable;

import java.util.List;

public class RankViewPagerAdaptor extends ViewPagerAdaptor {

    public RankViewPagerAdaptor(List<View> viewList) {
        super(viewList);
    }

    @Nullable
    @Override
    // 此处代码不可删除
    // 如果只在 xml 文件中设置标题
    // 在调用 setupWithViewPager 时
    // 会清空已有的 tab
    public CharSequence getPageTitle(int position) {
        if (position == 0)
            return "支出";
        else
            return "收入";
    }
}
