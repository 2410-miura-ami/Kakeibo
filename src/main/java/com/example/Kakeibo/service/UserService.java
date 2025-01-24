package com.example.Kakeibo.service;

import com.example.Kakeibo.controller.form.UserForm;
import com.example.Kakeibo.repository.UserRepository;
import com.example.Kakeibo.repository.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

}
