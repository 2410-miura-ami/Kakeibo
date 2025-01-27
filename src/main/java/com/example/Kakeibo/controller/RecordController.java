package com.example.Kakeibo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
//記録の登録編集・個別記録画面表示を行うコントローラー
public class RecordController {
    @GetMapping("/newRecord")
    public ModelAndView getNewRecord(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("new_record");
        return mav;
    }
}
