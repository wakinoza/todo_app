package com.example.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;

/**
 * . Userクラスのサービスクラス
 */
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;

  /**
   * . ログイン認証メソッド
   * 
   * @param name ユーザー名
   * @param rawPassword パスワード
   * @return 認証済みユーザー
   */
  public User login(String name, String rawPassword) {
    return repository.findByName(name)
        .filter(user -> passwordEncoder.matches(rawPassword, user.getPassword())).orElse(null);
  }
}
