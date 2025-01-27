package com.example.Kakeibo.controller.form;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BigSmallCategoryForm {

    private int id;

    private String date;

    private String bigCategoryName;

    private String smallCategoryName;

    private int amount;

    private int bop;

    private String memo;

    private Date createdDate;

    private Date updatedDate;
}
