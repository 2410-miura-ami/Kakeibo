package com.example.Kakeibo.service;

import com.example.Kakeibo.controller.form.RecordBigCategoryForm;
import com.example.Kakeibo.controller.form.RecordSmallCategoryForm;
import com.example.Kakeibo.repository.SmallCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
//小カテゴリとレコードを内部結合する処理記載
public class SmallCategoryService {

    @Autowired
    SmallCategoryRepository smallCategoryRepository;

    /*
     * 支出の小カテゴリ別記録情報（小カテゴリ名・金額総額）を取得
     */
    public List<RecordSmallCategoryForm> findBySmallCategory(int userId, Date startDate, Date endDate, int bigCategoryId) {
        List<Object[]> results = smallCategoryRepository.findBySmallCategory(userId, startDate, endDate, bigCategoryId);

        List<RecordSmallCategoryForm> recordSmallCategoryForms = setRecordSmallCategoryForm(results);
        return recordSmallCategoryForms;
    }

    public List<RecordSmallCategoryForm> setRecordSmallCategoryForm(List<Object[]> results) {
        List<RecordSmallCategoryForm> recordSmallCategoryForms = new ArrayList<>();

        for (Object[] result : results) {
            RecordSmallCategoryForm recordSmallCategoryForm = new RecordSmallCategoryForm();
            BigDecimal totalAmount = (BigDecimal) result[0];
            int smallCategoryId = (int) result[1];
            String name = (String) result[2];
            recordSmallCategoryForm.setName(name);
            recordSmallCategoryForm.setTotalAmount(totalAmount);
            recordSmallCategoryForm.setSmallCategoryId(smallCategoryId);
            //BeanUtils.copyProperties(result, recordBigCategoryForm);
            recordSmallCategoryForms.add(recordSmallCategoryForm);
        }
        return recordSmallCategoryForms;
    }
}
