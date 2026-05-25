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

@Controller
@RequestMapping("/todo")
@RequiredArgsConstructor
public class TodoController {

  private final TodoService todoService;

  /**
   * 一覧表示 (GET /todo/list)
   */
  @GetMapping("/list")
  public String list(Model model, @AuthenticationPrincipal User loginUser) {

    if (loginUser != null) {
      model.addAttribute("loginUsername", loginUser.getName());
    } else {
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

    if (text == null || text.trim().isEmpty()) {
      // 💡addFlashAttribute を使うと、リダイレクト先まで1回だけデータを維持できる
      redirectAttributes.addFlashAttribute("errorMsg", "Todoを入力してください。");

    } else if (text.length() > 100) {
      redirectAttributes.addFlashAttribute("errorMsg", "Todoは100文字以内で入力してください。");
      redirectAttributes.addFlashAttribute("enteredText", text);
    } else {
      TodoItem item = new TodoItem();
      item.setText(text);
      todoService.save(item);
    }

    return "redirect:/todo/list";
  }

  /**
   * 進捗更新・削除 (POST /todo/update)
   */
  @PostMapping("/update")
  public String update(@RequestParam Integer id, Model model) {

    todoService.updateProgress(id);


    model.addAttribute("todoItemList", todoService.findAllTasks());
    return "redirect:/todo/list";
  }
}
