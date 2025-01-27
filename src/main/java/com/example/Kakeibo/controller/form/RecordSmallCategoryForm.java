package com.example.Kakeibo.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RecordSmallCategoryForm {

    private int id;

    private String name;

    private String color;

    private BigDecimal totalAmount;

    private int smallCategoryId;
}
