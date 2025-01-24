package com.example.Kakeibo.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserForm {

    private int id;

    private String name;

    private String email;

    private String password;

    private Date createdDate;

    private Date updatedDate;
}
