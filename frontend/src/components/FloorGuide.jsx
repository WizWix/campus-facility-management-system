import {useState} from 'react';

const STATUS_LABEL = {available: '예약 가능', partial: '일부 예약', full: '예약 마감'};
const STATUS_COLOR = {available: 'success', partial: 'warning', full: 'danger'};

export function FloorGuide({buildingData, onRoomClick}) {
  const floorKeys = Object.keys(buildingData.floors).map(Number).sort((a, b) => b - a);
  const [activeFloor, setActiveFloor] = useState(floorKeys[0]);

  const floorData = buildingData.floors[activeFloor];

  const categories = {};
  floorData.rooms.forEach(r => {
    if (!categories[r.category]) categories[r.category] = [];
    categories[r.category].push(r);
  });

  return (<div className="floor-guide">
    <div className="floor-list">
      {floorKeys.map(f => {
        const fd = buildingData.floors[f];
        return (<div key={f} className={`floor-item${f === activeFloor ? ' active' : ''}`}
                     onClick={() => setActiveFloor(f)}>
          <div className="floor-num">{f}F</div>
          <div>
            <div style={{fontWeight: 500, fontSize: '0.9rem'}}>{fd.desc}</div>
            <div className="floor-desc">호실 {fd.rooms.length}개</div>
          </div>
          <div className="floor-pin">{fd.rooms.length}</div>
        </div>);
      })}
    </div>
    <div className="floor-detail">
      <div className="d-flex justify-content-between align-items-end">
        <div className="floor-detail-header flex-grow-1">
          <h3>{activeFloor}F</h3>
          <div className="floor-subtitle">{buildingData.name} · {floorData.desc}</div>
        </div>
        <div className="status-legend mb-3">
          <div className="status-legend-item">
            <div className="room-status-dot available"/>
            예약 가능
          </div>
          <div className="status-legend-item">
            <div className="room-status-dot partial"/>
            일부 예약
          </div>
          <div className="status-legend-item">
            <div className="room-status-dot full"/>
            예약 마감
          </div>
        </div>
      </div>
      {Object.entries(categories).map(([cat, rooms]) => (<div key={cat} className="room-category">
        <div className="room-category-title">{cat}<span className="room-category-count">{rooms.length}</span>
        </div>
        {rooms.map(room => (<div key={room.id} className="room-row" onClick={() => onRoomClick(room.id)}>
          <div className="d-flex align-items-center">
            <div className={`room-status-dot ${room.status}`}/>
            <div>
              <div className="room-name">{room.name}</div>
              <div className="room-cap">{room.type} · 수용 {room.capacity}명</div>
            </div>
          </div>
          <span className={`badge bg-${STATUS_COLOR[room.status]}`} style={{fontSize: '0.72rem'}}>
                  {STATUS_LABEL[room.status]}
                </span>
        </div>))}
      </div>))}
    </div>
  </div>);
}
