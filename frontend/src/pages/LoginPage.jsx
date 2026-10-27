import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'

export default function LoginPage() {
    const { login } = useAuth()
    const navigate = useNavigate()
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [error, setError] = useState('')

    async function onSubmit(e) {
        e.preventDefault()
        setError('')
        try {
            const res = await login(email, password)
            if (res.role === 'ADMIN') navigate('/admin')
            else if (res.role === 'TRAINER') navigate('/trainer')
            else navigate('/app')
        } catch (err) {
            setError(err.message)
        }
    }

    return (
        <section className="card">
            <h1>Logowanie</h1>
            <form onSubmit={onSubmit} className="form">
                <label>
                    Email
                    <input value={email} onChange={(e) => setEmail(e.target.value)} type="email" required />
                </label>
                <label>
                    Hasło
                    <input
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        type="password"
                        required
                    />
                </label>
                {error && <p className="error">{error}</p>}
                <button type="submit">Zaloguj</button>
            </form>
            <p>
                Nie masz konta? <Link to="/register">Zarejestruj się</Link>
            </p>
            <p className="hint">
                Demo: admin@gym.local / Admin123! · trainer@gym.local / Trainer123!
            </p>
        </section>
    )
}