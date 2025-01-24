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
            recordBigCategoryForm.setName(name);
            recordBigCategoryForm.setTotalAmount(totalAmount);
            recordBigCategoryForm.setBigCategoryId(bigCategoryId);
            //BeanUtils.copyProperties(result, recordBigCategoryForm);
            recordBigCategoryForms.add(recordBigCategoryForm);
        }
        return recordBigCategoryForms;
    }

    /*
     * 支出と収入の総額を取得
     */
    public List<RecordBigCategoryForm> findByBop(int userId, Date startDate, Date endDate) {
        List<Object[]> results = bigCategoryRepository.findByBop(userId, startDate, endDate);

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


}
