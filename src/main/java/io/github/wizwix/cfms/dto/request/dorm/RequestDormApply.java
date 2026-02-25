package io.github.wizwix.cfms.dto.request.dorm;

import io.github.wizwix.cfms.model.enums.DormPeriod;

/// 기숙사 입주 신청 요청
///
/// @param roomId        [Long] 신청 호실 ID
/// @param semester      [String] 학기 (e.g. "2026-1")
/// @param period        [DormPeriod] 입주 기간 (SEMESTER / YEAR)
/// @param partnerNumber [String] 같이 신청 시 친구 학번 (nullable, 단독 신청이면 null)
public record RequestDormApply(Long roomId, String semester, DormPeriod period, String partnerNumber) {}
