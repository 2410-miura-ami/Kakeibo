package com.example.Kakeibo.controller.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.NumberFormat;

import java.util.Date;

@Getter
@Setter
public class RecordForm {

    private int id;

    //@NumberFormat(style = NumberFormat.Style.CURRENCY)
    @Min(value =1 ,message = "・無効な金額です")
    private int amount;

    private int bop;

    @Range(min = 1,max = 29, message = "・小カテゴリーまで入力してください")
    private int smallCategoryId;

    private int bigCategoryId;

    @NotBlank(message = "・日付を入力してください")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String date;

    @Size(max = 60, message = "・メモは50文字以下で入力してください")
    private String memo;

    private int userId;

    //追加（浦郷）
    public String bopName;
    public String bigCategoryName;

    private Date createdDate;

    private Date updatedDate;
}
