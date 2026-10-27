import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'

export default function RegisterPage() {
    const { register } = useAuth()
    const navigate = useNavigate()
    const [form, setForm] = useState({
        firstName: '',
        lastName: '',
        email: '',
        password: '',
    })
    const [error, setError] = useState('')

    function set(field, value) {
        setForm((f) => ({ ...f, [field]: value }))
    }

    async function onSubmit(e) {
        e.preventDefault()
        setError('')
        try {
            await register(form)
            navigate('/app')
        } catch (err) {
            setError(err.message)
        }
    }

    return (
        <section className="card">
            <h1>Rejestracja</h1>
            <form onSubmit={onSubmit} className="form">
                <label>
                    Imię
                    <input value={form.firstName} onChange={(e) => set('firstName', e.target.value)} required />
                </label>
                <label>
                    Nazwisko
                    <input value={form.lastName} onChange={(e) => set('lastName', e.target.value)} required />
                </label>
                <label>
                    Email
                    <input
                        type="email"
                        value={form.email}
                        onChange={(e) => set('email', e.target.value)}
                        required
                    />
                </label>
                <label>
                    Hasło (min. 8)
                    <input
                        type="password"
                        value={form.password}
                        onChange={(e) => set('password', e.target.value)}
                        minLength={8}
                        required
                    />
                </label>
                {error && <p className="error">{error}</p>}
                <button type="submit">Utwórz konto</button>
            </form>
            <p>
                Masz konto? <Link to="/login">Zaloguj</Link>
            </p>
        </section>
    )
}