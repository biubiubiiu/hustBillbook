package com.example.hustbillbook;

import android.util.SparseArray;

import com.example.hustbillbook.bean.TypeViewBean;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DataRepository {

    private final List<TypeViewBean> allExpenseTypes;
    private final List<TypeViewBean> allIncomeTypes;

    // allRecordTypes = allExpenseTypes + allIncomeTypes
    // 使用 SparseArray 替代 HashMap 以提高性能
    private final SparseArray<TypeViewBean> allRecordTypes;

    private final List<TypeViewBean> allAccountTypes;

    private static DataRepository instance;

    private DataRepository() {
        allExpenseTypes = new ArrayList<>();
        allIncomeTypes = new ArrayList<>();
        allAccountTypes = new ArrayList<>();

        String info = getJson("Types.json");
        extractInfo(info);

        allRecordTypes = new SparseArray<>();
        for (TypeViewBean typeViewBean : allExpenseTypes) {
            allRecordTypes.put(typeViewBean.getId(), typeViewBean);
        }
        for (TypeViewBean typeViewBean : allIncomeTypes) {
            allRecordTypes.put(typeViewBean.getId(), typeViewBean);
        }
    }

    public static DataRepository getInstance() {
        if (instance == null) {
            instance = new DataRepository();
        }
        return instance;
    }

    public SparseArray<TypeViewBean> getAllRecordTypes() {
        return allRecordTypes;
    }

    public List<TypeViewBean> getAllExpenseTypes() {
        return allExpenseTypes;
    }

    public List<TypeViewBean> getAllIncomeTypes() {
        return allIncomeTypes;
    }

    public List<TypeViewBean> getAllAccountTypes() {
        return allAccountTypes;
    }

    public TypeViewBean findType(int id) {
        for (TypeViewBean typeViewBean : allExpenseTypes) {
            if (typeViewBean.getId() == id)
                return typeViewBean;
        }
        for (TypeViewBean typeViewBean : allIncomeTypes) {
            if (typeViewBean.getId() == id)
                return typeViewBean;
        }
        for (TypeViewBean typeViewBean : allAccountTypes) {
            if (typeViewBean.getId() == id)
                return typeViewBean;
        }
        return null;
    }

    @NotNull
    private String getJson(String fileName) {
        InputStream is = DataRepository.this.getClass().getClassLoader().getResourceAsStream("assets/" + fileName);
        InputStreamReader streamReader = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(streamReader);
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private void extractInfoHelper(String info, String arrayName, List<TypeViewBean> list) {
        try {
            JSONObject person = new JSONObject(info);
            JSONArray array = person.getJSONArray(arrayName);
            for (int i = 0; i < array.length(); i++) {
                JSONObject expense_Array = array.getJSONObject(i);
                TypeViewBean typeViewBean = new TypeViewBean();
                typeViewBean.setId(expense_Array.getInt("id"));
                typeViewBean.setTypeName(expense_Array.getString("typeName"));
                typeViewBean.setTypeImg(expense_Array.getString("typeImg"));
                list.add(typeViewBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void extractInfo(String info) {
        extractInfoHelper(info, "expenseTypes", allExpenseTypes);
        extractInfoHelper(info, "incomeTypes", allIncomeTypes);
        extractInfoHelper(info, "accountTypes", allAccountTypes);
    }
}
