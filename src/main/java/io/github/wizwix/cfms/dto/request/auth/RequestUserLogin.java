package io.github.wizwix.cfms.dto.request.auth;

/// 사용자 로그인
///
/// @param userNumber 사용자 학번 or 교번
/// @param password   비밀번호
public record RequestUserLogin(String userNumber, String password) {}
