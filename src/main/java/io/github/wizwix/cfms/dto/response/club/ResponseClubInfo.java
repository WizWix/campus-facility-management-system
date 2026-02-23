package io.github.wizwix.cfms.dto.response.club;

import io.github.wizwix.cfms.model.enums.ClubStatus;

public record ResponseClubInfo(Long id, String name, String slug, String description, String presidentName,
                               Boolean autoApprove, Integer memberCount, ClubStatus status) {}
