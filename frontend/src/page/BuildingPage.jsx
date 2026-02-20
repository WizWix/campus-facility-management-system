import {useEffect, useState} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import {FloorGuide} from '../components/FloorGuide.jsx';
import {MiniCalendar} from '../components/MiniCalendar.jsx';
import {ReservationView} from '../components/ReservationView.jsx';
import {fetchBuildingDetail, fetchCurrentSemester} from '../data/api.js';

const MONTH_NAMES = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];

export function BuildingPage() {
  const {buildingKey} = useParams();
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState('floor');
  const [jumpToRoom, setJumpToRoom] = useState(null);
  const [data, setData] = useState(null);
  const [semester, setSemester] = useState(null);

  useEffect(() => {
    fetchBuildingDetail(buildingKey).then(setData);
    fetchCurrentSemester().then(setSemester);
  }, [buildingKey]);

  if (!data) return <div className="container mt-4">로딩 중...</div>;

  const now = new Date();
  const y = now.getFullYear();
  const m = now.getMonth();

  function handleTabChange(tab) {
    setActiveTab(tab);
    setJumpToRoom(null);
  }

  function handleRoomClick(roomId) {
    setJumpToRoom(roomId);
    setActiveTab('schedule');
  }

  return (<div id="buildingView" className="active">
    <div className="breadcrumb-nav">
      <div className="container">
        <a href="#" onClick={e => {
          e.preventDefault();
          navigate('/');
        }}>HOME</a>
        <span className="sep">/</span>
        <a href="#" onClick={e => {
          e.preventDefault();
          navigate('/');
        }}>캠퍼스 지도</a>
        <span className="sep">/</span>
        <span className="current">{data.name}</span>
        <span className="sep">/</span>
        <span className="current">{activeTab === 'floor' ? '층별 안내' : '예약 현황'}</span>
      </div>
    </div>

    <div className="store-info-area">
      <div className="container">
        <div className="store-name">
          <span>{data.name}</span>
          <a className="back-link" onClick={() => navigate('/')}>← 캠퍼스 지도로 돌아가기</a>
        </div>
        <div className="info-grid">
          <div className="info-col">
            <div className="semester-box">
              <div className="semester-icon">📚</div>
              <div className="semester-label">현재 학기</div>
              <div className="semester-value">{semester?.name || ''}</div>
              <div className="semester-sub">
                {semester ? `${semester.start.replace(/-/g, '.')} ~ ${semester.end.replace(/-/g, '.')}` : ''}
              </div>
            </div>
          </div>
          <div className="info-col">
            <ul className="hours-list">
              <li><span className="hours-icon">⏰</span> 평일 이용시간 09:00 ~ 22:00</li>
              <li><span className="hours-icon">⏰</span> 주말 이용시간 10:00 ~ 18:00</li>
              <li><span className="hours-icon">📋</span> 예약 가능 단위: 정시 1시간</li>
              <li><span className="hours-icon">ℹ️</span> 시설 예약은 관리자 승인 후 확정</li>
            </ul>
            <div className="hours-today">오늘은 <strong>09:00</strong>부터 <strong>22:00</strong>까지 이용 가능합니다.</div>
          </div>
          <div className="info-col">
            <div className="calendar-box">
              <div className="cal-header">
                <span className="cal-month-num">{String(m + 1).padStart(2, '0')}</span>
                <div>
                  <div className="cal-month-name">{MONTH_NAMES[m]}</div>
                  <div className="cal-year">{y}</div>
                </div>
              </div>
              <MiniCalendar buildingKey={buildingKey}/>
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
                <div className="cal-legend-item">
                  <div className="cal-legend-dot today-dot"/>
                  오늘
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div className="building-tabs">
      <button className={activeTab === 'floor' ? 'active' : ''} onClick={() => handleTabChange('floor')}>층별 안내
      </button>
      <button className={activeTab === 'schedule' ? 'active' : ''} onClick={() => handleTabChange('schedule')}>예약
        현황
      </button>
    </div>

    {activeTab === 'floor' ? (<FloorGuide buildingData={data} onRoomClick={handleRoomClick}/>) : (
        <ReservationView buildingKey={buildingKey} buildingData={data} jumpToRoom={jumpToRoom}/>)}
  </div>);
}
