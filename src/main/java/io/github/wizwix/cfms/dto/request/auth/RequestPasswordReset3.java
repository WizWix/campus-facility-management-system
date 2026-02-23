package io.github.wizwix.cfms.dto.request.auth;

/// 비밀번호 초기화 요청 (3단계)
///
/// @param userNumber 교번 or 학번
/// @param newPassword 새로운 비밀번호
// `usernumber`는 숨겨진 `<input>`으로 받음
// 그런데 이 과정을 어떻게 안전하게 처리할 수 있을까? 누군가가 임의로 `RequestPasswordReset3` 요청만을 보냈다고 해서 그대로 처리해서는 안됨.
public record RequestPasswordReset3(String userNumber, String newPassword) {}
