package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.Data;

/**
 * . ユーザー情報を保持するクラス
 */
@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {

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

  /**
   * ユーザーに与えられている権限（ロール）のリストを返します。 今回は一般ユーザーのみで権限の区別（管理者など）はしないため、空のリストを返します。
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  /**
   * 認証に使用する「ユーザー名」を返すメソッド。 Spring Securityに「うちのアプリでは 'name' フィールドがユーザー名だよ」と教えます。
   */
  @Override
  public String getUsername() {
    return this.name;
  }

  /**
   * 認証に使用する「パスワード」を返すメソッド
   */
  @Override
  public String getPassword() {
    return this.password;
  }

  /**
   * アカウントの有効期限が切れていないかを検証します。 今回は期限切れの仕組みはないため、常に「切れていない（true）」を返します。
   */
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  /**
   * アカウントがロックされていないかを検証します。 常に「ロックされていない（true）」を返します。
   */
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  /**
   * パスワードの有効期限が切れていないかを検証します。 常に「切れていない（true）」を返します。
   */
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  /**
   * アカウントが有効（利用可能）であるかを検証します。 常に「有効（true）」を返します。
   */
  @Override
  public boolean isEnabled() {
    return true;
  }
}

