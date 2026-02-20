import {useEffect, useState} from 'react';
import {useAuth} from '../context/AuthContext.jsx';
import {fetchReservations} from '../data/api.js';
import {AuthModal} from './AuthModal.jsx';
import {MiniCalendar} from './MiniCalendar.jsx';
import {ReserveModal} from './ReserveModal.jsx';

const MONTH_NAMES = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];

export function ReservationView({buildingKey, buildingData, jumpToRoom}) {
  const {currentUser} = useAuth();
  const now = new Date();
  const [selectedRoom, setSelectedRoom] = useState(null);
  const [selectedDay, setSelectedDay] = useState(now.getDate());
  const [reservations, setReservations] = useState([]);
  const [modalInfo, setModalInfo] = useState(null);
  const [showAuth, setShowAuth] = useState(false);

  const floorKeys = Object.keys(buildingData.floors).map(Number).sort((a, b) => a - b);

  useEffect(() => {
    if (jumpToRoom) {
      for (const f of floorKeys) {
        const room = buildingData.floors[f].rooms.find(r => r.id === jumpToRoom);
        if (room) {
          setSelectedRoom(room);
          break;
        }
      }
    }
  }, [jumpToRoom]);

  // 예약 데이터 로드
  useEffect(() => {
    if (!selectedRoom) return;
    const y = now.getFullYear();
    const m = now.getMonth();
    const dateStr = `${y}-${String(m + 1).padStart(2, '0')}-${String(selectedDay).padStart(2, '0')}`;
    fetchReservations(selectedRoom.id, dateStr).then(setReservations);
  }, [selectedRoom, selectedDay]);

  const y = now.getFullYear();
  const m = now.getMonth();
  const dayNames = ['일', '월', '화', '수', '목', '금', '토'];
  const selDate = new Date(y, m, selectedDay);

  function handleEmptySlotClick(h) {
    if (!currentUser) {
      setShowAuth(true);
      return;
    }
    if (currentUser.role === '학생') {
      alert('학생은 직접 시설 예약이 불가합니다.\n동아리장 또는 교수만 예약할 수 있습니다.');
      return;
    }
    setModalInfo({room: selectedRoom, startHour: h});
  }

  function handleReserved() {
    // 예약 후 타임라인 새로고침
    if (!selectedRoom) return;
    const dateStr = `${y}-${String(m + 1).padStart(2, '0')}-${String(selectedDay).padStart(2, '0')}`;
    fetchReservations(selectedRoom.id, dateStr).then(setReservations);
  }

  return (<div className="reservation-view">
    <div className="rv-room-list">
      {floorKeys.map(f => {
        const fd = buildingData.floors[f];
        return (<div key={f} className="rv-floor-group">
          <div className="rv-floor-label">{f}F · {fd.desc}</div>
          {fd.rooms.map(room => (<div
              key={room.id}
              className={`rv-room-item${selectedRoom?.id === room.id ? ' active' : ''}`}
              data-room-id={room.id}
              onClick={() => {
                setSelectedRoom(room);
                setSelectedDay(now.getDate());
              }}
          >
            <div className={`rv-dot room-status-dot ${room.status}`}/>
            <span>{room.name}</span>
          </div>))}
        </div>);
      })}
    </div>
    <div className="rv-main">
      <div className="rv-content">
        <div className="rv-calendar-side">
          <div className="rv-mini-cal">
            <div className="cal-header">
              <span className="cal-month-num">{String(m + 1).padStart(2, '0')}</span>
              <div>
                <div className="cal-month-name">{MONTH_NAMES[m]}</div>
                <div className="cal-year">{y}</div>
              </div>
            </div>
            <MiniCalendar buildingKey={buildingKey} roomId={selectedRoom?.id} selectedDay={selectedDay}
                          onDayClick={d => setSelectedDay(d)}/>
            <div className="cal-legend">
              <div className="cal-legend-item">
                <div className="cal-legend-dot full"/>
                마감
              </div>
              <div className="cal-legend-item">
                <div className="cal-legend-dot partial"/>
                일부
              </div>
              <div className="cal-legend-item">
                <div className="cal-legend-dot empty"/>
                가능
              </div>
            </div>
          </div>
          <hr className="my-3"/>
          <div style={{fontSize: '0.82rem', color: '#718096'}}>
            <div className="mb-2"><strong style={{color: '#2D3748'}}>범례</strong></div>
            <div className="d-flex align-items-center gap-2 mb-1">
              <div style={{
                width: 20, height: 12, background: 'linear-gradient(135deg,#2c5282,#3182ce)', borderRadius: 3,
              }}/>
              <span>승인된 예약</span>
            </div>
            <div className="d-flex align-items-center gap-2 mb-1">
              <div style={{
                width: 20,
                height: 12,
                background: 'repeating-linear-gradient(45deg,#ed8936,#ed8936 4px,#dd6b20 4px,#dd6b20 8px)',
                borderRadius: 3,
              }}/>
              <span>승인 대기</span>
            </div>
            <div className="d-flex align-items-center gap-2">
              <div style={{
                width: 20, height: 12, background: '#F7FAFC', border: '1px solid #e2e8f0', borderRadius: 3,
              }}/>
              <span>예약 가능 (클릭)</span>
            </div>
          </div>
        </div>
        <div className="rv-timeline-side">
          {!selectedRoom ? (<div className="d-flex align-items-center justify-content-center h-100 text-muted">
            <div className="text-center">
              <div style={{fontSize: '2.5rem', marginBottom: '0.5rem'}}>👈</div>
              <div style={{fontSize: '0.95rem'}}>좌측에서 호실을 선택하세요</div>
            </div>
          </div>) : (<>
            <div className="rv-timeline-header">
              <h4>{selectedRoom.name} <span
                  style={{fontWeight: 400, fontSize: '0.9rem', color: '#718096'}}>· {buildingData.name}</span>
              </h4>
              <div className="rv-date-sub">{y}년 {m + 1}월 {selectedDay}일 ({dayNames[selDate.getDay()]})</div>
            </div>
            <div className="timeline">
              {Array.from({length: 13}, (_, i) => i + 9).map(h => {
                const res = reservations.find(r => h >= r.start && h < r.end);
                const isStart = res && h === res.start;
                return (<div key={h} className="tl-slot">
                  <div className="tl-time">{String(h).padStart(2, '0')}:00</div>
                  <div className="tl-content">
                    {isStart && (<div className={`tl-booked ${res.status}`}
                                      style={res.end - res.start > 1 ? {minHeight: (res.end - res.start) * 52 - 8} : undefined}>
                      <div className="tl-title">{res.title}</div>
                      <div
                          className="tl-detail">{res.detail} · {String(res.start).padStart(2, '0')}:00~{String(res.end).padStart(2, '0')}:00
                      </div>
                      <div className="tl-status-tag">{res.status === 'approved' ? '승인됨' : '승인 대기'}</div>
                    </div>)}
                    {!res && (<div className="tl-empty" onClick={() => handleEmptySlotClick(h)}>+ 예약 가능</div>)}
                  </div>
                </div>);
              })}
            </div>
          </>)}
        </div>
      </div>
    </div>
    {modalInfo && (<ReserveModal
        buildingName={buildingData.name}
        room={modalInfo.room}
        selectedDay={selectedDay}
        startHour={modalInfo.startHour}
        onClose={() => setModalInfo(null)}
        onReserved={handleReserved}
    />)}
    {showAuth && <AuthModal onClose={() => setShowAuth(false)}/>}
  </div>);
}
