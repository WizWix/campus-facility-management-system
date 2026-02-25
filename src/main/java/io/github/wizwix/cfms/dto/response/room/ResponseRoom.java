package io.github.wizwix.cfms.dto.response.room;

/// 호실 정보
///
/// @param id           [Long] 방 ID (순수 인덱싱용)
/// @param buildingName [String] 건물 이름
/// @param name         [String] 방 번호 (e.g. '104호')
public record ResponseRoom(Long id, String buildingName, String name) {}
