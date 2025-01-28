package com.example.Kakeibo.controller;

import com.example.Kakeibo.controller.form.BigSmallCategoryForm;
import com.example.Kakeibo.controller.form.RecordForm;
import com.example.Kakeibo.service.BigSmallCategoryService;
import com.example.Kakeibo.service.RecordService;
import com.example.Kakeibo.service.SmallCategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
//記録の登録編集・個別記録画面表示を行うコントローラー
public class RecordController {

    @Autowired
    BigSmallCategoryService bigSmallCategoryService;

    @Autowired
    SmallCategoryService smallCategoryService;

    @Autowired
    HttpSession session;

    /*
     * 個別記録画面表示
     */

    @GetMapping("/showRecord")
    public ModelAndView selectRecord(@RequestParam(required = false) String date, @RequestParam(required = false) String smallCategoryId, @RequestParam(required = false) Integer bidCategoryId) {
        ModelAndView mav = new ModelAndView();

        //ログインユーザ情報を取得
        //UserForm loginUser = (UserForm) session.getAttribute("loginUser");
        //Integer loginId = loginUser.getId();
        Integer loginId = 1;
        List<BigSmallCategoryForm> results = new ArrayList<>();

        List<String> errorMessages = new ArrayList<>();
        //バリデーションsmallCategoryIdの表記チェック
        if (smallCategoryId != null && (smallCategoryId.isEmpty() || !smallCategoryId.matches("^[0-9]+$"))) {
            errorMessages.add("・不正なパラメータが入力されました");
            session.setAttribute("errorMessages", errorMessages);
            return new ModelAndView("redirect:/houseHold/" + bidCategoryId);
        }
        if(smallCategoryId != null) {
            int smallCategory = Integer.parseInt(smallCategoryId);
            //存在チェック（smallCategoryIdが登録されているカテゴリかチェック）
            if (!smallCategoryService.findById(smallCategory)) {
                errorMessages.add("・不正なパラメータが入力されました");
                session.setAttribute("errorMessages", errorMessages);
                return new ModelAndView("redirect:/houseHold/" + bidCategoryId);
            }

            //小カテゴリをもとに個別記録を取得
            results = bigSmallCategoryService.select(loginId, smallCategory);
            mav.addObject("back", "houseHold");
            mav.addObject("bigCategoryId", bidCategoryId);
        }

        //バリデーション（dateのnull・正規表現チェック）
        //nullまたはYYYY/MM/DDのフォーマットでない場合、エラーメッセージを表示
        if ((date != null) && (!date.matches("^\\d{4}-\\d{2}-\\d{2}$"))) {
            errorMessages.add("・不正なパラメータが入力されました");
            session.setAttribute("errorMessages", errorMessages);
            return new ModelAndView("redirect:/history");
        }

        if (date != null) {
            //バリデーション（dateが存在する日付かチェック）
            //リクエストパラメータで取得した日付をString型からLocalDate型に変換
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            try {
                LocalDate.parse(date, formatter);
                //LocalDate型への変換が失敗した場合、エラーメッセージを表示
            } catch (DateTimeParseException e) {
                errorMessages.add("・不正なパラメータが入力されました");
                session.setAttribute("errorMessages", errorMessages);
                mav.setViewName("redirect:/history");
                return mav;
            }

            //dateをもとに個別記録取得
            results = bigSmallCategoryService.select(loginId, date);
            mav.addObject("back", "history");
        }


        mav.addObject("records", results);
        mav.setViewName("/show_record");

        return mav;
    }
    @GetMapping("/newRecord")
    public ModelAndView getNewRecord(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("new_record");
        return mav;
    }
}
