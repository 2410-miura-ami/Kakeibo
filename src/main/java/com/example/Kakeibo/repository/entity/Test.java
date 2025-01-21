package com.example.Kakeibo.repository.entity;

import jakarta.persistence.*;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tests")
@Getter
@Setter

public class Test {
    //参照時の使用するentity
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String test;

}

