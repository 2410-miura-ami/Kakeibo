package com.example.Kakeibo.repository;

import com.example.Kakeibo.repository.entity.Record;
import com.example.Kakeibo.repository.entity.SevenMonthSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
                    "GROUP BY DATE_FORMAT(date, '%Y-%m') " +
                    "ORDER BY time ASC; ",
            nativeQuery = true
    )
    public List<Object[]> selectAmount7Month(@Param("firstDate") Date firstDate, @Param("lastDate") Date lastDate);

    default List<SevenMonthSummary> findMonthSummaries(Date firstDate, Date lastDate) {
        return selectAmount7Month(firstDate, lastDate).stream()
                .map(SevenMonthSummary::new)
                .collect(Collectors.toList());
    }
}
