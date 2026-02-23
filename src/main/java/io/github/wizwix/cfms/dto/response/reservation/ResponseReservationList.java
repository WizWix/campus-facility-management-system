package io.github.wizwix.cfms.dto.response.reservation;

import io.github.wizwix.cfms.model.enums.ReservationStatus;

import java.time.LocalDateTime;

public record ResponseReservationList(Long reservationId, String roomCode, LocalDateTime startTime,
                                      LocalDateTime endTime, ReservationStatus status, String purpose) {}
