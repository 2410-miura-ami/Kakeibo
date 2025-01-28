package com.example.Kakeibo.controller;

import com.example.Kakeibo.controller.form.RecordForm;
import com.example.Kakeibo.controller.form.RecordHistoryForm;
import com.example.Kakeibo.controller.form.UserForm;
import com.example.Kakeibo.service.RecordService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
//履歴画面の表示を行うコントローラー
public class HistoryController {

    @Autowired
    RecordService recordService;

    @Autowired
    HttpSession session;

    /*
     * 履歴画面表示処理
     */
    @GetMapping("/history")
    public ModelAndView selectMonth(@RequestParam(required = false) String nextMonth, @RequestParam(required = false) String previousMonth) throws JsonProcessingException {
        ModelAndView mav = new ModelAndView();

        //ログインユーザ情報を取得
        UserForm loginUser = (UserForm) session.getAttribute("loginUser");
        Integer loginId = loginUser.getId();

        //現在日時から当月の初日と最終日を算出
        LocalDate today = LocalDate.now();

        LocalDate firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());

        if(session.getAttribute("firstDayOfMonth") != null){
            firstDayOfMonth = (LocalDate)session.getAttribute("firstDayOfMonth");
            lastDayOfMonth = (LocalDate)session.getAttribute("lastDayOfMonth");
            session.removeAttribute("firstDayOfMonth");
            session.removeAttribute("lastDayOfMonth");
        }

        if(nextMonth != null){
            firstDayOfMonth = LocalDate.parse(nextMonth).plusDays(2);
            lastDayOfMonth = firstDayOfMonth.with(TemporalAdjusters.lastDayOfMonth());
        }
        if(previousMonth != null) {
            lastDayOfMonth = LocalDate.parse(previousMonth).minusDays(1);
            firstDayOfMonth = lastDayOfMonth.with(TemporalAdjusters.firstDayOfMonth());
        }

        //日付をDate型からString型に変換
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String firstDay = firstDayOfMonth.format(formatter);
        String lastDay = lastDayOfMonth.format(formatter);

        session.setAttribute("firstDayOfMonth", firstDayOfMonth);
        session.setAttribute("lastDayOfMonth", lastDayOfMonth);

        List<RecordHistoryForm> results = recordService.select(firstDay, lastDay, loginId);

        ObjectMapper objectMapper = new ObjectMapper();
        String recordListJson = objectMapper.writeValueAsString(results);

        //用意したデータを画面にセット
        mav.addObject("date", firstDay);
        mav.addObject("recordList", recordListJson);
        mav.addObject("history");

        return mav;

    }

}
