package io.github.wizwix.cfms.dto.request.auth;

/// 회원 가입 요청
///
/// 이 프로젝트는 이미 학생과 교사 정보를 갖고 있는 대학이 서비스하는 것을 전제로 삼았기에, 회원 가입은 본래 불가능해야 함.
/// 하지만 프로젝트 시연을 위해 임의로 회원 가입 요청을 도입함
///
/// @param userNumber 교번 or 학번
/// @param name 이름
/// @param password 비밀번호
/// @param email 이메일 주소
public record RequestUserRegister(String userNumber, String name, String password, String email) {}
