package io.github.wizwix.cfms.dto.request;

/// @param name        동아리 이름
/// @param autoApprove 부원 자동 가입 허가 여부
/// @param description 동아리 소개/설명문
/// @param slug        동아리 URL 식별자
public record RequestClubCreate(String name, Boolean autoApprove, String description, String slug) {
}
