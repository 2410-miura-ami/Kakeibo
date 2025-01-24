package com.example.Kakeibo.repository;


import com.example.Kakeibo.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    //ログイン時のユーザ情報取得
    public List<User> findByEmail(String email);
}
