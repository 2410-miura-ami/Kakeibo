package com.example.Kakeibo.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RecordBigCategoryForm {

    private int id;

    private String name;

    private BigDecimal totalAmount;

    private int bigCategoryId;
}
