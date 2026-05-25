package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * . ユーザー情報を保持するクラス
 */
@Entity
@Table(name = "users")
@Data
public class User {

  /** . ユーザーID */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /** . ユーザー名 */
  @Column(nullable = false, unique = true, length = 50)
  private String name;

  /** . パスワード */
  @Column(nullable = false, length = 100)
  private String password;
}
