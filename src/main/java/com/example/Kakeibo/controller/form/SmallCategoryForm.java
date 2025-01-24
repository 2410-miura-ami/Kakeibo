package com.example.Kakeibo.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SmallCategoryForm {

    private int id;

    private String name;

    private int bigCategoryId;

    private int bop;

    private Date createdDate;

    private Date updatedDate;
}
