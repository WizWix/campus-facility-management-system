package io.github.wizwix.cfms.model.enums;

/// 동아리 가입 신청 현황
///   * `APPROVED`: 동아리 가입 승인됨
///   * `LEFT`: 동아리 탈퇴함
///   * `PENDING`: 동아리 가입 승인 대기중
///   * `REJECTED`: 동아리 가입 거부됨
public enum ClubMemberStatus {
  /// 동아리 가입 승인됨
  APPROVED,
  /// 동아리 탈퇴함
  LEFT,
  /// 동아리 가입 승인 대기 중
  PENDING,
  /// 동아리 가입 거부됨
  REJECTED
}
