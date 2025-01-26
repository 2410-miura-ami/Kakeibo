package com.example.Kakeibo.service;

import com.example.Kakeibo.controller.form.RecordForm;
import com.example.Kakeibo.controller.form.RecordHistoryForm;
import com.example.Kakeibo.repository.entity.Record;
import com.example.Kakeibo.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
//記録の登録・編集を行うサービス
public class RecordService {

    @Autowired
    RecordRepository recordRepository;

    public List<RecordHistoryForm> select(String firstDay, String lastDay){

        List<Object[]> rawData = recordRepository.select(firstDay, lastDay);
        List<RecordHistoryForm> records = objectToForm(rawData);

        return records;
    }

    private List<RecordHistoryForm> objectToForm(List<Object[]> rawData){

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

}
