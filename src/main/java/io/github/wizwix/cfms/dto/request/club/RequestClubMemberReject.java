package io.github.wizwix.cfms.dto.request.club;

import org.springframework.lang.Nullable;

/// 동아리 가입 거절
///
/// @param reason 간단한 거절 사유
public record RequestClubMemberReject(@Nullable String reason) {}
