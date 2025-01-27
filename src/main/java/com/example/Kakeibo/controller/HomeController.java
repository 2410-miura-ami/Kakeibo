package com.example.Kakeibo.controller;

import com.example.Kakeibo.controller.form.RecordBigCategoryForm;
import com.example.Kakeibo.controller.form.UserForm;
import com.example.Kakeibo.service.BigCategoryService;
import com.example.Kakeibo.service.RecordService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ModelAndView top() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/home");
        //セッションにログインユーザ情報をつめる
        UserForm loginUser = (UserForm) session.getAttribute("loginUser");
        mav.addObject("loginUser", loginUser);

        //デフォルト表示月
        //Date displayDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date firstDate = getFirstDate(displayDate);
        Date lastDate = getLastDate(displayDate);

        //recordService.selectAmount7Month(firstDate, lastDate);

        //[円グラフ表示]支出の大カテゴリ別記録情報（大カテゴリ名・金額総額）を取得
        List<RecordBigCategoryForm> recordBigCategoryFormList = bigCategoryService.findByBigCategory(loginUser.getId(), firstDate, lastDate);

        //円グラフ表示のため、大カテゴリ名・金額総額をそれぞれ配列に格納する
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
            color.add("rgb(245, 245, 245)");
        }
        //配列に変換
        String expenseLabel[] = bigCategoryName.toArray(new String[bigCategoryName.size()]);
        BigDecimal expenseData[] = amountByCategory.toArray(new BigDecimal[amountByCategory.size()]);
        String categoryColor[] = color.toArray(new String[color.size()]);

        //円グラフの表示のため配列を画面にセット
        mav.addObject("expenseLabel", expenseLabel);
        mav.addObject("expenseData", expenseData);
        mav.addObject("categoryColor", categoryColor);

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
