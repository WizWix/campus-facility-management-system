package io.github.wizwix.cfms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clubs")
@RequiredArgsConstructor
public class ClubApiController {
  /// 동아리 개설 신청
  @PostMapping
  public void create() {}

  /// 동아리 상세 정보
  @GetMapping("/{slug}")
  public void info(@PathVariable String slug) {}

  /// 동아리 가입 신청
  @PostMapping("/{slug}/members")
  public void join(@PathVariable String slug) {}

  /// 부원 탈퇴/추방
  @DeleteMapping("/{slug}/members/{userId}")
  public void kick(@PathVariable String slug, @PathVariable String userId) {}

  /// 동아리 부원 목록
  @GetMapping("/{slug}/members")
  public void memberList(@PathVariable String slug) {}

  /// 동아리 목록 검색
  // TODO: `page`, `query` 구현
  @GetMapping
  public void search() {}

  /// 부원 역할 변경
  @PatchMapping("/{slug}/members/{userId}/role")
  public void setRole(@PathVariable String slug, @PathVariable String userId) {}

  /// 동아리 정보 수정
  @PatchMapping("/{slug}")
  public void update(@PathVariable String slug) {}
}
