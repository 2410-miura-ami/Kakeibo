package com.example.Kakeibo.service;

import com.example.Kakeibo.controller.form.UserForm;
import com.example.Kakeibo.controller.form.BigSmallCategoryForm;
import com.example.Kakeibo.repository.RecordRepository;
import com.example.Kakeibo.repository.entity.SevenMonthSummary;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Session;
import org.springframework.beans.BeanUtils;
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
    @Autowired
    HttpSession session;

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
     * 新規記録登録処理
     */
    public void insert(RecordForm reqRecord){
        RecordForm recordForm = setBop(reqRecord);
        Record record = setRecordEntity(recordForm);
        recordRepository.save(record);
    }

    /*
     * エンティティに変換
     */
    private Record setRecordEntity(RecordForm reqRecord){
        Record record = new Record();
        BeanUtils.copyProperties(reqRecord, record);
        return record;
    }

    /*
     * 小カテゴリID、BOP、ログインユーザIDをつめる
     */
    private RecordForm setBop(RecordForm reqRecord){
        UserForm loginUser = (UserForm) session.getAttribute("loginUser");
        Integer loginId = loginUser.getId();
        reqRecord.setUserId(loginId);
        switch (reqRecord.getSmallCategoryId()) {
            case 1,2:
                reqRecord.setBop(1);
                reqRecord.setBigCategoryId(1);
                break;
            case 3,4:
                reqRecord.setBop(1);
                reqRecord.setBigCategoryId(2);
                break;
            case 5,6,7:
                reqRecord.setBop(2);
                reqRecord.setBigCategoryId(3);
                break;
            case 8,9,10,11:
                reqRecord.setBop(2);
                reqRecord.setBigCategoryId(4);
                break;
            case 12,13,14,15:
                reqRecord.setBop(2);
                reqRecord.setBigCategoryId(5);
                break;
            case 16,17,18,19:
                reqRecord.setBop(2);
                reqRecord.setBigCategoryId(6);
                break;
            case 20,21,22,23:
                reqRecord.setBop(2);
                reqRecord.setBigCategoryId(7);
                break;
            case 24,25,26,27,28:
                reqRecord.setBop(2);
                reqRecord.setBigCategoryId(8);
                break;
            case 29:
                reqRecord.setBop(2);
                reqRecord.setBigCategoryId(9);
                break;
            default:
                break;
        }
        return reqRecord;
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
     * idを元に記録を更新（記録編集処理）
     */
    public void update(RecordForm reqRecord, Integer id){
        RecordForm recordForm = setBop(reqRecord);
        Record record = setRecordEntity(recordForm);
        recordRepository.save(record);
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

