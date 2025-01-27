package com.example.Kakeibo.controller;

import com.example.Kakeibo.controller.form.BigSmallCategoryForm;
import com.example.Kakeibo.controller.form.RecordForm;
import com.example.Kakeibo.service.BigSmallCategoryService;
import com.example.Kakeibo.service.RecordService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    @Autowired
    HttpSession session;

    /*
     * 個別記録画面表示
     */
    @GetMapping("/showRecord")
    public ModelAndView selectRecord(@RequestParam(required = false) String date, @RequestParam(required = false) Integer smallCategoryId) {
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
            Integer bigCategoryId = (Integer)session.getAttribute("bigCategoryId");
            mav.addObject("bigCategoryId", bigCategoryId);
        }

        mav.addObject("records", results);
        mav.setViewName("/show_record");

        return mav;
    }
}
