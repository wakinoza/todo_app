package com.example.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Spring SecurityがDBからユーザー情報を取得するための窓口クラス
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  /**
   * ユーザー名を受け取り、対応するユーザー情報をDBから検索して返します。
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.debug("Spring Securityによる認証処理を開始しました。検索対象ユーザー名: [{}]", username);

    User user = userRepository.findByName(username).orElseThrow(() -> {
      log.warn("認証失敗: 指定されたユーザー名はデータベースに存在しません。入力値: [{}]", username);
      return new UsernameNotFoundException("ユーザーが見つかりません: " + username);
    });

    log.info("DBからのユーザー情報の取得に成功しました。ユーザー名: [{}]", user.getName());
    return user;
  }
}
