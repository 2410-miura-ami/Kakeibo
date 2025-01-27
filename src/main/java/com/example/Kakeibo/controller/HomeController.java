package com.example.Kakeibo.controller;

import com.example.Kakeibo.controller.form.UserForm;
import com.example.Kakeibo.repository.entity.SevenMonthSummary;
import com.example.Kakeibo.service.RecordService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
//ホーム画面の表示を行うコントローラー
public class HomeController {

    @Autowired
    HttpSession session;
    @Autowired
    RecordService recordService;

    @GetMapping
    public ModelAndView top() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/home");
        //セッションにログインユーザ情報をつめる
        UserForm loginUser = (UserForm) session.getAttribute("loginUser");
        mav.addObject("loginUser", loginUser);

        //デフォルト表示月
        Date displayDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date firstDate = getFirstDate(displayDate);
        Date lastDate = getLastDate(displayDate);

        List<SevenMonthSummary> monthSummaries = recordService.findMonthSummaries(firstDate, lastDate);
        mav.addObject("monthSummaries", monthSummaries);

        String monthLabel[] = monthSummaries.stream().map(SevenMonthSummary::getMonth).toArray(String[]::new);
        BigDecimal incomeData[] = monthSummaries.stream().map(SevenMonthSummary::getIncomeTotalAmount).toArray(BigDecimal[]::new);
        BigDecimal expenseData[] = monthSummaries.stream().map(SevenMonthSummary::getExpenseTotalAmount).toArray(BigDecimal[]::new);
        mav.addObject("monthLabel", monthLabel);
        mav.addObject("incomeData", incomeData);
        mav.addObject("expenseData", expenseData);
        return mav;
    }

    // 月初日を返す
    public static Date getFirstDate(Date date) {

        if (date==null) return null;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int first = calendar.getActualMinimum(Calendar.DATE);
        calendar.set(Calendar.DATE, first);

        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.MILLISECOND, 000);

        return calendar.getTime();
    }

    // 月末日を返す
    public static Date getLastDate(Date date) {

        if (date==null) return null;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int last = calendar.getActualMaximum(Calendar.DATE);
        calendar.set(Calendar.DATE, last);

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        return calendar.getTime();
    }
}
