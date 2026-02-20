export function MiniCalendar({buildingKey, roomId, selectedDay, onDayClick}) {
  const now = new Date();
  const y = now.getFullYear();
  const m = now.getMonth();
  const today = now.getDate();
  const firstDay = new Date(y, m, 1).getDay();
  const daysInMonth = new Date(y, m + 1, 0).getDate();
  const daysInPrev = new Date(y, m, 0).getDate();

  const weeks = [];
  let week = [];

  // 이전 달 채우기
  for (let i = 0; i < firstDay; i++) {
    week.push({day: daysInPrev - firstDay + 1 + i, otherMonth: true});
  }

  // 이번 달
  for (let d = 1; d <= daysInMonth; d++) {
    if (week.length === 7) {
      weeks.push(week);
      week = [];
    }
    const seed = ((roomId || buildingKey || 'x').charCodeAt(0)) + d;
    const rv = seed % 100;
    let resClass = '';
    if (rv < 20) resClass = 'res-full'; else if (rv < 50) resClass = 'res-partial'; else resClass = 'res-empty';

    week.push({
      day: d,
      otherMonth: false,
      isToday: d === today,
      isSelected: d === selectedDay,
      isSunday: week.length === 0,
      isSaturday: week.length === 6,
      resClass,
    });
  }

  // 다음 달 채우기
  let nd = 1;
  while (week.length < 7) {
    week.push({day: nd++, otherMonth: true});
  }
  weeks.push(week);

  return (<table className="cal-table">
    <thead>
    <tr>
      <th>S</th>
      <th>M</th>
      <th>T</th>
      <th>W</th>
      <th>T</th>
      <th>F</th>
      <th>S</th>
    </tr>
    </thead>
    <tbody>
    {weeks.map((wk, wi) => (<tr key={wi}>
      {wk.map((cell, ci) => {
        if (cell.otherMonth) {
          return <td key={ci} className="other-month"><span className="day-num">{cell.day}</span></td>;
        }
        const cls = [cell.isSunday && 'sunday', cell.isSaturday && 'saturday', cell.isToday && 'today', cell.isSelected && 'selected', cell.resClass].filter(Boolean).join(' ');

        return (<td
            key={ci}
            className={cls}
            style={onDayClick ? {cursor: 'pointer'} : undefined}
            onClick={() => onDayClick && onDayClick(cell.day)}
        >
          <span className="day-num">{cell.day}</span>
        </td>);
      })}
    </tr>))}
    </tbody>
  </table>);
}
