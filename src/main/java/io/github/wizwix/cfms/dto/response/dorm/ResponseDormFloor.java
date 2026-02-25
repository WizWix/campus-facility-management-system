package io.github.wizwix.cfms.dto.response.dorm;

import java.util.List;

/// 기숙사 층 정보
///
/// @param floor [int] 층
/// @param rooms [List] 해당 층 호실 목록
public record ResponseDormFloor(int floor, List<ResponseDormRoom> rooms) {}
