package com.example.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {

  private final UserService userService;

  @GetMapping("/login")
  public String viewLogin() {
    return "login"; // src/main/resources/templates/login.html を表示
  }

  @PostMapping("/login")
  public String login(@RequestParam String name, @RequestParam String pass,
      HttpServletRequest request, Model model) {

    User loginUser = userService.login(name, pass);

    if (loginUser != null) {
      // セッション固定攻撃対策
      HttpSession session = request.getSession(false);
      if (session != null) {
        session.invalidate();
      }
      session = request.getSession(true);
      session.setAttribute("loginUser", loginUser);

      // 2. CSRF対策
      // 本来はSpring Securityが自動で行いますが、自前で行う場合は
      // ここで生成し、Thymeleafのhiddenフィールド等に埋め込みます。

      return "loginResult";
    }

    model.addAttribute("error", "ユーザー名またはパスワードが正しくありません");
    return "login";
  }

  @GetMapping("/logout")
  public String logout(HttpSession session) {
    if (session != null) {
      session.invalidate();
    }

    return "logout";
  }
}
