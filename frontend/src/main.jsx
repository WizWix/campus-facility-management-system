import {StrictMode} from 'react';
import {createRoot} from 'react-dom/client';
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import './styles/normalize.css';
import './styles/index.css';

createRoot(document.getElementById('root')).render(
    <StrictMode>
      <AuthProvider>
        <BrowserRouter>
          <Header/>
          <main>
            <Routes>
              {/* Public */}
              <Route path="/" element={<HomePage/>}/>
              <Route path="/login" element={<LoginPage/>}/>
              <Route path="/register" element={<RegisterPage/>}/>
              <Route path="/search" element={<SearchPage/>}/>
              <Route path="/user/:userId" element={<UserProfilePage/>}/>
              {/* Dynamic Category Routes */}
              <Route path="/:category" element={<CategoryListPage/>}/>
              <Route path="/:category/:id" element={<ArticleViewPage/>}/>
              <Route path="/:category/:id/discussion" element={<DiscussionViewPage/>}/>
              {/* Protected (User) */}
              <Route path="/:category/:id/edit" element={<ProtectedRoute><EditorPage/></ProtectedRoute>}/>
              <Route path="/my/*" element={<ProtectedRoute><MyPageLayoutPage/></ProtectedRoute>}/>
              <Route path="/new" element={<ProtectedRoute><EditorPage/></ProtectedRoute>}/>
              {/* Protected (Admin) */}
              <Route path="/manage" element={<ProtectedRoute><AdminDashboardPage/></ProtectedRoute>}/>
            </Routes>
          </main>
        </BrowserRouter>
      </AuthProvider>
    </StrictMode>,
);
