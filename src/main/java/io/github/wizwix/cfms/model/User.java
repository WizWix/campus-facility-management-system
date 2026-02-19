package io.github.wizwix.cfms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "cfms_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id; // 순수 인덱싱용
  private LocalDateTime createdAt;
  @Column(unique = true)
  private String email;
  private boolean enabled = true; // 탈퇴 시 Soft Delete 사용
  private String name;
  private String number; // 학번 or 교번 (e.g. '260004181')
  private String password; // BCrypt로 암호화된 비밀번호
  @Enumerated(EnumType.STRING)
  private UserRole role;
}
