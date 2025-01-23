package com.example.Kakeibo.repository;

import com.example.Kakeibo.repository.entity.BigCategory;
import com.example.Kakeibo.repository.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BigCategoryRepository extends JpaRepository<BigCategory, Integer> {
}
