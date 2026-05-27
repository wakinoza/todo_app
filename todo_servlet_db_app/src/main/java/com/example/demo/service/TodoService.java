package com.example.demo.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.entity.TodoItem;
import com.example.demo.repository.TodoItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * . TodoItemのサービスクラス
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TodoService {

  private final TodoItemRepository repository;

  /**
   * 全件取得
   */
  public List<TodoItem> findAllTasks() {
    List<TodoItem> list = repository.findAll();
    log.debug("Todo全件取得を実行しました。取得件数: {}件", list.size());
    return list;
  }

  /**
   * タスクの保存
   */
  public void save(TodoItem item) {
    repository.save(item);
    log.info("DBに新規Todoを保存しました。生成されたID: [{}], 内容: [{}]", item.getId(), item.getText());
  }

  /**
   * 進捗更新
   */
  public void updateProgress(Integer id) {
    log.debug("Todo進捗更新処理を開始します。対象ID: {}", id);
    repository.findById(id).ifPresentOrElse(item -> {
      String current = item.getProgress();

      if ("未実施".equals(current)) {
        item.setProgress("実施中");
        log.info("Todoの状態を更新しました。ID: [{}], 遷移: [未実施] -> [実施中]", id);
      } else if ("実施中".equals(current)) {
        item.setProgress("完了済");
        log.info("Todoの状態を更新しました。ID: [{}], 遷移: [実施中] -> [完了済]", id);
      } else {
        repository.delete(item);
        log.info("TodoをDBから物理削除しました。削除対象ID: [{}], 内容: [{}]", id, item.getText());
      }
    }, () -> {

      log.warn("進捗更新に失敗しました。指定されたTodo ID: [{}] はデータベースに存在しません。", id);
    });
  }
}
