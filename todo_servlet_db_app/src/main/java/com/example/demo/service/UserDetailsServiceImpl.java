package com.example.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;

/**
 * Spring SecurityがDBからユーザー情報を取得するための窓口クラス
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  /**
   * ユーザー名を受け取り、対応するユーザー情報をDBから検索して返します。
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    return userRepository.findByName(username)
        .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + username));
  }
}
