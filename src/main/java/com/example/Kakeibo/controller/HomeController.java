package com.example.Kakeibo.controller;

import com.example.Kakeibo.controller.form.RecordBigCategoryForm;
import com.example.Kakeibo.controller.form.UserForm;
import com.example.Kakeibo.repository.entity.SevenMonthSummary;
import com.example.Kakeibo.service.BigCategoryService;
import com.example.Kakeibo.service.RecordService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    @Autowired
    BigCategoryService bigCategoryService;

    public static Date displayDate = new Date();

    @GetMapping
    public ModelAndView top(@RequestParam(required = false) String num) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/home");
        //セッションにログインユーザ情報をつめる
        UserForm loginUser = (UserForm) session.getAttribute("loginUser");
        Integer loginId = loginUser.getId();
        mav.addObject("loginUser", loginUser);

        //デフォルト表示月
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(displayDate);
        if (num != null){
            int reqNum = Integer.parseInt(num);
            calendar.add(Calendar.MONTH, reqNum);
            displayDate  = calendar.getTime();
            return new ModelAndView("redirect:/");
        }

        Date firstDate = getFirstDate(displayDate);
        Date lastDate = getLastDate(displayDate);

        //前後3か月分の1月ごとの収支情報を取得
        List<SevenMonthSummary> monthSummaries = recordService.findMonthSummaries(loginId,firstDate, lastDate);
        mav.addObject("monthSummaries", monthSummaries);


        String monthLabel[] = monthSummaries.stream().map(SevenMonthSummary::getMonth).toArray(String[]::new);
        BigDecimal incomeData[] = monthSummaries.stream().map(SevenMonthSummary::getIncomeTotalAmount).toArray(BigDecimal[]::new);
        BigDecimal expenseData[] = monthSummaries.stream().map(SevenMonthSummary::getExpenseTotalAmount).toArray(BigDecimal[]::new);
        mav.addObject("monthLabel", monthLabel);
        mav.addObject("incomeData", incomeData);
        mav.addObject("expenseData", expenseData);

        //以下、円グラフ表示処理
        List<RecordBigCategoryForm> recordBigCategoryFormList = bigCategoryService.findByBigCategory(loginUser.getId(), firstDate, lastDate);
        //それぞれのListを作成し、そのListを配列に変換
        List<String> bigCategoryName = new ArrayList<>();
        List<BigDecimal> amountByCategory = new ArrayList<>();
        List<String> color = new ArrayList<>();
        if (recordBigCategoryFormList != null) {
            //それぞれのリストに追加
            for (RecordBigCategoryForm exBigCategory : recordBigCategoryFormList) {
                bigCategoryName.add(exBigCategory.getName());
                amountByCategory.add(exBigCategory.getTotalAmount());
                color.add(exBigCategory.getColor());
            }
        } else {
            bigCategoryName.add("未登録");
            amountByCategory.add(BigDecimal.valueOf(1));
            color.add("rgb(240, 240, 240)");
        }
        //配列に変換
        String expenseLabel[] = bigCategoryName.toArray(new String[bigCategoryName.size()]);
        BigDecimal bigCategoryData[] = amountByCategory.toArray(new BigDecimal[amountByCategory.size()]);
        String categoryColor[] = color.toArray(new String[color.size()]);

        //円グラフの表示のため配列を画面にセット
        mav.addObject("expenseLabel", expenseLabel);
        mav.addObject("bigCategoryData", bigCategoryData);
        mav.addObject("categoryColor", categoryColor);

        //支出と収入の総額を取得
        BigDecimal incomeTotalAmount = BigDecimal.valueOf(0);;//収入
        BigDecimal expenseTotalAmount = BigDecimal.valueOf(0);;//支出

        List<RecordBigCategoryForm> recordBopForms = bigCategoryService.findByBop(loginId, firstDate, lastDate);
        //incomeTotalAmount = (!recordBopForms.isEmpty()) ? recordBopForms.get(0).getTotalAmount() : BigDecimal.valueOf(0);
        //expenseTotalAmount = (recordBopForms.size() == 2) ? recordBopForms.get(1).getTotalAmount() : BigDecimal.valueOf(0);
        if (recordBopForms != null) {
            if (recordBopForms.size() == 2) {
                incomeTotalAmount = recordBopForms.get(0).getTotalAmount();
                expenseTotalAmount = recordBopForms.get(1).getTotalAmount();
            } else if (recordBopForms.size() == 1 && recordBopForms.get(0).getId() == 1) {
                incomeTotalAmount = recordBopForms.get(0).getTotalAmount();
            } else if (recordBopForms.size() == 1 && recordBopForms.get(0).getId() == 2) {
                expenseTotalAmount = recordBopForms.get(0).getTotalAmount();
            }
        }

        mav.addObject("expenditureByBigCategory", recordBigCategoryFormList);
        mav.addObject("incomeTotalAmount", incomeTotalAmount);
        mav.addObject("expenseTotalAmount", expenseTotalAmount);
        mav.addObject("startDate", firstDate);

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
