package io.github.wizwix.cfms.service.iface;

import io.github.wizwix.cfms.dto.request.reservation.RequestReservation;
import io.github.wizwix.cfms.dto.response.reservation.ResponseReservation;
import io.github.wizwix.cfms.model.enums.ReservationStatus;

import java.time.LocalDate;
import java.util.List;

public interface IReservationService {
  void cancelReservation(String userNumber, Long reservationId);

  ResponseReservation createReservation(String userNumber, RequestReservation req);

  List<ResponseReservation> getMyReservations(String userNumber);

  /// 관리자용 — 상태별 예약 목록 조회 (AdminApiController에서 호출)
  List<ResponseReservation> getReservationsByStatus(ReservationStatus status);

  List<ResponseReservation> getRoomReservations(Long roomId, LocalDate date);

  /// 관리자용 — 예약 승인/거절 처리 (AdminApiController에서 호출)
  /// PENDING → APPROVED 또는 REJECTED로 변경, 거절 시 rejectReason 저장
  void updateReservationStatus(Long id, ReservationStatus status, String rejectReason, String adminNumber);
}
