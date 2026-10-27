import { Navigate, Route, Routes } from 'react-router-dom'
import { useAuth } from './auth/AuthContext'
import Layout from './components/Layout'
import LoginPage from './pages/LoginPage'
import RegisterPage from './pages/RegisterPage'
import UserDashboard from './pages/UserDashboard'
import TrainerDashboard from './pages/TrainerDashboard'
import AdminDashboard from './pages/AdminDashboard'

function PrivateRoute({ children, roles }) {
  const { user, loading } = useAuth()
  if (loading) return <p>Ładowanie…</p>
  if (!user) return <Navigate to="/login" replace />
  if (roles && !roles.includes(user.role)) return <Navigate to="/" replace />
  return children
}

export default function App() {
  return (
      <Routes>
        <Route element={<Layout />}>
          <Route index element={<Navigate to="/login" replace />} />
          <Route path="login" element={<LoginPage />} />
          <Route path="register" element={<RegisterPage />} />
          <Route
              path="app"
              element={
                <PrivateRoute roles={['USER']}>
                  <UserDashboard />
                </PrivateRoute>
              }
          />
          <Route
              path="trainer"
              element={
                <PrivateRoute roles={['TRAINER']}>
                  <TrainerDashboard />
                </PrivateRoute>
              }
          />
          <Route
              path="admin"
              element={
                <PrivateRoute roles={['ADMIN']}>
                  <AdminDashboard />
                </PrivateRoute>
              }
          />
        </Route>
      </Routes>
  )
}