package com.example.demo.controller;

import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.demo.entity.TodoItem;
import com.example.demo.entity.User;
import com.example.demo.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/todo")
@RequiredArgsConstructor
@Slf4j
public class TodoController {

  private final TodoService todoService;

  /**
   * 一覧表示 (GET /todo/list)
   */
  @GetMapping("/list")
  public String list(Model model, @AuthenticationPrincipal User loginUser) {

    if (loginUser != null) {
      log.info("Todo一覧画面へのアクセス。ログインユーザー: [{}] (ID: {})", loginUser.getName(), loginUser.getId());
      model.addAttribute("loginUsername", loginUser.getName());
    } else {
      log.warn("Todo一覧画面へのアクセスですが、ログインユーザー情報が取得できません。ゲストとして処理します。");
      model.addAttribute("loginUsername", "ゲスト");
    }

    List<TodoItem> todoItemList = todoService.findAllTasks();
    model.addAttribute("todoItemList", todoItemList);

    return "main";
  }

  /**
   * 新規作成 (POST /todo/create)
   */
  @PostMapping("/create")
  public String create(@RequestParam String text, RedirectAttributes redirectAttributes) {
    log.info("Todo新規作成リクエストを受信しました。入力された文字列: [{}]", text);

    if (text.trim().isEmpty()) {
      log.warn("Todo作成拒否: 入力値が空、または空白のみです。");
      redirectAttributes.addFlashAttribute("errorMsg", "Todoを入力してください。");

    } else if (text.length() > 100) {
      log.warn("Todo作成拒否: 文字数制限（100文字）を超過しています。入力サイズ: {} 文字", text.length());
      redirectAttributes.addFlashAttribute("errorMsg", "Todoは100文字以内で入力してください。");
      redirectAttributes.addFlashAttribute("enteredText", text);
    } else {
      TodoItem item = new TodoItem();
      item.setText(text);
      todoService.save(item);
      log.info("Todoの保存に成功しました。保存内容: [{}]", text);
    }

    return "redirect:/todo/list";
  }

  /**
   * 進捗更新・削除 (POST /todo/update)
   */
  @PostMapping("/update")
  public String update(@RequestParam Integer id, Model model) {
    log.info("Todo進捗更新リクエストを受信しました。対象Todo ID: [{}]", id);
    todoService.updateProgress(id);
    log.info("Todo ID: [{}] の進捗更新（または削除）が正常に完了しました。", id);

    model.addAttribute("todoItemList", todoService.findAllTasks());
    return "redirect:/todo/list";
  }
}
