package io.github.wizwix.cfms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomApiController {
  /// 특정 방의 예약 목록
  // TODO: `date` 구현
  @GetMapping("/{roomId}/reservations")
  public void listReservations(@PathVariable String roomId) {}
}
