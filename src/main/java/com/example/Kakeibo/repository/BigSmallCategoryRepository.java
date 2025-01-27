package com.example.Kakeibo.repository;

import com.example.Kakeibo.repository.entity.BigSmallCategory;
import com.example.Kakeibo.repository.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BigSmallCategoryRepository extends JpaRepository<BigSmallCategory, Integer> {


    @Transactional
    @Query(value =
            "SELECT " +
                    "records.id AS id, " +
                    "CAST(DATE_FORMAT(records.date, '%Y-%m-%d') AS CHAR) AS date, " +
                    "big_categories.name AS bigCategoryName, " +
                    "small_categories.name AS smallCategoryName, " +
                    "records.amount AS amount, " +
                    "records.bop AS bop, " +
                    "records.memo AS memo, " +
                    "records.created_date AS createdDate, " +
                    "records.updated_date AS updatedDate " +
                    "FROM records " +
                    "INNER JOIN small_categories " +
                    "ON records.small_category_id = small_categories.id " +
                    "INNER JOIN big_categories " +
                    "ON records.big_category_id = big_categories.id " +
                    "WHERE DATE_FORMAT(records.date, '%Y-%m-%d') = :targetDate " +
                    "AND records.user_id = :loginId " +
                    "ORDER BY records.small_category_id ASC",
            nativeQuery = true
    )
    List<Object[]> select(@Param("loginId")Integer loginId, @Param("targetDate")String targetDate);

    @Transactional
    @Query(value =
            "SELECT " +
                    "records.id AS id, " +
                    "CAST(DATE_FORMAT(records.date, '%Y-%m-%d') AS CHAR) AS date, " +
                    "small_categories.name AS smallCategoryName, " +
                    "big_categories.name AS bigCategoryName, " +
                    "records.amount AS amount, " +
                    "records.bop AS bop, " +
                    "records.memo AS memo, " +
                    "records.created_date AS createdDate, " +
                    "records.updated_date AS updatedDate " +
                    "FROM records " +
                    "INNER JOIN small_categories " +
                    "ON records.small_category_id = small_categories.id " +
                    "INNER JOIN big_categories " +
                    "ON records.big_category_id = big_categories.id " +
                    "WHERE records.small_category_id = :smallCategoryId " +
                    "AND records.user_id = :loginId " +
                    "ORDER BY records.date DESC",
            nativeQuery = true
    )
    List<Object[]> select(@Param("loginId")Integer loginId, @Param("smallCategoryId")Integer smallCategoryId);
}
