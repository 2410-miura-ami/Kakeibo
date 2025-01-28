package com.example.Kakeibo.service;

import com.example.Kakeibo.controller.form.BigSmallCategoryForm;
import com.example.Kakeibo.repository.RecordRepository;
import com.example.Kakeibo.repository.entity.SevenMonthSummary;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.Kakeibo.controller.form.RecordForm;
import com.example.Kakeibo.controller.form.RecordHistoryForm;
import com.example.Kakeibo.repository.entity.Record;
import com.example.Kakeibo.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
//記録の登録・編集を行うサービス
public class RecordService {

    @Autowired
    RecordRepository recordRepository;

    public List<SevenMonthSummary> findMonthSummaries(Integer loginId, Date firstDate, Date lastDate){
        List<SevenMonthSummary> monthSummaries = recordRepository.findMonthSummaries(loginId, firstDate, lastDate);
        return monthSummaries;
    }

    /*
     * 該当月の日付ごとの収入支出別合計を取得（履歴画面の表示）
     */
    public List<RecordHistoryForm> select(String firstDay, String lastDay, Integer loginId) {

        List<Object[]> rawData = recordRepository.select(firstDay, lastDay, loginId);
        List<RecordHistoryForm> records = objectToForm1(rawData);

        return records;
    }

    /*
     * object型のクエリ結果をFormに変換（履歴画面の表示）
     */
    private List<RecordHistoryForm> objectToForm1(List<Object[]> rawData) {

        List<RecordHistoryForm> result = new ArrayList<>();
        for (Object[] row : rawData) {
            int incomeAmount = ((BigDecimal) row[0]).intValue();
            int expenseAmount = ((BigDecimal) row[1]).intValue();
            String date = (String) row[2];

            RecordHistoryForm recordHistoryForm = new RecordHistoryForm();
            recordHistoryForm.setIncomeAmount(incomeAmount);
            recordHistoryForm.setExpenseAmount(expenseAmount);
            recordHistoryForm.setDate(date);

            result.add(recordHistoryForm);
        }
        return result;
    }

    /*
     * idを元に記録を取得（記録編集画面の表示）
     */
    public RecordForm select(Integer id) {

        List<Object[]> records = recordRepository.select(id);
        List<RecordForm> results = objectToForm2(records);

        return results.get(0);
    }

    /*
     * EntityをFormに変換（個別記録画面の表示）
     */
    private List<RecordForm> objectToForm2(List<Object[]> records) {

        List<RecordForm> results = new ArrayList<>();

        for (Object[] record : records) {
            RecordForm result = new RecordForm();
            int id = (int) record[0];
            String date = (String) record[1];
            Integer bigCategoryId = (Integer) record[2];
            Integer smallCategoryId = (Integer) record[3];
            int amount = (int) record[4];
            Integer bop = (Integer) record[5];
            String memo = (String) record[6];
            Integer userId = (Integer) record[7];
            Date createdDate = (Date) record[8];
            Date updatedDate = (Date) record[9];

            result.setId(id);
            result.setDate(date);
            result.setBigCategoryId(bigCategoryId);
            result.setSmallCategoryId(smallCategoryId);
            result.setAmount(amount);
            result.setBop(bop);
            result.setMemo(memo);
            result.setUserId(userId);
            result.setCreatedDate(createdDate);
            result.setUpdatedDate(updatedDate);

            results.add(result);
        }

        return results;
    }


}

