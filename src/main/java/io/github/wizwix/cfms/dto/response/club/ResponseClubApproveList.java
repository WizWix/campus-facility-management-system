package io.github.wizwix.cfms.dto.response.club;

import java.time.LocalDateTime;

public record ResponseClubApproveList(Long id, String name, String presidentName, LocalDateTime createdAt) {}
