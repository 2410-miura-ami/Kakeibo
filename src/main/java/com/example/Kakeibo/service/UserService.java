package com.example.Kakeibo.service;

import com.example.Kakeibo.controller.form.UserForm;
import com.example.Kakeibo.repository.UserRepository;
import com.example.Kakeibo.repository.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    /*
     * ログイン時のユーザ情報取得
     */
    public UserForm selectLoginUser(String email){
        //社員番号をもとにユーザ情報取得
        List<User> results = userRepository.findByEmail(email);
        //存在しないアカウントの場合nullを返す
        if (results.size() == 0) {
            return null;
        }
        //アカウントが存在した場合、ユーザ情報をentityからformに詰める
        List<UserForm> loginUser = setUserForm(results);
        return loginUser.get(0);
    }

    //フォームに詰める
    private List<UserForm> setUserForm(List<User> results) {
        List<UserForm> users = new ArrayList<>();

        for (User result : results) {
            UserForm user = new UserForm();
            BeanUtils.copyProperties(result, user);
            users.add(user);
        }
        return users;
    }

    /*
     * ユーザ新規登録
     */
    public void saveUser(UserForm repUser) {
        //エンティティに詰めて登録
        User user = setUserEntity(repUser);
        userRepository.save(user);
    }

    /*
     * エンティティに詰める
     */
    public User setUserEntity(UserForm reqUser) {
        User user = new User();
        BeanUtils.copyProperties(reqUser, user);

        Date nowDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(nowDate);
        try {
            user.setUpdatedDate(sdf.parse(currentTime));
            if (reqUser.getCreatedDate() == null) {
                user.setCreatedDate(sdf.parse(currentTime));
            } else {
                user.setCreatedDate(reqUser.getCreatedDate());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return user;
    }

    /*
     * メールアドレスの重複チェック
     */
    public UserForm findByEmail(String email) {
        List<User> results = userRepository.findByEmail(email);
        //存在しないあかうんとの場合、nullを返す
        if (results.size() == 0) {
            return null;
        }
        List<UserForm> userForm = setUserForm(results);
        return  userForm.get(0);
    }



}
