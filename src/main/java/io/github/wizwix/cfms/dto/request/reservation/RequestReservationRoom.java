package io.github.wizwix.cfms.dto.request.reservation;

import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

/// 공실 예약 요청
///
/// @param roomId 방 ID
public record RequestReservationRoom(Long roomId, @Nullable Long clubId, LocalDateTime startTime, LocalDateTime endTime,
                                     String purpose) {}
