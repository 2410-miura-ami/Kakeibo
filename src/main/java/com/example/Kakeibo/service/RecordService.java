package com.example.Kakeibo.service;

import com.example.Kakeibo.repository.RecordRepository;
import com.example.Kakeibo.repository.entity.SevenMonthSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
