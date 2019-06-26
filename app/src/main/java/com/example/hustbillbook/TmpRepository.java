package com.example.hustbillbook;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hustbillbook.bean.RecordBean;
import com.example.hustbillbook.bean.TypeViewBean;
import com.example.hustbillbook.tools.ImageUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/*
 For test only !!!
 */
public class TmpRepository {

    private List<TypeViewBean> allExpenseTypes;
    private List<TypeViewBean> allIncomeTypes;
    private static TmpRepository instance;

    // TODO 引用 strings.xml 文件来避免hardcode
    // TODO 考虑修改为 HashMap 结构以提高性能
    private TmpRepository(){
        allExpenseTypes = new ArrayList<>();
        allExpenseTypes.add(new TypeViewBean(RecordBean.Type.CANYIN.getId(), "餐饮", "img_canyin"));
        allExpenseTypes.add(new TypeViewBean(RecordBean.Type.DIANYING.getId(), "电影", "img_huafei"));
        allExpenseTypes.add(new TypeViewBean(RecordBean.Type.HUAFEI.getId(), "话费", "img_huafei"));
        allExpenseTypes.add(new TypeViewBean(RecordBean.Type.JIAOTONG.getId(), "交通", "img_jiaotong"));
        allExpenseTypes.add(new TypeViewBean(RecordBean.Type.YILIAO.getId(), "医疗", "img_yiliao"));
        allExpenseTypes.add(new TypeViewBean(RecordBean.Type.YIFU.getId(), "服饰", "img_yifu"));
        allExpenseTypes.add(new TypeViewBean(RecordBean.Type.FANGZU.getId(), "房租", "img_fangzu"));
        allExpenseTypes.add(new TypeViewBean(RecordBean.Type.SHUIGUO.getId(), "水果", "img_shuiguo"));
        allExpenseTypes.add(new TypeViewBean(RecordBean.Type.LINGSHI.getId(), "零食", "img_lingshi"));
        allExpenseTypes.add(new TypeViewBean(RecordBean.Type.SHUCAI.getId(), "蔬菜", "img_shucai"));
        allExpenseTypes.add(new TypeViewBean(RecordBean.Type.SHENGHUOYONGPIN.getId(), "生活用品", "img_shenghuoyongpin"));
        allExpenseTypes.add(new TypeViewBean(RecordBean.Type.TAOBAO.getId(), "淘宝", "img_taobao"));
        allExpenseTypes.add(new TypeViewBean(RecordBean.Type.ZHUANZHANG.getId(), "转账", "img_zhuanzhang"));
        allExpenseTypes.add(new TypeViewBean(RecordBean.Type.HONGBAO.getId(), "红包", "img_hongbao"));
        allExpenseTypes.add(new TypeViewBean(RecordBean.Type.QITA.getId(), "其他", "img_qita"));

        allIncomeTypes = new ArrayList<>();
        allIncomeTypes.add(new TypeViewBean(RecordBean.Type.GONGZI.getId(), "工资", "img_gongzi"));
        allIncomeTypes.add(new TypeViewBean(RecordBean.Type.LINGHUAQIAN.getId(), "零花钱", "img_linghuaqian"));
        allIncomeTypes.add(new TypeViewBean(RecordBean.Type.JIANZHI.getId(), "兼职", "img_jianzhi"));
        allIncomeTypes.add(new TypeViewBean(RecordBean.Type.SHENGHUOFEI.getId(), "生活费", "img_shenghuofei"));
        allIncomeTypes.add(new TypeViewBean(RecordBean.Type.HONGBAO.getId(), "红包", "img_hongbao"));
        allIncomeTypes.add(new TypeViewBean(RecordBean.Type.TOUZI.getId(), "投资", "img_touzi"));
        allIncomeTypes.add(new TypeViewBean(RecordBean.Type.JIANGJIN.getId(), "奖金", "img_jiangjin"));
        allIncomeTypes.add(new TypeViewBean(RecordBean.Type.QITA.getId(), "其他", "img_qita"));
    }

    public static TmpRepository getInstance() {
        if (instance == null) {
            instance = new TmpRepository();
        }
        return instance;
    }

    public List<TypeViewBean> getAllExpenseTypes() {
        return allExpenseTypes;
    }

    public List<TypeViewBean> getAllIncomeTypes() { return allIncomeTypes; }

    public TypeViewBean findType(int id) {
        for (TypeViewBean typeViewBean : allExpenseTypes) {
            if (typeViewBean.getId() == id)
                return typeViewBean;
        }
        for (TypeViewBean typeViewBean : allIncomeTypes) {
            if (typeViewBean.getId() == id)
                return typeViewBean;
        }
        return null;
    }
}
