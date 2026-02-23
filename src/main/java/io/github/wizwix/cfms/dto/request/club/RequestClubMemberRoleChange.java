package io.github.wizwix.cfms.dto.request.club;

import io.github.wizwix.cfms.model.enums.ClubRole;

/// 동아리 부원 역할 변경
///
/// @param id 변경 대상의 ID
/// @param clubRole 새 역할
public record RequestClubMemberRoleChange(Long id, ClubRole clubRole) {}
