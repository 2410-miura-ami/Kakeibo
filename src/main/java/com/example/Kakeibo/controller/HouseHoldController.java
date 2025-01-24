package com.example.Kakeibo.controller;

import com.example.Kakeibo.controller.form.BigCategoryForm;
import com.example.Kakeibo.controller.form.RecordBigCategoryForm;
import com.example.Kakeibo.controller.form.RecordSmallCategoryForm;
import com.example.Kakeibo.controller.form.UserForm;
import com.example.Kakeibo.repository.entity.Record;
import com.example.Kakeibo.service.BigCategoryService;
import com.example.Kakeibo.service.SmallCategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
//家計簿（大）家計簿（小）画面の表示をするコントローラー
public class HouseHoldController {
    @Autowired
    HttpSession session;

    @Autowired
    BigCategoryService bigCategoryService;

    @Autowired
    SmallCategoryService smallCategoryService;

    /*
     *家計簿（大）画面表示処理
     */
    @GetMapping("/houseHold")
    public ModelAndView houseHold() throws ParseException {
        ModelAndView mav = new ModelAndView();

        //ログインユーザ情報を取得
        //UserForm loginUser = (UserForm) session.getAttribute("loginUser");
        //Integer loginId = loginUser.getId();
        Integer loginId = 1;

        //【仮】表示月の取得
        Date now = new Date();
        Calendar calender = Calendar.getInstance();
        calender.setTime(now);
        calender.set(Calendar.DAY_OF_MONTH, 1);
        calender.set(Calendar.HOUR_OF_DAY, 0);
        calender.set(Calendar.MINUTE, 0);
        calender.set(Calendar.SECOND, 0);
        //Date型のフォーマット揃える
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String start = sdf.format(calender.getTime());
        Date startDate = sdf.parse(start);

        int endDay = calender.getActualMaximum(Calendar.DAY_OF_MONTH);
        calender.set(Calendar.DAY_OF_MONTH, endDay);
        //Date型のフォーマット揃える
        String end = sdf.format(calender.getTime());
        Date endDate = sdf.parse(end);

        //支出の大カテゴリ別記録情報（大カテゴリ名・金額総額）を取得
        List<RecordBigCategoryForm> recordBigCategoryFormList = bigCategoryService.findByBigCategory(loginId, startDate, endDate);

        //支出と収入の総額を取得
        BigDecimal incomeTotalAmount;
        BigDecimal expenseTotalAmount;

        List<RecordBigCategoryForm> recordBopForms = bigCategoryService.findByBop(loginId, startDate, endDate);
        incomeTotalAmount = recordBopForms.get(0).getTotalAmount();
        expenseTotalAmount = recordBopForms.get(1).getTotalAmount();

        mav.addObject("expenditureByBigCategory", recordBigCategoryFormList);
        mav.addObject("incomeTotalAmount", incomeTotalAmount);
        mav.addObject("expenseTotalAmount", expenseTotalAmount);
        mav.addObject("startDate", startDate);

        //画面遷移先を指定
        mav.setViewName("/big_household");

        //画面に遷移
        return mav;
    }

    /*
     *家計簿（小）画面表示処理
     */
    @GetMapping("/houseHold/{bigCategoryId}")
    public ModelAndView houseHoldSmall(@PathVariable("bigCategoryId") int bigCategoryId) throws ParseException {
        ModelAndView mav = new ModelAndView();

        //ログインユーザ情報を取得
        //UserForm loginUser = (UserForm) session.getAttribute("loginUser");
        //Integer loginId = loginUser.getId();
        Integer loginId = 1;

        //【仮】表示月の取得
        Date now = new Date();
        Calendar calender = Calendar.getInstance();
        calender.setTime(now);
        calender.set(Calendar.DAY_OF_MONTH, 1);
        calender.set(Calendar.HOUR_OF_DAY, 0);
        calender.set(Calendar.MINUTE, 0);
        calender.set(Calendar.SECOND, 0);
        //Date型のフォーマット揃える
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String start = sdf.format(calender.getTime());
        Date startDate = sdf.parse(start);

        int endDay = calender.getActualMaximum(Calendar.DAY_OF_MONTH);
        calender.set(Calendar.DAY_OF_MONTH, endDay);
        //Date型のフォーマット揃える
        String end = sdf.format(calender.getTime());
        Date endDate = sdf.parse(end);

        //支出の大カテゴリ別記録情報（小カテゴリ名・金額総額）を取得
        List<RecordSmallCategoryForm> recordSmallCategoryFormList = smallCategoryService.findBySmallCategory(loginId, startDate, endDate, bigCategoryId);

        mav.addObject("expenditureBySmallCategory", recordSmallCategoryFormList);

        //画面遷移先を指定
        mav.setViewName("/small_household");

        //画面に遷移
        return mav;
    }
}
