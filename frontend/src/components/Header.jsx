import {useState} from 'react';
import {Link} from 'react-router-dom';
import {useAuth} from '../context/AuthContext.jsx';
import {AuthModal} from './AuthModal.jsx';

export function Header() {
  const {currentUser, logout} = useAuth();
  const [showAuth, setShowAuth] = useState(false);

  return (<>
    <header className="campus-header">
      <div className="container d-flex align-items-center justify-content-between">
        <div>
          <h1 style={{fontSize: '1.4rem', fontWeight: 700, margin: 0}}>
            <Link to="/" style={{color: 'white', textDecoration: 'none'}}>
              Campus 시설 관리 시스템
            </Link>
          </h1>
          <div className="subtitle">캠퍼스 시설 예약 및 동아리 관리</div>
        </div>
        <div className="d-flex gap-2 align-items-center">
          {!currentUser ? (<>
            <button className="btn btn-outline-light btn-sm" onClick={() => setShowAuth(true)}>내 예약</button>
            <button className="btn btn-light btn-sm" onClick={() => setShowAuth(true)}>로그인</button>
          </>) : (<div className="user-display">
            <span className="user-name">{currentUser.name}</span>
            <span className="user-role">{currentUser.role}</span>
            <button className="btn btn-outline-light btn-sm">내 예약</button>
            <button className="btn btn-outline-light btn-sm" onClick={logout}>로그아웃</button>
          </div>)}
        </div>
      </div>
    </header>
    {showAuth && <AuthModal onClose={() => setShowAuth(false)}/>}
  </>);
}
