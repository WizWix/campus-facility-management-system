package io.github.wizwix.cfms.dto.response.club;

import io.github.wizwix.cfms.model.enums.ClubStatus;

import java.time.LocalDateTime;

public record ResponseAdminClub(Long clubId, String name, String presidentName, LocalDateTime createdAt,
                                ClubStatus status) {}
