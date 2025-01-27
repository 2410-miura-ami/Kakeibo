package com.example.Kakeibo.repository;

import com.example.Kakeibo.repository.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Integer> {

    @Transactional
    @Query(value =
            "SELECT " +
                    "SUM(CASE WHEN records.bop = 1 THEN records.amount ELSE 0 END) AS incomeTotalAmount, " +
                    "SUM(CASE WHEN records.bop = 2 THEN records.amount ELSE 0 END) AS expenseTotalAmount, " +
                    "DATE_FORMAT(records.date, '%Y-%m-%d') AS date " +
                    "FROM records " +
                    "WHERE records.date BETWEEN :firstDay AND :lastDay " +
                    "AND user_id = :loginId " +
                    "GROUP BY records.date " +
                    "ORDER BY records.date ASC" ,
            nativeQuery = true
    )
    public List<Object[]> select(@Param("firstDay") String firstDay, @Param("lastDay") String lastDay, @Param("loginId") Integer loginId);

}
