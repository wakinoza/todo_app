package com.example.demo.config;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import com.example.demo.entity.User;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("SecurityConfigのログアウト動作テスト")
class SecurityConfigTest {

  @Autowired
  private MockMvc mockMvc;

  @Nested
  @DisplayName("ログアウトハンドラーの分岐テスト")
  class LogoutHandlerTest {

    @Test
    @DisplayName("1. ログインユーザーがログアウトした場合、ユーザー名がログ出力され、ログイン画面へリダイレクトされること")
    void shouldLogUserInfoOnLogout() throws Exception {
      User mockUser = new User();
      mockUser.setId(1);
      mockUser.setName("Alice");

      mockMvc.perform(post("/logout").with(csrf()).with(user(mockUser)))
          .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/login?logout"));
    }

    @Test
    @DisplayName("2. 未ログイン状態でログアウトした場合、ゲスト用のログが出力され、ログイン画面へリダイレクトされること")
    void shouldLogGuestInfoOnLogout() throws Exception {
      mockMvc.perform(post("/logout").with(csrf()).with(anonymous()))
          .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/login?logout"));
    }
  }
}
