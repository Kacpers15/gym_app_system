import { useEffect, useState } from 'react'
import { adminApi, groupClassApi, membershipApi, trainerApi } from '../api/client'

export default function AdminDashboard() {
    const [users, setUsers] = useState([])
    const [trainers, setTrainers] = useState([])
    const [classes, setClasses] = useState([])
    const [error, setError] = useState('')
    const [msg, setMsg] = useState('')

    const [classForm, setClassForm] = useState({
        name: '',
        description: '',
        trainerId: '',
        startTime: '',
        durationMinutes: 60,
        maxParticipants: 10,
    })

    const [typeForm, setTypeForm] = useState({
        name: '',
        description: '',
        price: '99.99',
        durationDays: 30,
        active: true,
    })

    async function load() {
        const [u, t, c] = await Promise.all([
            adminApi.users(),
            trainerApi.list(),
            groupClassApi.list(),
        ])
        setUsers(u)
        setTrainers(t)
        setClasses(c)
        if (t[0] && !classForm.trainerId) {
            setClassForm((f) => ({ ...f, trainerId: String(t[0].id) }))
        }
    }

    useEffect(() => {
        load().catch((e) => setError(e.message))
    }, [])

    function toApiDate(v) {
        return v.length === 16 ? `${v}:00` : v
    }

    async function createClass(e) {
        e.preventDefault()
        try {
            await groupClassApi.adminCreate({
                ...classForm,
                trainerId: Number(classForm.trainerId),
                durationMinutes: Number(classForm.durationMinutes),
                maxParticipants: Number(classForm.maxParticipants),
                startTime: toApiDate(classForm.startTime),
            })
            setMsg('Utworzono zajęcia')
            await load()
        } catch (err) {
            setError(err.message)
        }
    }

    async function createType(e) {
        e.preventDefault()
        try {
            await membershipApi.adminCreateType({
                ...typeForm,
                price: Number(typeForm.price),
                durationDays: Number(typeForm.durationDays),
            })
            setMsg('Utworzono typ karnetu')
        } catch (err) {
            setError(err.message)
        }
    }

    return (
        <div className="stack">
            <h1>Panel administratora</h1>
            {msg && <p className="ok">{msg}</p>}
            {error && <p className="error">{error}</p>}

            <section className="card">
                <h2>Użytkownicy</h2>
                <ul className="list">
                    {users.map((u) => (
                        <li key={u.id}>
                            #{u.id} {u.firstName} {u.lastName} — {u.email} [{u.role}]
                        </li>
                    ))}
                </ul>
            </section>

            <section className="card">
                <h2>Nowe zajęcia grupowe</h2>
                <form className="form" onSubmit={createClass}>
                    <label>
                        Nazwa
                        <input
                            value={classForm.name}
                            onChange={(e) => setClassForm({ ...classForm, name: e.target.value })}
                            required
                        />
                    </label>
                    <label>
                        Opis
                        <input
                            value={classForm.description}
                            onChange={(e) => setClassForm({ ...classForm, description: e.target.value })}
                        />
                    </label>
                    <label>
                        Trener
                        <select
                            value={classForm.trainerId}
                            onChange={(e) => setClassForm({ ...classForm, trainerId: e.target.value })}
                        >
                            {trainers.map((t) => (
                                <option key={t.id} value={t.id}>
                                    {t.firstName} {t.lastName}
                                </option>
                            ))}
                        </select>
                    </label>
                    <label>
                        Start
                        <input
                            type="datetime-local"
                            value={classForm.startTime}
                            onChange={(e) => setClassForm({ ...classForm, startTime: e.target.value })}
                            required
                        />
                    </label>
                    <label>
                        Czas (min)
                        <input
                            type="number"
                            value={classForm.durationMinutes}
                            onChange={(e) => setClassForm({ ...classForm, durationMinutes: e.target.value })}
                        />
                    </label>
                    <label>
                        Limit miejsc
                        <input
                            type="number"
                            value={classForm.maxParticipants}
                            onChange={(e) => setClassForm({ ...classForm, maxParticipants: e.target.value })}
                        />
                    </label>
                    <button type="submit">Utwórz</button>
                </form>
            </section>

            <section className="card">
                <h2>Nadchodzące zajęcia</h2>
                <ul className="list">
                    {classes.map((c) => (
                        <li key={c.id}>
                            {c.name} — {c.startTime} (wolne {c.freeSpots})
                        </li>
                    ))}
                </ul>
            </section>

            <section className="card">
                <h2>Nowy typ karnetu</h2>
                <form className="form" onSubmit={createType}>
                    <label>
                        Nazwa
                        <input
                            value={typeForm.name}
                            onChange={(e) => setTypeForm({ ...typeForm, name: e.target.value })}
                            required
                        />
                    </label>
                    <label>
                        Cena
                        <input
                            value={typeForm.price}
                            onChange={(e) => setTypeForm({ ...typeForm, price: e.target.value })}
                            required
                        />
                    </label>
                    <label>
                        Dni ważności
                        <input
                            type="number"
                            value={typeForm.durationDays}
                            onChange={(e) => setTypeForm({ ...typeForm, durationDays: e.target.value })}
                        />
                    </label>
                    <button type="submit">Dodaj</button>
                </form>
            </section>
        </div>
    )
}