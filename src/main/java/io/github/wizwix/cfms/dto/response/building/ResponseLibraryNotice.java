package io.github.wizwix.cfms.dto.response.building;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseLibraryNotice {
  private Long id;
  /** 상세 조회 시에만 채워짐 (목록 조회 시 null) */
  private String content;
  private String date;
  private String title;
  private String type;     // "공지" | "긴급" | "이벤트" | "안내"
  private int views;
}
