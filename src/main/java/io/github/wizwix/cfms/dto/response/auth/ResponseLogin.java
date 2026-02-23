package io.github.wizwix.cfms.dto.response.auth;

import io.github.wizwix.cfms.model.enums.UserRole;

/// 로그인 성공 시 반환 (로그인 성공 전용 레코드)
public record ResponseLogin(String token, String name, UserRole role) {}
