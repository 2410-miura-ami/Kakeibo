package com.example.Kakeibo.controller.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserForm {

    private int id;

    @Size(max = 10, message = "・氏名は10文字以下で入力してください")
    private String name;

    @Size(max = 50, message = "・メールアドレスは50文字以下で入力してください")
    private String email;

    private String password;

    private Date createdDate;

    private Date updatedDate;

    //追加
    private String passwordConfirmation;
}
