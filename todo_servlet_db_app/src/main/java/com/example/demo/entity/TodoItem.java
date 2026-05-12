package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * . todo情報を保持するクラス
 */
@Entity
@Table(name = "todoItems")
@Data
public class TodoItem {

  /** . id */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /** . テキスト入力欄の文字列情報 */
  private String text;

  /** . 進捗情報を表す文字列情報 */
  private String progress = "未実施";
}
