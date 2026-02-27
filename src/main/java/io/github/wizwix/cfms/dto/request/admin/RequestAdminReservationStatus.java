package io.github.wizwix.cfms.dto.request.admin;

import io.github.wizwix.cfms.model.enums.ReservationStatus;
import org.springframework.lang.Nullable;

/// 시설 예약 상태 변경 요청 (관리자)
/// 프론트(AdminPage.jsx ReservationStatusModal)에서 { status, rejectReason } 형태로 전송
/// ※ 필드명이 프론트와 일치해야 Jackson 역직렬화 가능 — "rejectReason"으로 통일
///
/// @param status       [ReservationStatus] APPROVED 또는 REJECTED
/// @param rejectReason [String] 거절 시 사유 (승인 시 null)
public record RequestAdminReservationStatus(ReservationStatus status, @Nullable String rejectReason) {}
