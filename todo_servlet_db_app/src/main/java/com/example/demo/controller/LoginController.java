package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LoginController {

  /**
   * ログイン画面を表示します。
   */
  @GetMapping("/login")
  public String viewLogin() {
    return "login";
  }

}
