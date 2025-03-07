package com.example.Kakeibo.controller;

import com.example.Kakeibo.controller.form.BigSmallCategoryForm;
import com.example.Kakeibo.controller.form.RecordForm;
import com.example.Kakeibo.controller.form.TempData;
import com.example.Kakeibo.controller.form.UserForm;
import com.example.Kakeibo.service.BigSmallCategoryService;
import com.example.Kakeibo.service.RecordService;
import com.example.Kakeibo.service.SmallCategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

        //【追加】編集するidチェックのエラーメッセージを取得
        List<String> ErrorMessages = (List<String>)session.getAttribute("errorMessages");
        mav.addObject("errorMessages", ErrorMessages);
        session.removeAttribute("errorMessages");

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
                //三浦追加sessionにdateをセット
                session.setAttribute("date", date);
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
        TempData tempData = new TempData();
        mav.addObject("record", recordForm);
        mav.addObject("tempData", tempData);
        mav.setViewName("new_record");
        return mav;
    }
    /*
     * 記録登録処理
     */
    @PostMapping("/newRecord")
    public ModelAndView postNewRecord(@Validated RecordForm reqRecord, BindingResult result, TempData tempData){
        List<String> errorMessages = new ArrayList<>();
        if (result.hasErrors()) {
            //エラーがあったら、エラーメッセージを格納する
            //エラーメッセージの取得
            for (FieldError error : result.getFieldErrors()) {
                String message = error.getDefaultMessage();
                //取得したエラーメッセージをエラーメッセージのリストに格納
                errorMessages.add(message);
            }
        }
        if (!errorMessages.isEmpty()) {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("new_record");
            mav.addObject("errorMessages", errorMessages);
            mav.addObject("record", reqRecord);
            mav.addObject("tempData", tempData);
            return mav;
        }
        recordService.insert(reqRecord);
        return new ModelAndView("redirect:/");
    }
    /*
     * 記録編集画面表示
     */
    @GetMapping("/editRecord/{id}")
    public ModelAndView getEditRecord(@PathVariable String id, Model model){
        ModelAndView mav = new ModelAndView();

        //セッションから遷移元画面の識別子を取得
        String landmark = (String)session.getAttribute("landmark");
        //識別子から遷移元画面が家計簿画面だった場合は、セッションから小カテゴリIDを取得し画面にセット
        if(Objects.equals(landmark, "houseHold")){
            Integer smallCategoryId = (Integer)session.getAttribute("smallCategoryId");
            mav.addObject("smallCategoryId", smallCategoryId);
        }

        //バリデーション追加
        //編集する記録IDのチェック
        List<String> ErrorMessages = new ArrayList<>();
        if (id == null || !id.matches("^[0-9]+$")) {
            ErrorMessages.add("・不正なパラメータが入力されました");
            session.setAttribute("errorMessages", ErrorMessages);

            if(Objects.equals(landmark, "houseHold")){
                Integer smallCategoryId = (Integer)session.getAttribute("smallCategoryId");
                return new ModelAndView("redirect:/showRecord?smallCategoryId=" + smallCategoryId);
            } else if (Objects.equals(landmark, "history")) {
                String date = (String) session.getAttribute("date");
                return new ModelAndView("redirect:/showRecord?date=" + date);
            } else {
                return new ModelAndView("redirect:/");
            }

        }

        int recordId = Integer.parseInt(id);

        RecordForm result = recordService.select(recordId);

        //編集する記録IDの存在チェック
        if (result == null) {
            ErrorMessages.add("・不正なパラメータが入力されました");
            session.setAttribute("errorMessages", ErrorMessages);
            if(Objects.equals(landmark, "houseHold")){
                Integer smallCategoryId = (Integer)session.getAttribute("smallCategoryId");
                return new ModelAndView("redirect:/showRecord?smallCategoryId=" + smallCategoryId);
            } else if (Objects.equals(landmark, "history")) {
                String date = (String) session.getAttribute("date");
                return new ModelAndView("redirect:/showRecord?date=" + date);
            } else {
                return new ModelAndView("redirect:/");
            }
        }

        List<String> errorMessages = (List<String>)model.getAttribute("errorMessages");
        if(errorMessages != null && !errorMessages.isEmpty()){
            RecordForm recordForm = (RecordForm) model.getAttribute("record");

            mav.addObject("record", recordForm);
            mav.addObject("errorMessages", errorMessages);
        }else {
            mav.addObject("record", result);
        }

        mav.addObject("landmark", landmark);
        mav.setViewName("edit_record");
        return mav;
    }

    /*
     * 記録編集処理
     */
    @PostMapping("/updateRecord")
    public ModelAndView getEditRecord(@ModelAttribute @Validated RecordForm recordForm, BindingResult result,  RedirectAttributes redirectAttribute){
        ModelAndView mav = new ModelAndView();

        Integer id = recordForm.getId();

        List<String> errorMessages = new ArrayList<>();
        if (result.hasErrors()) {
            //エラーがあったら、エラーメッセージを格納する
            //エラーメッセージの取得
            for (FieldError error : result.getFieldErrors()) {
                String message = error.getDefaultMessage();
                //取得したエラーメッセージをエラーメッセージのリストに格納
                errorMessages.add(message);
            }
        }
        if (!errorMessages.isEmpty()) {
            redirectAttribute.addFlashAttribute("errorMessages", errorMessages);
            redirectAttribute.addFlashAttribute("record", recordForm);
            return new ModelAndView("redirect:/editRecord/" + id);
        }

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

    /*
     * 編集する記録Idが空白の時
     *個別記録編集画面
     */
    @GetMapping("/editRecord/")
    public ModelAndView editRecordInvalid() {
        ModelAndView mav = new ModelAndView();

        List<String> errorMessages = new ArrayList<>();
        errorMessages.add("・不正なパラメータが入力されました");
        session.setAttribute("errorMessages", errorMessages);

        //セッションから遷移元画面の識別子を取得
        String landmark = (String)session.getAttribute("landmark");
        if(Objects.equals(landmark, "houseHold")){
            Integer smallCategoryId = (Integer)session.getAttribute("smallCategoryId");
            return new ModelAndView("redirect:/showRecord?smallCategoryId=" + smallCategoryId);
        } else if (Objects.equals(landmark, "history")) {
            String date = (String) session.getAttribute("date");
            return new ModelAndView("redirect:/showRecord?date=" + date);
        }else {
            return new ModelAndView("redirect:/");
        }
    }

    /*
     * 記録削除処理
     */
    @GetMapping("/deleteRecord/{id}")
    public ModelAndView deleteRecord(@PathVariable int id){

        RecordForm recordForm = recordService.select(id);
        recordService.delete(id);

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
