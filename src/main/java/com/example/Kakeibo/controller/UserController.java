package com.example.Kakeibo.controller;

import com.example.Kakeibo.controller.form.UserForm;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.example.Kakeibo.service.UserService;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
//ログインとユーザ登録を行うコントローラー
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    HttpSession session;

    /*
     * ログイン画面表示
     */
    @GetMapping("/login")
    public ModelAndView loginView() {
        ModelAndView mav = new ModelAndView();
        // 画面遷移先を指定
        mav.setViewName("/login");
        //エラーメッセージ表示
        List<String> errorMessage = (List<String>) session.getAttribute("errorMessages");
        String email = (String) session.getAttribute("email");
        if (errorMessage != null) {
            mav.addObject("errorMessages", errorMessage);
            mav.addObject("email", email);
            session.invalidate();
        }

        //ログインフィルターのエラーメッセージをmavに詰めてセッション削除
        List<String> filterErrorMessages = (List<String>) session.getAttribute("filterErrorMessages");
        mav.addObject("filterErrorMessages", filterErrorMessages);
        session.removeAttribute("filterErrorMessages");

        return mav;
    }

    /*
     * ログイン処理
     */
    @GetMapping("/loginUser")
    public ModelAndView login(@RequestParam(name = "email", required = false) String email,
                              @RequestParam(name = "password", required = false) String password) {
        //バリデーション
        //エラーメッセージの準備
        List<String> errorMessages = new ArrayList<String>();
        //社員番号入力チェック
        if (email.isBlank()) {
            errorMessages.add("・社員番号を入力してください");
        }
        //パスワード入力チェック
        if (password.isBlank()) {
            errorMessages.add("・パスワードを入力してください");
        }
        //エラーメッセージが１つ以上ある場合
        if (errorMessages.size() != 0) {
            //セッションにエラーメッセージと社員番号を設定
            session.setAttribute("errorMessages", errorMessages);
            session.setAttribute("email", email);
            //ログイン画面にリダイレクト
            return new ModelAndView("redirect:/login");
        }
        //リクエストから取得した社員番号をもとにユーザ情報を取得
        UserForm loginUser = userService.selectLoginUser(email);
        //バリデーション
        //ユーザが存在しないか停止中またはパスワードが違えばエラーメッセージをセット
        if (loginUser == null /*|| !BCrypt.checkpw(password, loginUser.getPassword())*/) {
            errorMessages.add("・ログインに失敗しました");
        }
        //エラーメッセージが１つ以上ある場合
        if (errorMessages.size() != 0) {
            //セッションにエラーメッセージと社員番号を設定
            session.setAttribute("errorMessages", errorMessages);
            session.setAttribute("email", email);
            //ログイン画面にリダイレクト
            return new ModelAndView("redirect:/login");
        }
        //セッションにログインユーザ情報を詰める
        session.setAttribute("loginUser", loginUser);
        return new ModelAndView("redirect:/");
    }

    /*
     * ログアウト処理
     */
    @GetMapping("/logout")
    public ModelAndView logout() {
        //セッションのログイン情報を破棄
        session.invalidate();

        //ログイン画面にリダイレクト
        return new ModelAndView("redirect:/login");
    }
}
