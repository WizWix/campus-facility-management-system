package io.github.wizwix.cfms.dto.response.dorm;

/// 기숙사 호실 정보
///
/// @param id           [Long] 호실 ID
/// @param roomNumber   [String] 호실 번호 (e.g. "M101")
/// @param floor        [int] 층
/// @param occupancy    [int] 현재 입주 인원 (0: 빈방, 1: 1명, 2: 만실)
/// @param residentName [String] 1명 있을 때 표시 (마스킹 처리, e.g. "홍O동"), null이면 빈방
public record ResponseDormRoom(Long id, String roomNumber, int floor, int occupancy, String residentName) {}
