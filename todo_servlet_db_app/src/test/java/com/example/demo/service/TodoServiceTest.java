package com.example.demo.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.demo.entity.TodoItem;
import com.example.demo.repository.TodoItemRepository;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

  @Mock
  private TodoItemRepository repository;

  @InjectMocks
  private TodoService todoService;

  @Test
  @DisplayName("findAllTasks：すべてのタスクが漏れなく取得できること")
  void shouldReturnAllTasks() {
    TodoItem item1 = new TodoItem();
    item1.setText("タスク1");
    TodoItem item2 = new TodoItem();
    item2.setText("タスク2");
    List<TodoItem> dummyList = List.of(item1, item2);
    when(repository.findAll()).thenReturn(dummyList);

    List<TodoItem> actualList = todoService.findAllTasks();

    assertThat(actualList).hasSize(2).containsSequence(item1, item2);
  }

  @Test
  @DisplayName("save：タスクが正しくリポジトリ経由で保存されること")
  void shouldCallRepositorySave() {
    TodoItem item = new TodoItem();
    item.setText("新しいタスク");

    todoService.save(item);

    verify(repository, times(1)).save(item);
  }

  @Nested
  @DisplayName("updateProgress:進捗がただしく更新されていること")
  class UpdateProgressTest {

    @Test
    @DisplayName("現在のステータスが【未実施】の場合、【実施中】に更新されること")
    void shouldUpdateToInDevelopmentWhenStatusIsTodo() {
      TodoItem item = new TodoItem();
      item.setId(1);
      item.setProgress("未実施");
      when(repository.findById(1)).thenReturn(Optional.of(item));

      todoService.updateProgress(1);

      assertThat(item.getProgress()).isEqualTo("実施中");
      verify(repository, never()).delete(any());
    }

    @Test
    @DisplayName("現在のステータスが【実施中】の場合、【完了済】に更新されること")
    void shouldUpdateToDoneWhenStatusIsInDevelopment() {
      TodoItem item = new TodoItem();
      item.setId(2);
      item.setProgress("実施中");
      when(repository.findById(2)).thenReturn(Optional.of(item));

      todoService.updateProgress(2);

      assertThat(item.getProgress()).isEqualTo("完了済");
      verify(repository, never()).delete(any());
    }

    @Test
    @DisplayName("現在のステータスが【完了済】の場合、タスクが物理削除されること")
    void shouldDeleteTaskWhenStatusIsDone() {
      TodoItem item = new TodoItem();
      item.setId(3);
      item.setProgress("完了済");
      when(repository.findById(3)).thenReturn(Optional.of(item));

      todoService.updateProgress(3);

      verify(repository, times(1)).delete(item);
    }

    @Test
    @DisplayName("指定したIDのタスクがDBに存在しない場合、何も処理が行われないこと")
    void shouldDoNothingWhenTaskDoesNotExist() {

      when(repository.findById(999)).thenReturn(Optional.empty());

      assertThatCode(() -> todoService.updateProgress(999)).doesNotThrowAnyException();

      verify(repository, never()).delete(any());
    }
  }
}
