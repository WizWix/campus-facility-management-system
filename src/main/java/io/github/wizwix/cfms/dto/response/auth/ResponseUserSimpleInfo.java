package io.github.wizwix.cfms.dto.response.auth;

import io.github.wizwix.cfms.model.enums.Gender;
import io.github.wizwix.cfms.model.enums.UserRole;

/// 사용자 간단 정보
///
/// @param id         [Long] 사용자 ID (내부 인덱싱용)
/// @param name       [String] 사용자 이름
/// @param userNumber [String] 학번 or 교번
/// @param role       [UserRole] 사용자 유형
/// @param gender     [Gender] 성별
public record ResponseUserSimpleInfo(Long id, String name, String userNumber, UserRole role, Gender gender) {}
