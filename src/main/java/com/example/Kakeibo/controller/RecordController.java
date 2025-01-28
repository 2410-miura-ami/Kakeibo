package com.example.Kakeibo.controller;

import com.example.Kakeibo.controller.form.BigSmallCategoryForm;
import com.example.Kakeibo.controller.form.RecordForm;
import com.example.Kakeibo.controller.form.UserForm;
import com.example.Kakeibo.service.BigSmallCategoryService;
import com.example.Kakeibo.service.RecordService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
//記録の登録編集・個別記録画面表示を行うコントローラー
public class RecordController {

    @Autowired
    BigSmallCategoryService bigSmallCategoryService;

    @Autowired
    RecordService recordService;

    @Autowired
    HttpSession session;

    /*
     * 個別記録画面表示
     */
    @GetMapping("/showRecord")
    public ModelAndView selectRecord(@RequestParam(required = false) String date, @RequestParam(required = false) Integer smallCategoryId) {
        ModelAndView mav = new ModelAndView();

        //ログインユーザ情報を取得
        UserForm loginUser = (UserForm) session.getAttribute("loginUser");
        Integer loginId = loginUser.getId();

        List<BigSmallCategoryForm> results = new ArrayList<>();

        //遷移元からの引数が日付だった場合
        if(date != null){
            results = bigSmallCategoryService.select(loginId, date);
            //遷移元の識別子を画面とセッションにセット
            mav.addObject("landmark", "history");
            session.setAttribute("landmark", "history");
        }
        //遷移元からの引数が小カテゴリIDだった場合
        if(smallCategoryId != null){
            //セッションからselectの開始日と終了日を取得
            Date startDate = (Date)session.getAttribute("startDate");
            Date endDate = (Date)session.getAttribute("endDate");

            results = bigSmallCategoryService.select(loginId, smallCategoryId, startDate, endDate);
            //遷移元の識別子を画面とセッションにセット
            mav.addObject("landmark", "houseHold");
            session.setAttribute("landmark", "houseHold");
            //小カテゴリIDをセッションにセット
            session.setAttribute("smallCategoryId", smallCategoryId);
            //大カテゴリIDをセッションから取得し、画面にセット
            Integer bigCategoryId = (Integer)session.getAttribute("bigCategoryId");
            session.removeAttribute("bigCategoryId");
            mav.addObject("bigCategoryId", bigCategoryId);
        }

        mav.addObject("records", results);
        mav.setViewName("/show_record");

        return mav;
    }

    /*
     * 記録追加画面表示
     */
    @GetMapping("/newRecord")
    public ModelAndView getNewRecord(){
        ModelAndView mav = new ModelAndView();
        RecordForm recordForm = new RecordForm();
        mav.addObject("record", recordForm);
        mav.setViewName("new_record");
        return mav;
    }
    /*
     * 記録登録処理
     */
    @PostMapping("/newRecord")
    public ModelAndView postNewRecord(RecordForm reqRecord){
        recordService.insert(reqRecord);
        return new ModelAndView("redirect:/");
    }
    /*
     * 記録編集画面表示
     */
    @GetMapping("/editRecord/{id}")
    public ModelAndView getEditRecord(@PathVariable Integer id){
        ModelAndView mav = new ModelAndView();

        RecordForm result = recordService.select(id);

        //セッションから遷移元画面の識別子を取得
        String landmark = (String)session.getAttribute("landmark");
        //識別子から遷移元画面が家計簿画面だった場合は、セッションから小カテゴリIDを取得し画面にセット
        if(Objects.equals(landmark, "houseHold")){
            Integer smallCategoryId = (Integer)session.getAttribute("smallCategoryId");
            mav.addObject("smallCategoryId", smallCategoryId);
        }

        mav.addObject("record", result);
        mav.addObject("landmark", landmark);
        mav.setViewName("edit_record");
        return mav;
    }

    /*
     * 記録編集処理
     */
    @PostMapping("/updateRecord")
    public ModelAndView getEditRecord(@ModelAttribute RecordForm recordForm){
        ModelAndView mav = new ModelAndView();

        Integer id = recordForm.getId();
        /*Date date = new Date();
        recordForm.setCreatedDate(date);
        recordForm.setUpdatedDate(date);*/
        recordService.update(recordForm, id);

        String landmark = (String)session.getAttribute("landmark");
        if(Objects.equals(landmark, "houseHold")){
            Integer smallCategoryID = recordForm.getSmallCategoryId();
            return new ModelAndView("redirect:/showRecord?smallCategoryId=" + smallCategoryID);
        } else if (Objects.equals(landmark, "history")) {
            String date = recordForm.getDate();
            return new ModelAndView("redirect:/showRecord?date=" + date);
        }else {
            return new ModelAndView("redirect:/");
        }
    }
}
