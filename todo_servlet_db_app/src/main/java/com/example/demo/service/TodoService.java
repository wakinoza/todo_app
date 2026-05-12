package com.example.demo.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.entity.TodoItem;
import com.example.demo.repository.TodoItemRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoService {

  private final TodoItemRepository repository;

  /**
   * 全件取得
   */
  public List<TodoItem> findAllTasks() {
    return repository.findAll();
  }

  /**
   * タスクの保存
   */
  public void save(TodoItem item) {
    repository.save(item);
  }

  /**
   * 進捗更新
   */
  public void updateProgress(Integer id) {
    repository.findById(id).ifPresent(item -> {
      String current = item.getProgress();

      if ("未実施".equals(current)) {
        item.setProgress("実施中");
      } else if ("実施中".equals(current)) {
        item.setProgress("完了済");
      } else {
        repository.delete(item);
      }
    });
  }
}
