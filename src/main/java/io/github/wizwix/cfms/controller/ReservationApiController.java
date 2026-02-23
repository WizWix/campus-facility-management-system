package io.github.wizwix.cfms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationApiController {
  /// 시설 예약 신청
  @PostMapping
  public void createReservation() {}

  /// 예약 취소
  @DeleteMapping("/{id}")
  public void deleteReservation(@PathVariable String id) {}

  /// 내 예약 내역 조회
  @GetMapping("/me")
  public void myReservations() {}
}
