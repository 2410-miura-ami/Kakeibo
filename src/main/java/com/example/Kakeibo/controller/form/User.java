package com.example.Kakeibo.controller.form;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class User {

    private int id;

    private String name;

    private String email;

    private String password;

    private Date createdDate;

    private Date updatedDate;
}
