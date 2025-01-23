package com.example.Kakeibo.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "records")
@Getter
@Setter
public class Record {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int amount;

    @Column
    private int bop;

    @Column
    private int smallCategoryId;

    @Column
    private int bigCategoryId;

    @Column
    private Date date;

    @Column
    private String memo;

    @Column
    private int userId;

    @Column(name = "created_date", insertable = true, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "updated_date", insertable = true, updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
}
