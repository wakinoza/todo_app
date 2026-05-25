package com.example.demo.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.demo.entity.TodoItem;
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
  public String list(Model model) {

    List<TodoItem> todoItemList = todoService.findAllTasks();
    model.addAttribute("todoItemList", todoItemList);

    return "main";
  }

  /**
   * 新規作成 (POST /todo/create)
   */
  @PostMapping("/create")
  public String create(@RequestParam String text, Model model) {

    if (text == null || text.trim().isEmpty()) {
      model.addAttribute("errorMsg", "Todoを入力してください。");

    } else if (text.length() > 100) {
      model.addAttribute("errorMsg", "Todoは100文字以内で入力してください。");
      model.addAttribute("enteredText", text);
    } else {
      TodoItem item = new TodoItem();
      item.setText(text);
      todoService.save(item);
    }

    model.addAttribute("todoItemList", todoService.findAllTasks());
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
