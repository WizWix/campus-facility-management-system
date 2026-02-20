import {useState} from 'react';
import {useAuth} from '../context/AuthContext.jsx';
import {loginApi, signupApi} from '../data/api.js';

function isKorean(str) {
  return /^[가-힣]{2,5}$/.test(str);
}

export function AuthModal({onClose}) {
  const {login} = useAuth();
  const [tab, setTab] = useState('login'); // 'login' | 'signup'
  const [selectedRole, setSelectedRole] = useState(null);

  // 로그인 폼
  const [loginId, setLoginId] = useState('');
  const [loginPw, setLoginPw] = useState('');

  // 회원가입 폼
  const [signupName, setSignupName] = useState('');
  const [signupId, setSignupId] = useState('');
  const [signupPw, setSignupPw] = useState('');
  const [signupPw2, setSignupPw2] = useState('');

  async function handleLogin() {
    if (!loginId || !loginPw) {
      alert('아이디와 비밀번호를 입력해주세요.');
      return;
    }
    try {
      const user = await loginApi(loginId, loginPw);
      login(user);
      onClose();
    } catch (e) {
      alert('아이디 또는 비밀번호가 올바르지 않습니다.');
    }
  }

  async function handleSignup() {
    if (!selectedRole) {
      alert('회원 유형을 선택해주세요.');
      return;
    }
    if (!isKorean(signupName)) {
      alert('이름은 한글 실명(2~5자)으로 입력해주세요.\n닉네임은 사용할 수 없습니다.');
      return;
    }
    if (!signupId || signupId.length < 4) {
      alert('아이디는 4자 이상 입력해주세요.');
      return;
    }
    if (signupPw.length < 8) {
      alert('비밀번호는 8자 이상이어야 합니다.');
      return;
    }
    if (signupPw !== signupPw2) {
      alert('비밀번호가 일치하지 않습니다.');
      return;
    }

    try {
      const user = await signupApi(signupId, signupPw, signupName, selectedRole);
      login(user);
      onClose();
      alert(`✅ 회원가입이 완료되었습니다!\n\n${signupName}(${selectedRole})님 환영합니다.`);
    } catch (e) {
      alert(e.message || '회원가입에 실패했습니다.');
    }
  }

  const roles = [{key: '학생', icon: '🎓', label: '학생', desc: '동아리 가입 가능\n시설 예약 불가'}, {
    key: '교수', icon: '👨‍🏫', label: '교수', desc: '수업 개설\n반복 예약 가능',
  }, {key: '동아리장', icon: '⭐', label: '동아리장', desc: '동아리 개설\n시설 예약 가능'}];

  return (<div className="modal-bg show" onClick={e => {
    if (e.target === e.currentTarget) onClose();
  }}>
    <div className="modal-box" style={{width: 440}}>
      <div className="modal-hd">
        <button className="modal-close" onClick={onClose}>✕</button>
        <h3>한빛대학교 시설 관리 시스템</h3>
      </div>
      <div className="modal-bd">
        {/* 탭 */}
        <div className="auth-tabs">
          <div className={`auth-tab${tab === 'login' ? ' active' : ''}`} onClick={() => setTab('login')}>로그인</div>
          <div className={`auth-tab${tab === 'signup' ? ' active' : ''}`} onClick={() => setTab('signup')}>회원가입
          </div>
        </div>

        {tab === 'login' ? (<>
          <div className="mb-3">
            <label className="form-label">아이디</label>
            <input type="text" className="form-control" placeholder="아이디를 입력하세요" value={loginId}
                   onChange={e => setLoginId(e.target.value)}/>
          </div>
          <div className="mb-3">
            <label className="form-label">비밀번호</label>
            <input type="password" className="form-control" placeholder="비밀번호를 입력하세요" value={loginPw}
                   onChange={e => setLoginPw(e.target.value)}/>
          </div>
          <button className="btn btn-primary w-100" onClick={handleLogin}>로그인</button>
        </>) : (<>
          <div className="mb-3">
            <label className="form-label">회원 유형 선택</label>
            <div className="role-cards">
              {roles.map(r => (<div
                  key={r.key}
                  className={`role-card${selectedRole === r.key ? ' selected' : ''}`}
                  onClick={() => setSelectedRole(r.key)}
              >
                <div className="role-icon">{r.icon}</div>
                <div className="role-label">{r.label}</div>
                <div className="role-desc">{r.desc.split('\n').map((line, i) => <span
                    key={i}>{line}<br/></span>)}</div>
              </div>))}
            </div>
          </div>
          <div className="mb-3">
            <label className="form-label">이름 (한글)</label>
            <input type="text" className="form-control" placeholder="실명을 입력하세요 (예: 홍길동)" value={signupName}
                   onChange={e => setSignupName(e.target.value)}/>
            <div className="form-text text-muted" style={{fontSize: '.75rem'}}>닉네임은 사용 불가하며, 반드시 한글 실명을
              입력해주세요.
            </div>
          </div>
          <div className="mb-3">
            <label className="form-label">아이디</label>
            <input type="text" className="form-control" placeholder="영문/숫자 조합" value={signupId}
                   onChange={e => setSignupId(e.target.value)}/>
          </div>
          <div className="mb-3">
            <label className="form-label">비밀번호</label>
            <input type="password" className="form-control" placeholder="8자 이상" value={signupPw}
                   onChange={e => setSignupPw(e.target.value)}/>
          </div>
          <div className="mb-3">
            <label className="form-label">비밀번호 확인</label>
            <input type="password" className="form-control" placeholder="비밀번호를 다시 입력하세요" value={signupPw2}
                   onChange={e => setSignupPw2(e.target.value)}/>
          </div>
          <button className="btn btn-primary w-100" onClick={handleSignup}>회원가입</button>
        </>)}
      </div>
    </div>
  </div>);
}
