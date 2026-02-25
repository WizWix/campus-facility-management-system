// Vite 프록시를 통해 백엔드(8080)로 전달됨 (vite.config.js 참고)
const BASE = '/api';

// ── 건물/시설 관련 (백엔드 미구현 → 현재 buildings.js mock 데이터 사용 중) ──

// TODO: 백엔드 BuildingApiController 구현 후 활성화
export async function fetchBuildingMap() {
  const res = await fetch(`${BASE}/buildings/map`);
  return res.json();
}

// TODO: 백엔드 BuildingApiController 구현 후 활성화
export async function fetchBuildingDetail(key) {
  const res = await fetch(`${BASE}/buildings/${key}`);
  return res.json();
}

// TODO: 백엔드 ReservationApiController 구현 후 활성화
export async function fetchReservations(roomKey, date) {
  const res = await fetch(`${BASE}/reservations?roomKey=${roomKey}&date=${date}`);
  return res.json();
}

export async function createReservation(data) {
  const res = await fetch(`${BASE}/reservations`, {
    method: 'POST', headers: {'Content-Type': 'application/json'}, body: JSON.stringify(data),
  });
  if (!res.ok) {
    const text = await res.text();
    throw new Error(text);
  }
  return res.json();
}

// TODO: 백엔드 SemesterController 자체가 없음 — 구현 필요
export async function fetchCurrentSemester() {
  const res = await fetch(`${BASE}/semesters/current`);
  return res.json();
}

// ── 식당 관련 ──

/// 오늘의 학식 — date 없으면 서버에서 오늘 날짜로 조회
/// 응답: { date, meals: [{ type, time, icon, items: [{ name, price, discount }] }] }
export async function fetchTodayMeals(date) {
  const q = date ? `?date=${date}` : '';
  const res = await fetch(`${BASE}/cafeteria/meals${q}`);
  return res.json();
}

/// 푸드코트 가게 전체
/// 응답: [{ id, name, desc, category, representative, hours, menu: [{ name, price, discount, popular }] }]
export async function fetchFoodCourtStores() {
  const res = await fetch(`${BASE}/cafeteria/foodcourt`);
  return res.json();
}

// ── 인증 관련 ──

/// 로그인 — 백엔드 RequestLogin DTO에 맞춰 userNumber 사용
/// 응답: { accessToken, user: { id, name, userNumber, role } }
export async function loginApi(userNumber, password) {
  const res = await fetch(`${BASE}/auth/login`, {
    method: 'POST', headers: {'Content-Type': 'application/json'}, body: JSON.stringify({userNumber, password}),
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

/// 회원가입 — 백엔드 RequestRegister DTO: { userNumber, name, password, email, gender }
/// 응답: 201 Created (body 없음), 가입 후 별도 로그인 필요
export async function signupApi(userNumber, password, name, email, gender) {
  const res = await fetch(`${BASE}/auth/register`, {
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    body: JSON.stringify({userNumber, name, password, email, gender}),
  });
  if (!res.ok) throw new Error(await res.text());
}

/// 로그아웃 — 백엔드에서 JWT 쿠키를 만료시킴
export async function logoutApi() {
  await fetch(`${BASE}/auth/logout`, {method: 'POST'});
}

// ── 기숙사 관련 ──

/// 기숙사 호실 목록 (층별) — gender: "MALE" or "FEMALE"
/// 응답: [{ floor, rooms: [{ id, roomNumber, floor, occupancy, residentName }] }]
export async function fetchDormRooms(gender) {
  const res = await fetch(`${BASE}/dorms/rooms?gender=${gender}`);
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

/// 기숙사 입주 신청 — 로그인 필요 (JWT 쿠키 자동 전송)
/// data: { roomId, semester, period, partnerNumber }
/// 응답: { applicationId, roomNumber, message }
export async function applyDorm(data) {
  const res = await fetch(`${BASE}/dorms/apply`, {
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    body: JSON.stringify(data),
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

// ── 마이페이지 관련 ──

/// 내 프로필 조회 — 로그인 필요
/// 응답: { id, name, userNumber, email, role, gender, createdAt }
export async function fetchMyProfile() {
  const res = await fetch(`${BASE}/users/me`);
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

/// 내 정보 수정 — 로그인 필요
/// data: { oldPassword, newPassword, email }
export async function updateMyProfile(data) {
  const res = await fetch(`${BASE}/users/me`, {
    method: 'PATCH',
    headers: {'Content-Type': 'application/json'},
    body: JSON.stringify(data),
  });
  if (!res.ok) throw new Error(await res.text());
}

/// 내 기숙사 신청 내역 조회 — 로그인 필요
/// 응답: [{ id, roomNumber, semester, period, status, partnerName, createdAt }]
export async function fetchMyDormApplications() {
  const res = await fetch(`${BASE}/dorms/my`);
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

/// 기숙사 신청 취소 — PENDING만 가능, 로그인 필요
export async function cancelDormApplication(id) {
  const res = await fetch(`${BASE}/dorms/${id}`, {method: 'DELETE'});
  if (!res.ok) throw new Error(await res.text());
}
