// Vite 프록시를 통해 백엔드(8080)로 전달됨 (vite.config.js 참고)
const BASE = '/api';

// ── 건물/시설 관련 ──

/// 건물 목록 조회 (공개)
/// 응답: [{ id, name, slug, info, points, type, rentable }]
export async function fetchBuildings() {
  const res = await fetch(`${BASE}/buildings`);
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

/// 건물 호실 목록 조회 (공개)
/// 응답: [{ id, buildingName, name, floor, type, capacity }]
export async function fetchBuildingRooms(slug) {
  const res = await fetch(`${BASE}/buildings/${slug}/rooms`);
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

/// 호실별 예약 현황 조회 (공개)
/// 응답: [{ id, roomCode, buildingName, userName, clubName, startTime, endTime, status, purpose, rejectReason, createdAt }]
export async function fetchRoomReservations(roomId, date) {
  const res = await fetch(`${BASE}/reservations?roomId=${roomId}&date=${date}`);
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

/// 시설 예약 신청 — 로그인 필요
/// data: { roomId, startTime, endTime, purpose, clubId? }
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

/// 회원가입 — 백엔드 RequestRegister DTO: { userNumber, name, password, email }
/// 응답: 201 Created (body 없음), 가입 후 별도 로그인 필요
export async function signupApi(userNumber, password, name, email) {
  const res = await fetch(`${BASE}/auth/register`, {
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    body: JSON.stringify({userNumber, name, password, email}),
  });
  if (!res.ok) throw new Error(await res.text());
}

/// 로그아웃 — 백엔드에서 JWT 쿠키를 만료시킴
export async function logoutApi() {
  await fetch(`${BASE}/auth/logout`, {method: 'POST'});
}
