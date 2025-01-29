package com.example.Kakeibo.controller.form;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.util.Date;

@Getter
@Setter
public class RecordForm {

    private int id;

    //@NumberFormat(style = NumberFormat.Style.CURRENCY)
    @Min(value =1 ,message = "・無効な入力です")
    private int amount;

    private Integer bop;

    @Range(min = 1,max = 29, message = "・無効な入力です")
    @NotNull(message = "・無効な入力です")
    private int smallCategoryId;

    private int bigCategoryId;

    @NotBlank(message = "・日付を入力してください")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String date;

    @Size(max = 50, message = "・50文字以下で入力してください")
    private String memo;

    private int userId;

    private Date createdDate;

    private Date updatedDate;
}
