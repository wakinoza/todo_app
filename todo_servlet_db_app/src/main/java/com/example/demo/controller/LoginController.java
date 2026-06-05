package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import lombok.extern.slf4j.Slf4j;


@Controller
@Slf4j
public class LoginController {

  /**
   * ログイン画面を表示します。
   */
  @GetMapping("/login")
  public String viewLogin() {
    log.info("ログイン画面（/login）がリクエストされました。");
    return "login";
  }

}
