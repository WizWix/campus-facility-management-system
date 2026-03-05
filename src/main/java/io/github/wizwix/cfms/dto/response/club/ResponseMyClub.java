package io.github.wizwix.cfms.dto.response.club;

import io.github.wizwix.cfms.model.enums.ClubMemberStatus;
import io.github.wizwix.cfms.model.enums.ClubRole;
import io.github.wizwix.cfms.model.enums.ClubStatus;

import java.time.LocalDateTime;

/// 마이페이지 — 내 동아리 목록 응답 DTO
///
/// @param clubId       [Long]            동아리 ID
/// @param clubName     [String]          동아리명
/// @param slug         [String]          동아리 URL 식별자
/// @param presidentName [String]         동아리 회장 이름
/// @param clubStatus   [ClubStatus]      동아리 개설 상태 (APPROVED 등)
/// @param myRole       [ClubRole]        내 역할 (ROLE_PRESIDENT / ROLE_MEMBER 등)
/// @param memberStatus [ClubMemberStatus] 내 가입 상태 (PENDING / APPROVED)
/// @param joinedAt     [LocalDateTime]   가입 신청일시
public record ResponseMyClub(
    Long clubId,
    String clubName,
    String slug,
    String presidentName,
    ClubStatus clubStatus,
    ClubRole myRole,
    ClubMemberStatus memberStatus,
    LocalDateTime joinedAt
) {}