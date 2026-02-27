package io.github.wizwix.cfms.service;

import io.github.wizwix.cfms.dto.response.building.ResponseLibraryBook;
import io.github.wizwix.cfms.dto.response.building.ResponseLibraryCongestion;
import io.github.wizwix.cfms.dto.response.building.ResponseLibraryNotice;
import io.github.wizwix.cfms.dto.response.building.ResponseLibraryReadingRoom;
import io.github.wizwix.cfms.dto.response.building.ResponseLibraryStudyRoom;
import io.github.wizwix.cfms.model.SeatReservation;
import io.github.wizwix.cfms.model.StudyRoomReservation;
import io.github.wizwix.cfms.repo.BookRepository;
import io.github.wizwix.cfms.repo.SeatReservationRepository;
import io.github.wizwix.cfms.repo.StudyRoomReservationRepository;
import io.github.wizwix.cfms.service.iface.ILibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LibraryService implements ILibraryService {

  // ── 5. 공지사항 ──
  // TODO: Notice 엔티티 + DevLoader로 전환 (현재 하드코딩 데이터)
  private static final List<Map<String, Object>> NOTICE_DB = List.of(
      notice(1L, "공지", "2025년 1학기 도서관 운영시간 변경 안내", "2025-02-20", 1823,
          "2025년 1학기부터 평일 운영시간이 오전 7시 30분~오후 11시로 변경됩니다. 토요일은 오전 9시~오후 6시, 일요일 및 공휴일은 오전 11시~오후 5시로 운영됩니다."),
      notice(2L, "긴급", "[긴급] 4층 디지털열람실 PC 시스템 점검 예정", "2025-02-19", 942,
          "2025년 2월 22일(토) 오전 9시~오후 1시 4층 디지털열람실 PC 전체 시스템 정기 점검을 실시합니다."),
      notice(3L, "이벤트", "도서관 독서 마라톤 참가자 모집", "2025-02-15", 2341,
          "2025년 봄학기 독서 마라톤 행사에 참여하세요! 3월 1일부터 5월 31일까지 10권 이상 읽은 참가자에게는 소정의 상품을 드립니다."),
      notice(4L, "공지", "2025년 상호대차 및 원문복사 서비스 안내", "2025-02-10", 567,
          "타 대학 소장 자료 신청 서비스(상호대차)와 원문복사 서비스를 이용하실 수 있습니다."),
      notice(5L, "안내", "도서 연체 자동반납기 설치 완료", "2025-02-05", 1204,
          "도서관 1층 입구에 자동반납기가 새롭게 설치되었습니다. 운영시간 외에도 24시간 도서 반납이 가능합니다.")
  );
  private static final Map<Long, String> ROOM_FLOOR = Map.of(
      1L, "2F", 2L, "3F", 3L, "B1", 4L, "4F");
  private static final Map<Long, String> ROOM_NAME = Map.of(
      1L, "제1열람실", 2L, "제2열람실", 3L, "야간열람실", 4L, "디지털열람실");
  // ── 열람실 스펙 정의 ──
  // TODO: ReadingRoom 엔티티 + DevLoader로 전환 (현재 하드코딩 데이터)
  // id → { totalSeats, targetUsedRate(%) }
  // targetUsedRate 는 OCCUPIED 비율 목표 — 100 절대 초과 불가
  // 2F: 29% 사용 중 (≈71% 이용가능), 3F: 44% 사용 중 (≈56% 이용가능)
  private static final Map<Long, int[]> ROOM_SPEC = Map.of(
      1L, new int[]{128, 29},   // 제1열람실  2F
      2L, new int[]{76, 44},   // 제2열람실  3F
      3L, new int[]{80, 15},   // 야간열람실 B1
      4L, new int[]{60, 92}    // 디지털열람실 4F
  );
  // ── 3. 스터디룸 ──
  // TODO: StudyRoom 엔티티 + DevLoader로 전환 (현재 하드코딩 데이터)
  private static final List<Map<String, Object>> STUDY_ROOM_DB = List.of(
      studyRoom(1L, "A101", "1F", 4, List.of("TV", "화이트보드"), "AVAILABLE"),
      studyRoom(2L, "A102", "1F", 6, List.of("TV", "화이트보드", "빔프로젝터"), "AVAILABLE"),
      studyRoom(3L, "B201", "2F", 8, List.of("화이트보드", "빔프로젝터"), "OCCUPIED"),
      studyRoom(4L, "B202", "2F", 4, List.of("TV"), "AVAILABLE"),
      studyRoom(5L, "C301", "3F", 10, List.of("TV", "화이트보드", "빔프로젝터", "화상회의"), "MAINTENANCE")
  );
  private final BookRepository bookRepository;

  // ── 열람실 공통 계산 헬퍼 ──
  private final SeatReservationRepository seatReservationRepository;

  // ── 1. 열람실 ──
  private final StudyRoomReservationRepository studyRoomReservationRepository;

  private static Map<String, Object> notice(Long id, String type, String title,
                                            String date, int views, String content) {
    Map<String, Object> m = new LinkedHashMap<>();
    m.put("id", id);
    m.put("type", type);
    m.put("title", title);
    m.put("date", date);
    m.put("views", views);
    m.put("content", content);
    return m;
  }

  private static Map<String, Object> studyRoom(Long id, String name, String floor, int capacity,
                                               List<String> amenities, String status) {
    Map<String, Object> m = new LinkedHashMap<>();
    m.put("id", id);
    m.put("name", name);
    m.put("floor", floor);
    m.put("capacity", capacity);
    m.put("amenities", amenities);
    m.put("status", status);
    return m;
  }

  // ── 2. 도서 검색 ──

  // ── 4. 혼잡도 ──
  // 이전: 고정 % 하드코딩 → 현재: buildSeats()로 실시간 OCCUPIED 비율 계산
  // 열람실 현황 탭(getReadingRooms)과 동일한 로직을 사용해 수치가 항상 일치함
  @Override
  public ResponseLibraryCongestion getCongestion(Long buildingId) {
    LocalDate today = LocalDate.now();
    int rate2F = Math.min((int) ((double) buildSeats(1L, today).stream().filter(s -> "OCCUPIED".equals(s.get("status"))).count() / ROOM_SPEC.get(1L)[0] * 100), 100);
    int rate3F = Math.min((int) ((double) buildSeats(2L, today).stream().filter(s -> "OCCUPIED".equals(s.get("status"))).count() / ROOM_SPEC.get(2L)[0] * 100), 100);
    int rateB1 = Math.min((int) ((double) buildSeats(3L, today).stream().filter(s -> "OCCUPIED".equals(s.get("status"))).count() / ROOM_SPEC.get(3L)[0] * 100), 100);
    int rate4F = Math.min((int) ((double) buildSeats(4L, today).stream().filter(s -> "OCCUPIED".equals(s.get("status"))).count() / ROOM_SPEC.get(4L)[0] * 100), 100);
    int rate1F = 45; // 1층 로비는 별도 센서 없으므로 고정값 유지 (TODO: 센서 연동)

    // 순서 고정: 4층(맨 위) → 3층 → 2층 → 1층 → B1(맨 아래)
    List<Map<String, Object>> floors = new ArrayList<>(List.of(
        new LinkedHashMap<>(Map.of("name", "4층 디지털열람실", "rate", rate4F, "capacity", 60)),
        new LinkedHashMap<>(Map.of("name", "3층 제2열람실", "rate", rate3F, "capacity", 76)),
        new LinkedHashMap<>(Map.of("name", "2층 제1열람실", "rate", rate2F, "capacity", 128)),
        new LinkedHashMap<>(Map.of("name", "1층 로비·안내데스크", "rate", rate1F, "capacity", 50)),
        new LinkedHashMap<>(Map.of("name", "B1 야간열람실", "rate", rateB1, "capacity", 80))
    ));
    List<Map<String, Object>> hourly = List.of(
        Map.of("hour", "08", "rate", 10), Map.of("hour", "09", "rate", 35),
        Map.of("hour", "10", "rate", 58), Map.of("hour", "11", "rate", 72),
        Map.of("hour", "12", "rate", 65), Map.of("hour", "13", "rate", 55),
        Map.of("hour", "14", "rate", 78), Map.of("hour", "15", "rate", 82),
        Map.of("hour", "16", "rate", 75), Map.of("hour", "17", "rate", 60),
        Map.of("hour", "18", "rate", 42), Map.of("hour", "19", "rate", 30),
        Map.of("hour", "20", "rate", 20), Map.of("hour", "21", "rate", 15)
    );
    int overallRate = floors.stream().mapToInt(f -> (int) f.get("rate")).sum() / floors.size();
    // @AllArgsConstructor 필드 순서: floors, hourlyTrend, overallRate
    return new ResponseLibraryCongestion(floors, hourly, overallRate);
  }

  // ── 6. 마이페이지용: 내 예약 조회 ──
  // SeatReservation / StudyRoomReservation 테이블에서 해당 학번의 예약만 필터링
  // roomName, floor는 하드코딩(ROOM_NAME, STUDY_ROOM_DB)에서 조회 (TODO: 엔티티 전환 후 JOIN)

  @Override
  public List<Map<String, Object>> getMySeatReservations(String userNumber) {
    return seatReservationRepository.findAll().stream()
        .filter(r -> userNumber.equals(r.getUserNumber()))
        .sorted(Comparator.comparing(SeatReservation::getDate).reversed())
        .map(r -> {
          Map<String, Object> m = new LinkedHashMap<>();
          m.put("id", r.getId());
          m.put("roomName", ROOM_NAME.getOrDefault(r.getRoomId(), "열람실"));
          m.put("floor", ROOM_FLOOR.getOrDefault(r.getRoomId(), "?F"));
          m.put("seatNo", r.getSeatNo());
          m.put("date", r.getDate().toString());
          return m;
        })
        .toList();
  }

  @Override
  public List<Map<String, Object>> getMyStudyRoomReservations(String userNumber) {
    return studyRoomReservationRepository.findAll().stream()
        .filter(r -> userNumber.equals(r.getUserNumber()))
        .sorted(Comparator.comparing(StudyRoomReservation::getDate).reversed())
        .map(r -> {
          var room = STUDY_ROOM_DB.stream()
              .filter(s -> s.get("id").equals(r.getRoomId()))
              .findFirst();
          String roomName = room.map(s -> s.get("name").toString()).orElse("스터디룸");
          String floor = room.map(s -> s.get("floor").toString()).orElse("?F");
          Map<String, Object> m = new LinkedHashMap<>();
          m.put("id", r.getId());
          m.put("roomName", roomName);
          m.put("floor", floor);
          m.put("date", r.getDate().toString());
          m.put("startHour", r.getStartHour());
          m.put("endHour", r.getStartHour() + 1);
          return m;
        })
        .toList();
  }

  @Override
  public ResponseLibraryNotice getNotice(Long buildingId, Long noticeId) {
    return NOTICE_DB.stream()
        .filter(n -> n.get("id").equals(noticeId))
        .findFirst()
        // @AllArgsConstructor 필드 순서: id, content, date, title, type, views
        .map(n -> new ResponseLibraryNotice(
            (Long) n.get("id"), n.get("content").toString(), n.get("date").toString(),
            n.get("title").toString(), n.get("type").toString(), (int) n.get("views")
        ))
        .orElseThrow(() -> new RuntimeException("공지를 찾을 수 없습니다: " + noticeId));
  }

  @Override
  public List<ResponseLibraryNotice> getNotices(Long buildingId) {
    return NOTICE_DB.stream()
        // @AllArgsConstructor 필드 순서: id, content, date, title, type, views
        .map(n -> new ResponseLibraryNotice(
            (Long) n.get("id"), null, n.get("date").toString(),
            n.get("title").toString(), n.get("type").toString(), (int) n.get("views")
        ))
        .toList();
  }

  @Override
  public ResponseLibraryReadingRoom getReadingRoomSeats(Long buildingId, Long roomId) {
    LocalDate today = LocalDate.now();
    List<Map<String, Object>> seats = buildSeats(roomId, today);

    int total = ROOM_SPEC.getOrDefault(roomId, new int[]{60, 50})[0];
    int occupied = (int) seats.stream().filter(s -> "OCCUPIED".equals(s.get("status"))).count();

    // @AllArgsConstructor 필드 순서: id, name, floor, seats, totalSeats, usedSeats
    return new ResponseLibraryReadingRoom(
        roomId,
        ROOM_NAME.getOrDefault(roomId, "열람실"),
        ROOM_FLOOR.getOrDefault(roomId, "?F"),
        seats,       // 좌석 배치도 포함
        total,
        occupied
    );
  }

  @Override
  public List<ResponseLibraryReadingRoom> getReadingRooms(Long buildingId) {
    LocalDate today = LocalDate.now();
    List<ResponseLibraryReadingRoom> result = new ArrayList<>();

    for (long roomId = 1; roomId <= 4; roomId++) {
      // buildSeats 로 정확한 OCCUPIED 수 계산 (좌측 카드와 오른쪽 완전 일치)
      List<Map<String, Object>> seats = buildSeats(roomId, today);
      int total = ROOM_SPEC.get(roomId)[0];
      int occupied = (int) seats.stream().filter(s -> "OCCUPIED".equals(s.get("status"))).count();

      // @AllArgsConstructor 필드 순서: id, name, floor, seats, totalSeats, usedSeats
      result.add(new ResponseLibraryReadingRoom(
          roomId,
          ROOM_NAME.get(roomId),
          ROOM_FLOOR.get(roomId),
          null,        // 목록 조회 시 seats null
          total,
          occupied
      ));
    }
    return result;
  }

  @Override
  public ResponseLibraryStudyRoom getStudyRoomSlots(Long buildingId, Long roomId, String date) {
    LocalDate localDate = LocalDate.parse(date);

    // DB에서 해당 방의 예약된 시간 조회
    List<String> occupiedSlots = studyRoomReservationRepository
        .findByRoomIdAndDate(roomId, localDate)
        .stream()
        .map(r -> String.format("%02d:00", r.getStartHour()))
        .toList();

    return STUDY_ROOM_DB.stream()
        .filter(r -> r.get("id").equals(roomId))
        .findFirst()
        // @AllArgsConstructor 필드 순서: id, name, amenities, capacity, floor, occupiedSlots, status
        .map(r -> new ResponseLibraryStudyRoom(
            (Long) r.get("id"), r.get("name").toString(), (List<String>) r.get("amenities"),
            (int) r.get("capacity"), r.get("floor").toString(),
            occupiedSlots, r.get("status").toString()
        ))
        .orElseThrow(() -> new RuntimeException("스터디룸을 찾을 수 없습니다: " + roomId));
  }

  @Override
  public List<ResponseLibraryStudyRoom> getStudyRooms(Long buildingId) {
    return STUDY_ROOM_DB.stream()
        // @AllArgsConstructor 필드 순서: id, name, amenities, capacity, floor, occupiedSlots, status
        .map(r -> new ResponseLibraryStudyRoom(
            (Long) r.get("id"), r.get("name").toString(), (List<String>) r.get("amenities"),
            (int) r.get("capacity"), r.get("floor").toString(),
            null, r.get("status").toString()
        ))
        .toList();
  }

  @Override
  public void reserveBook(Long buildingId, Long bookId) {
    // TODO: 도서 예약 엔티티 추가 후 구현
  }

  @Override
  @Transactional
  public void reserveSeat(Long buildingId, Long roomId, Integer seatNo, String userNumber) {
    LocalDate today = LocalDate.now();

    // 1인 1좌석 제한: 같은 날 어느 열람실이든 이미 예약한 경우 거부
    if (seatReservationRepository.existsByUserNumberAndDate(userNumber, today)) {
      throw new RuntimeException("오늘 이미 열람실 좌석을 예약하셨습니다. 1인 1좌석만 예약 가능합니다.");
    }

    // 해당 좌석 이미 예약된 경우 거부
    if (seatReservationRepository.existsByRoomIdAndSeatNoAndDate(roomId, seatNo, today)) {
      throw new RuntimeException("이미 다른 사용자가 예약한 좌석입니다.");
    }

    seatReservationRepository.save(
        SeatReservation.builder()
            .roomId(roomId)
            .seatNo(seatNo)
            .date(today)
            .userNumber(userNumber)
            .build()
    );
  }

  @Override
  @Transactional
  public void reserveStudyRoom(Long buildingId, Long roomId, String date,
                               Integer startHour, String userNumber) {
    LocalDate localDate = LocalDate.parse(date);

    // 같은 유저가 같은 날 같은 시간에 이미 다른 방을 예약했는지 확인 (시간 중복 방지)
    if (studyRoomReservationRepository
        .existsByUserNumberAndDateAndStartHour(userNumber, localDate, startHour)) {
      throw new RuntimeException(
          String.format("동일한 시간에 예약하셨습니다. %02d:00 시간대는 이미 다른 스터디룸을 예약하셨습니다. 같은 시간에 중복 예약은 불가능합니다.", startHour)
      );
    }

    // 해당 방 + 시간 이미 다른 사람이 예약한 경우
    if (studyRoomReservationRepository
        .existsByRoomIdAndDateAndStartHour(roomId, localDate, startHour)) {
      throw new RuntimeException("이미 다른 사용자가 예약한 시간입니다.");
    }

    studyRoomReservationRepository.save(
        StudyRoomReservation.builder()
            .roomId(roomId)
            .date(localDate)
            .startHour(startHour)
            .userNumber(userNumber)
            .build()
    );
  }

  @Override
  public List<ResponseLibraryBook> searchBooks(Long buildingId, String query, String publisher, String category) {
    return bookRepository.findAll().stream()
        .filter(b -> {
          boolean matchQ = query == null || query.isBlank()
              || b.getTitle().contains(query)
              || b.getAuthor().contains(query)
              || b.getPublisher().contains(query);
          boolean matchPub = publisher == null || publisher.isBlank()
              || b.getPublisher().contains(publisher);
          boolean matchCat = category == null || category.isBlank()
              || category.equals(b.getCategory());
          return matchQ && matchPub && matchCat;
        })
        // @AllArgsConstructor 필드 순서: id, author, available, category, location, publisher, title, year
        .map(b -> new ResponseLibraryBook(
            b.getId(), b.getAuthor(),
            b.getAvailable() != null && b.getAvailable(),
            b.getCategory(), "미지정", b.getPublisher(),
            b.getTitle(), 0
        ))
        .toList();
  }

  /**
   * 오늘 날짜 기준으로 한 열람실의 좌석 상태 목록을 생성한다.
   * <p>
   * 규칙:
   * 1. DB에서 예약된(RESERVED) 좌석 번호를 먼저 확정한다.
   * 2. 나머지 좌석을 날짜+roomId 기반 시드로 셔플한다.
   * 3. 셔플 결과 앞에서 targetOccupied 개수만큼 OCCUPIED, 나머지 AVAILABLE.
   * 4. targetOccupied = min(total × rate / 100, total - reserved) — 절대 초과 불가
   */
  private List<Map<String, Object>> buildSeats(Long roomId, LocalDate date) {
    int[] spec = ROOM_SPEC.getOrDefault(roomId, new int[]{60, 50});
    int total = spec[0];
    int targetRate = Math.min(spec[1], 100);

    // 1. DB 예약 좌석
    Set<Integer> reservedNos = seatReservationRepository
        .findByRoomIdAndDate(roomId, date)
        .stream()
        .map(SeatReservation::getSeatNo)
        .collect(Collectors.toSet());

    int reserved = reservedNos.size();
    // 2. OCCUPIED 개수 — (total - reserved) 를 절대 초과 불가
    int targetOccupied = Math.min((int) (total * targetRate / 100.0), total - reserved);

    // 3. 예약 안 된 좌석 목록 셔플 (시드: roomId + 날짜 day-of-year)
    List<Integer> freeSeats = new ArrayList<>();
    for (int i = 1; i <= total; i++) {
      if (!reservedNos.contains(i)) freeSeats.add(i);
    }
    long seed = roomId * 10000L + date.getDayOfYear();
    Collections.shuffle(freeSeats, new Random(seed));

    // 4. 앞 targetOccupied 개 → OCCUPIED, 나머지 → AVAILABLE
    Set<Integer> occupiedNos = new HashSet<>(
        freeSeats.subList(0, Math.min(targetOccupied, freeSeats.size()))
    );

    // 5. 좌석 목록 생성 (1번~total번 순서)
    List<Map<String, Object>> seats = new ArrayList<>();
    for (int seatNo = 1; seatNo <= total; seatNo++) {
      String status;
      if (reservedNos.contains(seatNo)) status = "RESERVED";
      else if (occupiedNos.contains(seatNo)) status = "OCCUPIED";
      else status = "AVAILABLE";
      seats.add(Map.of("seatNo", seatNo, "status", status));
    }
    return seats;
  }
}
