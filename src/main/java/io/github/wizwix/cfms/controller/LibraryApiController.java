package io.github.wizwix.cfms.controller;

import io.github.wizwix.cfms.dto.response.building.ResponseLibraryBook;
import io.github.wizwix.cfms.dto.response.building.ResponseLibraryCongestion;
import io.github.wizwix.cfms.dto.response.building.ResponseLibraryNotice;
import io.github.wizwix.cfms.dto.response.building.ResponseLibraryReadingRoom;
import io.github.wizwix.cfms.dto.response.building.ResponseLibraryStudyRoom;
import io.github.wizwix.cfms.service.iface.ILibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 도서관 전용 API 컨트롤러
 * Base: /api/buildings/{buildingId}/library
 */
@RestController
@RequestMapping("/api/buildings/{buildingId}/library")
@RequiredArgsConstructor
public class LibraryApiController {

  private final ILibraryService libraryService;

  // ── 4. 혼잡도 ──
  @GetMapping("/congestion")
  public ResponseEntity<ResponseLibraryCongestion> getCongestion(
      @PathVariable Long buildingId) {
    return ResponseEntity.ok(libraryService.getCongestion(buildingId));
  }

  @GetMapping("/notices/{noticeId}")
  public ResponseEntity<ResponseLibraryNotice> getNotice(
      @PathVariable Long buildingId,
      @PathVariable Long noticeId) {
    return ResponseEntity.ok(libraryService.getNotice(buildingId, noticeId));
  }

  // ── 5. 공지사항 ──
  @GetMapping("/notices")
  public ResponseEntity<List<ResponseLibraryNotice>> getNotices(
      @PathVariable Long buildingId) {
    return ResponseEntity.ok(libraryService.getNotices(buildingId));
  }

  @GetMapping("/reading-rooms/{roomId}/seats")
  public ResponseEntity<ResponseLibraryReadingRoom> getReadingRoomSeats(
      @PathVariable Long buildingId,
      @PathVariable Long roomId) {
    return ResponseEntity.ok(libraryService.getReadingRoomSeats(buildingId, roomId));
  }

  // ── 1. 열람실 ──
  @GetMapping("/reading-rooms")
  public ResponseEntity<List<ResponseLibraryReadingRoom>> getReadingRooms(
      @PathVariable Long buildingId) {
    return ResponseEntity.ok(libraryService.getReadingRooms(buildingId));
  }

  @GetMapping("/study-rooms/{roomId}/slots")
  public ResponseEntity<ResponseLibraryStudyRoom> getStudyRoomSlots(
      @PathVariable Long buildingId,
      @PathVariable Long roomId,
      @RequestParam String date) {
    return ResponseEntity.ok(libraryService.getStudyRoomSlots(buildingId, roomId, date));
  }

  // ── 3. 스터디룸 ──
  @GetMapping("/study-rooms")
  public ResponseEntity<List<ResponseLibraryStudyRoom>> getStudyRooms(
      @PathVariable Long buildingId) {
    return ResponseEntity.ok(libraryService.getStudyRooms(buildingId));
  }

  // ── 6. 마이페이지용: 내 예약 조회 (로그인 필수, Authentication에서 학번 추출) ──

  /** 내 열람실 좌석 예약 내역 — 로그인 필수 */
  @GetMapping("/reading-rooms/reservations/me")
  public ResponseEntity<List<Map<String, Object>>> mySeatReservations(
      @PathVariable Long buildingId,
      Authentication auth) {
    return ResponseEntity.ok(libraryService.getMySeatReservations(auth.getName()));
  }

  /** 내 스터디룸 예약 내역 — 로그인 필수 */
  @GetMapping("/study-rooms/reservations/me")
  public ResponseEntity<List<Map<String, Object>>> myStudyRoomReservations(
      @PathVariable Long buildingId,
      Authentication auth) {
    return ResponseEntity.ok(libraryService.getMyStudyRoomReservations(auth.getName()));
  }

  @PostMapping("/books/{bookId}/reserve")
  public ResponseEntity<Void> reserveBook(
      @PathVariable Long buildingId,
      @PathVariable Long bookId) {
    libraryService.reserveBook(buildingId, bookId);
    return ResponseEntity.ok().build();
  }

  // ── 예약 처리 (POST) ──

  /**
   * 좌석 예약 — 로그인 필수
   * POST /api/buildings/{buildingId}/library/reading-rooms/{roomId}/seats/{seatNo}/reserve
   */
  @PostMapping("/reading-rooms/{roomId}/seats/{seatNo}/reserve")
  public ResponseEntity<Void> reserveSeat(
      @PathVariable Long buildingId,
      @PathVariable Long roomId,
      @PathVariable Integer seatNo,
      Authentication auth) {
    // Authentication에서 학번 추출 (Spring Security Principal)
    String userNumber = auth.getName();
    libraryService.reserveSeat(buildingId, roomId, seatNo, userNumber);
    return ResponseEntity.ok().build();
  }

  /**
   * 스터디룸 예약 — 로그인 필수
   * POST /api/buildings/{buildingId}/library/study-rooms/{roomId}/reserve
   * Body: { "date": "2026-02-26", "startHour": 14 }
   */
  @PostMapping("/study-rooms/{roomId}/reserve")
  public ResponseEntity<Void> reserveStudyRoom(
      @PathVariable Long buildingId,
      @PathVariable Long roomId,
      @RequestBody Map<String, Object> body,
      Authentication auth) {
    String userNumber = auth.getName();
    String date = body.get("date").toString();
    Integer startHour = Integer.valueOf(body.get("startHour").toString());
    libraryService.reserveStudyRoom(buildingId, roomId, date, startHour, userNumber);
    return ResponseEntity.ok().build();
  }

  // ── 2. 도서 검색 ──
  @GetMapping("/books")
  public ResponseEntity<List<ResponseLibraryBook>> searchBooks(
      @PathVariable Long buildingId,
      @RequestParam(required = false, defaultValue = "") String q,
      @RequestParam(required = false) String publisher,
      @RequestParam(required = false) String category) {
    return ResponseEntity.ok(libraryService.searchBooks(buildingId, q, publisher, category));
  }
}
