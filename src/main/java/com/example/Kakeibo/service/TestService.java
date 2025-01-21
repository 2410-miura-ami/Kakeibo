package com.example.Kakeibo.service;

import com.example.Kakeibo.repository.TestRepository;
import com.example.Kakeibo.repository.entity.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestService {

    @Autowired
    TestRepository testRepository;

    public String findAll(){

        List<Test> tests = testRepository.findAll();

        return tests.get(0).getTest();

    }
}
