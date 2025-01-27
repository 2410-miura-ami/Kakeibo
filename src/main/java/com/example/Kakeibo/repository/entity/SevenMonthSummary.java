package com.example.Kakeibo.repository.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class SevenMonthSummary implements Serializable {
    private String month;
    private BigDecimal incomeTotalAmount;
    private BigDecimal expenseTotalAmount;

    public SevenMonthSummary(String month, BigDecimal incomeTotalAmount, BigDecimal expenseTotalAmount) {  //コンストラクタ
        super();
        this.month = month;
        this.incomeTotalAmount = incomeTotalAmount;
        this.expenseTotalAmount = expenseTotalAmount;
    }

    public SevenMonthSummary(Object[] objects) {
        this((String) objects[0], (BigDecimal) objects[1], (BigDecimal) objects[2]);
    }
}
