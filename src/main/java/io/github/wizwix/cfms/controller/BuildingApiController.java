package io.github.wizwix.cfms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/buildings")
@RequiredArgsConstructor
public class BuildingApiController {
  /// 전체 건물 목록 조회
  @GetMapping
  public void listAll() {}

  /// 건물의 방 목록
  @GetMapping("/{id}/rooms")
  public void listRooms(@PathVariable String id) {}
}
