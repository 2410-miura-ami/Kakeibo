package com.example.Kakeibo.controller.form;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class RecordForm {

    private int id;

    private int amount;

    private int bop;

    private int smallCategoryId;

    private int bigCategoryId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String date;

    private String memo;

    private int userId;

    private Date createdDate;

    private Date updatedDate;
}
