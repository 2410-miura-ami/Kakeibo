package com.example.Kakeibo.repository;

import com.example.Kakeibo.repository.entity.Record;
import com.example.Kakeibo.repository.entity.SevenMonthSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface RecordRepository extends JpaRepository<Record, Integer> {

    @Query(
            value = "SELECT " +
                    "DATE_FORMAT(date, '%Y-%m') AS time, " +
                    "SUM(CASE WHEN bop = 1 THEN amount ELSE 0 END) AS incomeTotalAmount, " +
                    "SUM(CASE WHEN bop = 2 THEN amount ELSE 0 END) AS expenseTotalAmount " +
                    "FROM records " +
                    "WHERE date BETWEEN DATE_SUB( :firstDate , interval 3 month) " +
                    "AND DATE_ADD( :lastDate , interval 3 month) " +
                    "AND user_id = :loginId " +
                    "GROUP BY DATE_FORMAT(date, '%Y-%m') " +
                    "ORDER BY time ASC; ",
            nativeQuery = true
    )
    public List<Object[]> selectAmount7Month(@Param("loginId") Integer loginId, @Param("firstDate") Date firstDate, @Param("lastDate") Date lastDate);

    default List<SevenMonthSummary> findMonthSummaries(Integer loginId, Date firstDate, Date lastDate) {
        return selectAmount7Month(loginId, firstDate, lastDate).stream()
                .map(SevenMonthSummary::new)
                .collect(Collectors.toList());
    }

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
