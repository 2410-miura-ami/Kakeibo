package com.example.Kakeibo.repository;

import com.example.Kakeibo.repository.entity.BigCategory;
import com.example.Kakeibo.repository.entity.Record;
import com.example.Kakeibo.repository.entity.RecordBigCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BigCategoryRepository extends JpaRepository<BigCategory, Integer> {

    @Query(
            value = "SELECT " +
                    "SUM(records.amount) AS totalAmount, " +
                    "records.big_category_id AS bigCategoryId, " +
                    "big_categories.name AS name " +
                    "FROM records " +
                    "INNER JOIN big_categories " +
                    "ON records.big_category_id = big_categories.id " +
                    "WHERE user_id = :userId " +
                    "AND records.bop = 2 " +
                    "AND records.date BETWEEN :startDate AND :endDate " +
                    "GROUP BY records.big_category_id " +
                    "ORDER BY totalAmount DESC ",
            nativeQuery = true
    )
    public List<Object[]> findByBigCategory(@Param("userId") int userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);


    @Query(
            value = "SELECT " +
                    "records.bop AS bop, " +
                    "SUM(records.amount) AS totalAmount " +
                    "FROM records " +
                    "WHERE user_id = :userId " +
                    "AND records.date BETWEEN :startDate AND :endDate " +
                    "GROUP BY records.bop " +
                    "ORDER BY bop ASC ",
            nativeQuery = true
    )
    public List<Object[]> findByBop(@Param("userId") int userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(
            value = "SELECT " +
                    "SUM(records.amount) AS totalAmount, " +
                    "records.big_category_id AS bigCategoryId, " +
                    "big_categories.name AS name " +
                    "FROM records " +
                    "INNER JOIN big_categories " +
                    "ON records.big_category_id = big_categories.id " +
                    "WHERE user_id = :userId " +
                    "AND records.bop = 2 " +
                    "AND records.date BETWEEN :startDate AND :endDate " +
                    "AND records.big_category_id = :bigCategoryId ",
            nativeQuery = true
    )
    public List<Object[]> findAmountByBigCategory(@Param("userId") int userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("bigCategoryId") int bigCategoryId);

}
