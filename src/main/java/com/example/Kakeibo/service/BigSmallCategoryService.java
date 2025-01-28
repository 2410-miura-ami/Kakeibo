package com.example.Kakeibo.service;

import com.example.Kakeibo.controller.form.BigSmallCategoryForm;
import com.example.Kakeibo.controller.form.RecordBigCategoryForm;
import com.example.Kakeibo.controller.form.RecordForm;
import com.example.Kakeibo.repository.BigSmallCategoryRepository;
import com.example.Kakeibo.repository.entity.BigSmallCategory;
import com.example.Kakeibo.repository.entity.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
//大カテゴリ・小カテゴリの両方とレコードを内部結合する処理記載
public class BigSmallCategoryService {

    @Autowired
    BigSmallCategoryRepository bigSmallCategoryRepository;

    /*
     * 該当日の全記録を取得（個別記録画面の表示）
     */
    public List<BigSmallCategoryForm> select(Integer loginId, String date) {
        List<Object[]> records = bigSmallCategoryRepository.select(loginId, date);
        List<BigSmallCategoryForm> results = objectToForm(records);

        return results;
    }

    /*
     * 指定の小カテゴリの全記録を取得（個別記録画面の表示）
     */
    public List<BigSmallCategoryForm> select(Integer loginId, Integer smallCategoryId, Date startDate, Date endDate) {
        List<Object[]> records = bigSmallCategoryRepository.select(loginId, smallCategoryId, startDate, endDate);
        List<BigSmallCategoryForm> results = objectToForm(records);

        return results;
    }

    /*
     * idを元に記録を取得（記録編集画面の表示）
     */
    public BigSmallCategoryForm select(Integer id) {
        List<Object[]> records = bigSmallCategoryRepository.select(id);
        List<BigSmallCategoryForm> results = objectToForm(records);

        return results.get(0);
    }

    /*
     * EntityをFormに変換（個別記録画面の表示）
     */
    private List<BigSmallCategoryForm> objectToForm(List<Object[]> records) {

        List<BigSmallCategoryForm> results = new ArrayList<>();

        for (Object[] record : records) {
            BigSmallCategoryForm result = new BigSmallCategoryForm();
            int id = (int) record[0];
            String date = (String) record[1];
            String bigCategoryName = (String) record[2];
            String smallCategoryName = (String) record[3];
            int amount = (int) record[4];
            int bop = (int) record[5];
            String memo = (String) record[6];
            Date createdDate = (Date) record[7];
            Date updatedDate = (Date) record[8];

            result.setId(id);
            result.setDate(date);
            result.setBigCategoryName(bigCategoryName);
            result.setSmallCategoryName(smallCategoryName);
            result.setAmount(amount);
            result.setBop(bop);
            result.setMemo(memo);
            result.setCreatedDate(createdDate);
            result.setUpdatedDate(updatedDate);

            results.add(result);
        }

        return results;
    }
}
