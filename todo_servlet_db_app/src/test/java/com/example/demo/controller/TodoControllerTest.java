package com.example.demo.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.example.demo.entity.TodoItem;
import com.example.demo.entity.User;
import com.example.demo.service.TodoService;

@WebMvcTest(TodoController.class)
@AutoConfigureMockMvc
class TodoControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private TodoService todoService;

  @Nested
  @DisplayName("list（一覧表示）のテスト")
  class ListTest {

    @Test
    @DisplayName("ログイン状態でアクセスした場合、ユーザー名とTodoリストが正しくModelに格納され、main画面に遷移すること")
    void shouldReturnMainViewWithLoggedInUser() throws Exception {
      User mockUser = new User();
      mockUser.setId(10);
      mockUser.setName("Alice");

      TodoItem item = new TodoItem();
      item.setId(1);
      item.setText("結合テストを書く");
      when(todoService.findAllTasks()).thenReturn(List.of(item));

      mockMvc.perform(get("/todo/list").with(user(mockUser))).andExpect(status().isOk())
          .andExpect(view().name("main")).andExpect(model().attribute("loginUsername", "Alice"))
          .andExpect(model().attribute("todoItemList", hasSize(1)));
    }

    @Test
    @DisplayName("未ログイン（ゲスト）でアクセスした場合、ユーザー名にゲストが設定されて一覧画面が表示されること")
    void shouldShowListAsGuestWhenNotLoggedIn() throws Exception {
      when(todoService.findAllTasks()).thenReturn(List.of());

      mockMvc.perform(get("/todo/list").with(user("guest"))).andExpect(status().isOk())
          .andExpect(view().name("main")).andExpect(model().attribute("loginUsername", "ゲスト"))
          .andExpect(model().attributeExists("todoItemList"));

      verify(todoService, times(1)).findAllTasks();
    }
  }

  @Nested
  @DisplayName("create（新規作成）のテスト")
  class CreateTest {

    @Test
    @DisplayName("正常な値（100文字以内）をPOSTした場合、Todoが保存され、一覧画面へリダイレクトされること")
    void shouldSaveTodoAndRedirectWhenInputIsValid() throws Exception {
      mockMvc.perform(post("/todo/create").param("text", "安全なタスク").with(csrf()).with(user("admin")))
          .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/todo/list"));

      verify(todoService, times(1)).save(any(TodoItem.class));
    }

    @Test
    @DisplayName("文字数が空（文字数0）の場合、保存されずにエラーメッセージがフラッシュ属性に格納されること")
    void shouldFailWithErrorMessageWhenInputIsEmpty() throws Exception {
      mockMvc.perform(post("/todo/create").param("text", "   ").with(csrf()).with(user("admin")))
          .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/todo/list"))
          .andExpect(flash().attribute("errorMsg", "Todoを入力してください。"));

      verify(todoService, never()).save(any());
    }

    @Test
    @DisplayName("入力値が100文字（境界値の最大値）の場合、正常に保存されて一覧画面へリダイレクトされること")
    void shouldSucceedWhenTextIsExactly100Characters() throws Exception {
      String maxValidText = "a".repeat(100);

      mockMvc
          .perform(
              post("/todo/create").param("text", maxValidText).with(csrf()).with(user("admin")))
          .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/todo/list"));

      verify(todoService, times(1)).save(any());
    }

    @Test
    @DisplayName("文字数が100文字を超過している場合、保存されずにエラーメッセージと入力値が引き継がれること")
    void shouldFailWithErrorMessageWhenInputExceedsLimit() throws Exception {
      String longText = "a".repeat(101);

      mockMvc.perform(post("/todo/create").param("text", longText).with(csrf()).with(user("admin")))
          .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/todo/list"))
          .andExpect(flash().attribute("errorMsg", "Todoは100文字以内で入力してください。"))
          .andExpect(flash().attribute("enteredText", longText));

      verify(todoService, never()).save(any());
    }

  }

  @Nested
  @DisplayName("update（進捗更新・削除）のテスト")
  class UpdateTest {

    @Test
    @DisplayName("有効なIDをPOSTした場合、進捗更新と再検索が走り、一覧画面へリダイレクトされること")
    void shouldUpdateProgressAndRedirect() throws Exception {
      TodoItem updatedItem = new TodoItem();
      updatedItem.setId(1);
      updatedItem.setText("進捗が更新されたタスク");
      updatedItem.setProgress("実施中");

      when(todoService.findAllTasks()).thenReturn(List.of(updatedItem));

      mockMvc.perform(post("/todo/update").param("id", "1").with(csrf()).with(user("admin")))
          .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/todo/list"));

      verify(todoService, times(1)).updateProgress(1);
    }
  }
}
