package com.example.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * . Userクラスのサービスクラス
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;

  // デバック用
  public User login(String name, String rawPassword) {
    log.error("★ログイン試行: name={}", name);
    return repository.findByName(name).filter(user -> {
      boolean match = passwordEncoder.matches(rawPassword, user.getPassword());
      log.error("★パスワード照合結果: {}", match);
      return match;
    }).orElse(null);
  }
  // 本番用
  // /**
  // * . ログイン認証メソッド
  // *
  // * @param name ユーザー名
  // * @param rawPassword パスワード
  // * @return 認証済みユーザー
  // */
  // public User login(String name, String rawPassword) {
  // return repository.findByName(name)
  // .filter(user -> passwordEncoder.matches(rawPassword, user.getPassword())).orElse(null);
  // }
}
