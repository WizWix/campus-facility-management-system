package io.github.wizwix.cfms.dto.response.building;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseLibraryBook {
  private Long id;
  private String author;
  private boolean available;
  private String category;
  private String location;  // TODO: Book 모델에 location 필드 추가 후 연동 (현재 "미지정" 하드코딩)
  private String publisher;
  private String title;
  private int year;         // TODO: Book 모델에 year 필드 추가 후 연동 (현재 0 하드코딩)
}
