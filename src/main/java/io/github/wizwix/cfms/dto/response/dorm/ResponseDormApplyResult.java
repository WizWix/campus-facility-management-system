package io.github.wizwix.cfms.dto.response.dorm;

/// 기숙사 신청 결과
///
/// @param applicationId [Long] 신청 ID
/// @param roomNumber    [String] 호실 번호
/// @param message       [String] 결과 메시지
public record ResponseDormApplyResult(Long applicationId, String roomNumber, String message) {}
