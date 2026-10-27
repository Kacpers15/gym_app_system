import { Link, Outlet } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'

export default function Layout() {
    const { user, logout } = useAuth()

    return (
        <div className="app">
            <header className="topbar">
                <Link to="/" className="brand">
                    GymApp
                </Link>
                <nav>
                    {user?.role === 'USER' && <Link to="/app">Panel</Link>}
                    {user?.role === 'TRAINER' && <Link to="/trainer">Trener</Link>}
                    {user?.role === 'ADMIN' && <Link to="/admin">Admin</Link>}
                </nav>
                <div className="userbox">
                    {user ? (
                        <>
              <span>
                {user.firstName} ({user.role})
              </span>
                            <button type="button" onClick={logout}>
                                Wyloguj
                            </button>
                        </>
                    ) : (
                        <>
                            <Link to="/login">Logowanie</Link>
                            <Link to="/register">Rejestracja</Link>
                        </>
                    )}
                </div>
            </header>
            <main className="content">
                <Outlet />
            </main>
        </div>
    )
}