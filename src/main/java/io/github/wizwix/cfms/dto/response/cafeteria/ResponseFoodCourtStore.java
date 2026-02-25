package io.github.wizwix.cfms.dto.response.cafeteria;

import java.util.List;

/// 푸드코트 가게 — 프론트 FOODCOURT_STORES 구조 대응
///
/// @param id             가게 ID
/// @param name           가게명
/// @param desc           설명
/// @param category       카테고리
/// @param representative 대표 메뉴명
/// @param hours          운영시간
/// @param menu           메뉴 목록
public record ResponseFoodCourtStore(Long id, String name, String desc, String category,
                                     String representative, String hours, List<ResponseStoreMenuItem> menu) {}
