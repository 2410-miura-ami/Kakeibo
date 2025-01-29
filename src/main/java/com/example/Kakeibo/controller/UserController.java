package com.example.Kakeibo.controller;

import com.example.Kakeibo.controller.form.UserForm;
import com.example.Kakeibo.repository.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
            errorMessages.add("・メールアドレスを入力してください");
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
        if (loginUser == null  /*|| !BCrypt.checkpw(password, loginUser.getPassword())*/) {
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

    /*
     * ユーザ登録画面表示
     */
    @GetMapping("/signup")
    public ModelAndView signup() {
        ModelAndView mav = new ModelAndView();

        UserForm userForm = new UserForm();
        mav.addObject("user", userForm);

        mav.setViewName("/sign_up");
        return mav;
    }

    /*
     * ユーザ登録処理
     */
    @PostMapping("/signup")
    public ModelAndView newUser(@ModelAttribute("user") @Validated UserForm userForm, BindingResult result) {
        ModelAndView mav = new ModelAndView();

        //バリデーションチェック
        List<String> errorMessages = new ArrayList<>();

        if (userForm.getName().isBlank()) {
            errorMessages.add("・氏名を入力してください");
        }
        if (userForm.getEmail().isBlank()) {
            errorMessages.add("・メールアドレスを入力してください");
        }
        if (userForm.getPassword().isBlank()) {
            errorMessages.add("・パスワードを入力してください");
        }

        if(result.hasErrors()) {
            //エラーがあったら、エラーメッセージを格納する
            //エラーメッセージの取得
            for (FieldError error : result.getFieldErrors()) {
                String message = error.getDefaultMessage();
                //取得したエラーメッセージをエラーメッセージのリストに格納
                errorMessages.add(message);
            }
        }

        //妥当性チェック　パスワードと確認用パスワードが同一か
        if (!userForm.getPassword().equals(userForm.getPasswordConfirmation())){
            errorMessages.add("・パスワードと確認用パスワードが一致しません");
        }

        //重複チェック
        if(!userForm.getEmail().isBlank()) {
            UserForm selectedEmail = userService.findByEmail(userForm.getEmail());
            if (selectedEmail != null){
                errorMessages.add("・メールアドレスが重複しています");
            }
        }

        if(!errorMessages.isEmpty()) {
            //エラーメッセージに値があれば、エラーメッセージを画面にバインド
            mav.addObject("errorMessages", errorMessages);

            //入力情報の保持
            mav.addObject("user", userForm);
            mav.setViewName("/sign_up");
            return mav;
        }

        //リクエストから取得したパスワードの暗号化
        String encodedPwd = BCrypt.hashpw(userForm.getPassword(), BCrypt.gensalt());
        //登録するパスワードを暗号化したパスワードに変更
        userForm.setPassword(encodedPwd);

        //登録処理
        userService.saveUser(userForm);

        //ログイン画面へリダイレクト
        return new ModelAndView("redirect:/login");

    }

}
