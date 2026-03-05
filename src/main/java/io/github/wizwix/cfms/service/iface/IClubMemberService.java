package io.github.wizwix.cfms.service.iface;

import io.github.wizwix.cfms.dto.response.club.ResponseClubMember;
import io.github.wizwix.cfms.dto.response.club.ResponseMyClub;
import io.github.wizwix.cfms.model.enums.ClubMemberStatus;
import io.github.wizwix.cfms.model.enums.ClubRole;

import java.util.List;

public interface IClubMemberService {
  /// 내가 가입한 동아리 목록 — 마이페이지 동아리 탭 (APPROVED + PENDING)
  List<ResponseMyClub> getMyClubs(String userNumber);

  /// 동아리 부원 목록 조회 — status null 이면 APPROVED 기본값
  List<ResponseClubMember> getMembers(String slug, ClubMemberStatus status);

  /// 동아리 가입 신청 — 회장 자기 가입 차단, 중복 가입 차단
  ResponseClubMember joinClub(String userNumber, String slug);

  /// 동아리 부원 추방 — 회장만 가능
  void kickMember(String requesterNumber, String slug, Long targetId);

  /// 동아리 부원 역할 변경 — 회장만 가능
  ResponseClubMember setRole(String requesterNumber, String slug, Long targetId, ClubRole clubRole);
}