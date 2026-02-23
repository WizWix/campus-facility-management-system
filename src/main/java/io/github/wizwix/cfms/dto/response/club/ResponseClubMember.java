package io.github.wizwix.cfms.dto.response.club;

import io.github.wizwix.cfms.model.enums.ClubRole;

import java.time.LocalDateTime;

public record ResponseClubMember(Long userId, String name, String number, ClubRole role, LocalDateTime joinedAt) {}
