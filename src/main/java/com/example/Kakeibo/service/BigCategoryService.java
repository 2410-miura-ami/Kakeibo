package com.example.Kakeibo.service;

import com.example.Kakeibo.controller.form.BigCategoryForm;
import com.example.Kakeibo.controller.form.RecordBigCategoryForm;
import com.example.Kakeibo.repository.BigCategoryRepository;
import com.example.Kakeibo.repository.entity.BigCategory;
import com.example.Kakeibo.repository.entity.RecordBigCategory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
//大カテゴリとレコードを内部結合する処理記載
public class BigCategoryService {
    @Autowired
    BigCategoryRepository bigCategoryRepository;

    /*
     * 支出の大カテゴリ別記録情報（大カテゴリ名・金額総額）を取得
     */
    public List<RecordBigCategoryForm> findByBigCategory(int userId, Date startDate, Date endDate) {
        List<Object[]> results = bigCategoryRepository.findByBigCategory(userId, startDate, endDate);

        if(results.isEmpty()) {
            return null;
        }

        List<RecordBigCategoryForm> recordBigCategoryForms = setRecordBigCategoryForm(results);
        return recordBigCategoryForms;
    }

    public List<RecordBigCategoryForm> setRecordBigCategoryForm(List<Object[]> results) {
        List<RecordBigCategoryForm> recordBigCategoryForms = new ArrayList<>();

        for (Object[] result : results) {
            RecordBigCategoryForm recordBigCategoryForm = new RecordBigCategoryForm();
            BigDecimal totalAmount = (BigDecimal) result[0];
            int bigCategoryId = (int) result[1];
            String name = (String) result[2];
            String color = (String) result[3];
            recordBigCategoryForm.setTotalAmount(totalAmount);
            recordBigCategoryForm.setBigCategoryId(bigCategoryId);
            recordBigCategoryForm.setName(name);
            recordBigCategoryForm.setColor(color);
            recordBigCategoryForms.add(recordBigCategoryForm);
        }
        return recordBigCategoryForms;
    }

    /*
     * 支出と収入の総額を取得
     */
    public List<RecordBigCategoryForm> findByBop(int userId, Date startDate, Date endDate) {
        List<Object[]> results = bigCategoryRepository.findByBop(userId, startDate, endDate);

        if (results.isEmpty()) {
            return null;
        }

        List<RecordBigCategoryForm> recordBopForms = setRecordBopForm(results);
        return recordBopForms;
    }

    public List<RecordBigCategoryForm> setRecordBopForm(List<Object[]> results) {
        List<RecordBigCategoryForm> recordBopForms = new ArrayList<>();

        for (Object[] result : results) {
            RecordBigCategoryForm recordBopForm = new RecordBigCategoryForm();
            int bopId = (int) result[0];
            BigDecimal totalAmount = (BigDecimal) result[1];
            recordBopForm.setId(bopId);
            recordBopForm.setTotalAmount(totalAmount);
            recordBopForms.add(recordBopForm);
        }
        return recordBopForms;
    }

    /*
     * 家計簿（小）画面で表示する大カテゴリの金額（大カテゴリ名・金額総額）を取得
     */
    public List<RecordBigCategoryForm> findByBigCategory(int userId, Date startDate, Date endDate, int bigCategoryId) {
        List<Object[]> results = bigCategoryRepository.findAmountByBigCategory(userId, startDate, endDate, bigCategoryId);

        //未登録の月の処理
        //未登録だと、上記resultsの[0]:totalAmount入る部分と、[1]:bigCategoryId入る部分がnullで返ってくる
        if (results.get(0)[0] == null) {
            List<RecordBigCategoryForm> recordBigCategoryForms = new ArrayList<>();
            RecordBigCategoryForm recordBigCategoryForm = new RecordBigCategoryForm();
            recordBigCategoryForm.setTotalAmount(BigDecimal.valueOf(0));
            recordBigCategoryForm.setBigCategoryId(bigCategoryId);
            recordBigCategoryForm.setName((String) results.get(0)[2]);
            recordBigCategoryForm.setColor((String) results.get(0)[3]);
            recordBigCategoryForms.add(recordBigCategoryForm);
            return recordBigCategoryForms;
        }

        List<RecordBigCategoryForm> recordBigCategoryForms = setRecordBigCategoryForm(results);
        return recordBigCategoryForms;
    }

    /*
     * 渡ってきたbigCategoryIDの存在チェック
     */
    public boolean findById(int bigCategoryId) {
        BigCategory bigCategory = bigCategoryRepository.findById(bigCategoryId).orElse(null);
        if (bigCategory == null) {
            return false;
        }
        return true;
    }


}
