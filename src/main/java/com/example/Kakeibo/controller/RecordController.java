package com.example.Kakeibo.controller;

import com.example.Kakeibo.controller.form.BigSmallCategoryForm;
import com.example.Kakeibo.controller.form.RecordForm;
import com.example.Kakeibo.service.BigSmallCategoryService;
import com.example.Kakeibo.service.RecordService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
//記録の登録編集・個別記録画面表示を行うコントローラー
public class RecordController {

    @Autowired
    BigSmallCategoryService bigSmallCategoryService;

    /*
     * 個別記録画面表示
     */

    @GetMapping("/showRecord")
    public ModelAndView selectRecord(@RequestParam(required = false) String date, @RequestParam(required = false) Integer smallCategoryId, @RequestParam(required = false) Integer bidCategoryId) {
        ModelAndView mav = new ModelAndView();

        //ログインユーザ情報を取得
        //UserForm loginUser = (UserForm) session.getAttribute("loginUser");
        //Integer loginId = loginUser.getId();
        Integer loginId = 1;
        List<BigSmallCategoryForm> results = new ArrayList<>();

        if(date != null){
            results = bigSmallCategoryService.select(loginId, date);
            mav.addObject("back", "history");
        }
        if(smallCategoryId != null){
            results = bigSmallCategoryService.select(loginId, smallCategoryId);
            mav.addObject("back", "houseHold");
            mav.addObject("bigCategoryId", bidCategoryId);
        }

        mav.addObject("records", results);
        mav.setViewName("/show_record");

        return mav;
    }
    @GetMapping("/newRecord")
    public ModelAndView getNewRecord(){
        ModelAndView mav = new ModelAndView();
        RecordForm recordForm = new RecordForm();
        mav.addObject("recordForm", recordForm);
        mav.setViewName("new_record");
        return mav;
    }

    @PostMapping("/newRecord")
    public void postNewRecord(RecordForm reqRecord){
        ModelAndView mav = new ModelAndView();
    }
}
