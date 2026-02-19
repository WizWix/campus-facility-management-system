package io.github.wizwix.cfms.model.enums;

public enum ReservationStatus {
  /// 시설 예약 신청 허가됨
  APPROVED,
  /// 시설 예약 신청 취소됨
  CANCELLED,
  /// 시설 예약 신청 승인 대기 중
  PENDING,
  /// 시설 예약 신청 승인 반려됨
  REJECTED
}
