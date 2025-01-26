package com.example.Kakeibo.repository;

import com.example.Kakeibo.repository.entity.Record;
import com.example.Kakeibo.repository.entity.SmallCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SmallCategoryRepository extends JpaRepository<SmallCategory, Integer> {

    @Query(
            value = "SELECT " +
                    "SUM(records.amount) AS totalAmount, " +
                    "records.small_category_id AS smallCategoryId, " +
                    "small_categories.name AS name " +
                    "FROM records " +
                    "INNER JOIN small_categories " +
                    "ON records.small_category_id = small_categories.id " +
                    "WHERE user_id = :userId " +
                    "AND records.bop = 2 " +
                    "AND records.big_category_id = :bigCategoryId " +
                    "AND records.date BETWEEN :startDate AND :endDate " +
                    "GROUP BY records.small_category_id " +
                    "ORDER BY totalAmount DESC ",
            nativeQuery = true
    )
    public List<Object[]> findBySmallCategory(@Param("userId") int userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("bigCategoryId") int bigCategoryId);
}
