package io.github.wizwix.cfms.model.enums;

/// 동아리 상태
///   * `APPROVED`: 동아리 개설 승인됨
///   * `PENDING`: 동아리 개설 승인 대기 중
///   * `REJECTED`: 동아리 개설 승인 거부됨
public enum ClubStatus {
  /// 동아리 개설이 승인됨
  APPROVED,
  /// 동아리 개설 승인 대기 중
  PENDING,
  /// 동아리 개설 승인 거부됨
  REJECTED
}
