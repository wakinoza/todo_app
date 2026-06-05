package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entity.TodoItem;

/**
 * . ToDoItemのリポジトリー
 */
@Repository
public interface TodoItemRepository extends JpaRepository<TodoItem, Integer> {

}
