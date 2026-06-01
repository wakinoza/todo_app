package com.example.demo.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserDetailsServiceImpl userDetailsService;

  @Nested
  @DisplayName("loadUserByUsername（ユーザー名による検索）のテスト")
  class LoadUserByUsernameTest {

    @Test
    @DisplayName("存在するユーザー名を指定した場合、対応するUserDetails（User）が取得できること")
    void shouldReturnUserDetailsWhenUserExists() {
      User dummyUser = new User();
      dummyUser.setId(1);
      dummyUser.setName("Alice");
      dummyUser.setPassword("hashed_password");
      when(userRepository.findByName("Alice")).thenReturn(Optional.of(dummyUser));

      UserDetails actualDetails = userDetailsService.loadUserByUsername("Alice");

      assertThat(actualDetails).isNotNull();
      assertThat(actualDetails.getUsername()).isEqualTo("Alice");
      assertThat(actualDetails.getPassword()).isEqualTo("hashed_password");

      assertThat(actualDetails).isInstanceOf(User.class);
    }

    @Test
    @DisplayName("存在しないユーザー名を指定した場合、UsernameNotFoundExceptionがスローされること")
    void shouldThrowExceptionWhenUserDoesNotExist() {

      when(userRepository.findByName("unknown_user")).thenReturn(Optional.empty());

      assertThatThrownBy(() -> userDetailsService.loadUserByUsername("unknown_user"))
          .isInstanceOf(UsernameNotFoundException.class)
          .hasMessageContaining("ユーザーが見つかりません: unknown_user");
    }
  }
}
