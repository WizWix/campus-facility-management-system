const BASE = '/api';

export async function fetchBuildingMap() {
  const res = await fetch(`${BASE}/buildings/map`);
  return res.json();
}

export async function fetchBuildingDetail(key) {
  const res = await fetch(`${BASE}/buildings/${key}`);
  return res.json();
}

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

export async function fetchCurrentSemester() {
  const res = await fetch(`${BASE}/semesters/current`);
  return res.json();
}

export async function loginApi(username, password) {
  const res = await fetch(`${BASE}/auth/login`, {
    method: 'POST', headers: {'Content-Type': 'application/json'}, body: JSON.stringify({username, password}),
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

export async function signupApi(username, password, name, role) {
  const res = await fetch(`${BASE}/auth/signup`, {
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    body: JSON.stringify({username, password, name, role}),
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}
