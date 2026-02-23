package io.github.wizwix.cfms.dto.request.auth;

/// 비밀번호 초기화 요청 (2단계)
///
/// @param token 본인 확인용 인증 토큰 (임의의 숫자 6자리)
// 인증 토큰이 일치할 시 3단계로 넘어감
public record RequestPasswordReset2(String token) {}
