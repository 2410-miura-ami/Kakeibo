package com.example.Kakeibo.controller;

import com.example.Kakeibo.controller.form.BigSmallCategoryForm;
import com.example.Kakeibo.controller.form.RecordForm;
import com.example.Kakeibo.controller.form.UserForm;
import com.example.Kakeibo.service.BigSmallCategoryService;
import com.example.Kakeibo.service.RecordService;
import com.example.Kakeibo.service.SmallCategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    @Autowired
    SmallCategoryService smallCategoryService;


    /*
     * 個別記録画面表示
     */
    @GetMapping("/showRecord")
    public ModelAndView selectRecord(@RequestParam(required = false) String date, @RequestParam(required = false) String smallCategoryId) {
        ModelAndView mav = new ModelAndView();

        //ログインユーザ情報を取得
        UserForm loginUser = (UserForm) session.getAttribute("loginUser");
        Integer loginId = loginUser.getId();

        List<BigSmallCategoryForm> results = new ArrayList<>();

        //【追加】
        //大カテゴリIDをセッションから取得
        Integer bigCategoryId = (Integer)session.getAttribute("bigCategoryId");

        List<String> errorMessages = new ArrayList<>();
        //バリデーションsmallCategoryIdの表記チェック
        if (smallCategoryId != null && (smallCategoryId.isEmpty() || !smallCategoryId.matches("^[0-9]+$"))) {
            errorMessages.add("・不正なパラメータが入力されました");
            session.setAttribute("errorMessages", errorMessages);
            return new ModelAndView("redirect:/houseHold/" + bigCategoryId);
        }

        //遷移元からの引数が小カテゴリIDだった場合
        if(smallCategoryId != null){

            int smallCategory = Integer.parseInt(smallCategoryId);
            //存在チェック（smallCategoryIdが登録されているカテゴリかチェック）
            if (!smallCategoryService.findById(smallCategory)) {
                errorMessages.add("・不正なパラメータが入力されました");
                session.setAttribute("errorMessages", errorMessages);
                return new ModelAndView("redirect:/houseHold/" + bigCategoryId);
            }

            //セッションからselectの開始日と終了日を取得
            Date startDate = (Date)session.getAttribute("startDate");
            Date endDate = (Date)session.getAttribute("endDate");

            results = bigSmallCategoryService.select(loginId, smallCategory, startDate, endDate);
            //遷移元の識別子を画面とセッションにセット
            mav.addObject("landmark", "houseHold");
            session.setAttribute("landmark", "houseHold");
            //小カテゴリIDをセッションにセット
            session.setAttribute("smallCategoryId", smallCategory);
            //セッションから取得した大カテゴリIDを画面にセット
            mav.addObject("bigCategoryId", bigCategoryId);
        }

        //バリデーション（dateのnull・正規表現チェック）
        //nullまたはYYYY/MM/DDのフォーマットでない場合、エラーメッセージを表示
        if ((date != null) && (!date.matches("^\\d{4}-\\d{2}-\\d{2}$"))) {
            errorMessages.add("・不正なパラメータが入力されました");
            session.setAttribute("errorMessages", errorMessages);
            return new ModelAndView("redirect:/history");
        }

        //遷移元からの引数が日付だった場合
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
            //遷移元の識別子を画面とセッションにセット
            mav.addObject("landmark", "history");
            session.setAttribute("landmark", "history");
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

        BigSmallCategoryForm result = bigSmallCategoryService.select(id);

        //セッションから遷移元画面の識別子を取得
        String landmark = (String)session.getAttribute("landmark");
        session.removeAttribute("landmark");
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


}
