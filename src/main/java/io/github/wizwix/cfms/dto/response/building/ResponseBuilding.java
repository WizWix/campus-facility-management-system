package io.github.wizwix.cfms.dto.response.building;

import io.github.wizwix.cfms.model.enums.BuildingType;

/// 건물 정보
///
/// @param id       [Long] 건물 ID (내부 인덱싱용)
/// @param name     [String] 건물 이름 (e.g. 'IT대학동')
/// @param slug     [String] 건물 URL 식별자 (e.g. 'lecture1')
/// @param info     [String] 건물 설명 (e.g. '강의실 XX개, 1~X층')
/// @param points   [String] 캠퍼스 전체 지도에서 해당 건물이 차지하는 영역의 각 꼭지점 모음
/// @param type     [BuildingType] 건물 유형
/// @param rentable [Boolean] 대여 가능 여부
public record ResponseBuilding(Long id, String name, String slug, String info, String points, BuildingType type,
                               Boolean rentable) {}
