package com.example.Kakeibo.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Entity
@Getter
@Setter
public class RecordBigCategory {
    //recordsとbog_categoriesの内部結合用エンティティ

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column(name = "totalAmount")
    private BigDecimal totalAmount;
}
