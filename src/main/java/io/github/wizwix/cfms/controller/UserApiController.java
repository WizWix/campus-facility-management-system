package io.github.wizwix.cfms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserApiController {
  /// 내 간단 정보 (상단 메뉴용)
  @GetMapping("/me")
  public void me() {}

  /// 사용자 프로필 조회
  @GetMapping("/{id}")
  public void profile(@PathVariable String id) {}

  /// 내 정보 수정 (비밀번호, 이메일 등)
  @PatchMapping("/me")
  public void update() {}
}
