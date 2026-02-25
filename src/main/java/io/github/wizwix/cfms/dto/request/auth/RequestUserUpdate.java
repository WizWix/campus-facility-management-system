package io.github.wizwix.cfms.dto.request.auth;

import io.github.wizwix.cfms.model.enums.Gender;

/// 회원 정보 갱신
///
/// 이 프로젝트는 이미 학생과 교사 정보를 갖고 있는 대학이 서비스하는 것을 전제로 삼았기에, 회원 정보 갱신은 본래 불가능해야 함.
/// 하지만 프로젝트 시연을 위해 임의로 회원 정보 갱신을 도입함
///
/// @param oldPassword [String] 기존 비밀번호 (불일치 시 갱신 거부)
/// @param newPassword [String] 새 비밀번호
/// @param email       [String] 새 이메일 주소
/// @param gender      [Gender] 성별 ("MALE" or "FEMALE")
public record RequestUserUpdate(String oldPassword, String newPassword, String email, Gender gender) {}
