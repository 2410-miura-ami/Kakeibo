package com.example.Kakeibo.repository;

import com.example.Kakeibo.repository.entity.Record;
import com.example.Kakeibo.repository.entity.SmallCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmallCategoryRepository extends JpaRepository<SmallCategory, Integer> {
}
