package io.github.wizwix.cfms.model.enums;

/// 시설 예약 신청 상태
///   * `APPROVED`: 시설 예약 신청 허가됨
///   * `CANCELLED`: 시설 예약 신청 (사용자에 의해) 취소됨
///   * `PENDING`: 시설 예약 신청 승인 대기 중
///   * `REJECTED`: 시설 예약 신청 승인 반려됨
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
