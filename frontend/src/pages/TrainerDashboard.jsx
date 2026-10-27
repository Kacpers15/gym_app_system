import { useEffect, useState } from 'react'
import { trainerApi } from '../api/client'

export default function TrainerDashboard() {
    const [startTime, setStartTime] = useState('')
    const [endTime, setEndTime] = useState('')
    const [trainings, setTrainings] = useState([])
    const [error, setError] = useState('')
    const [msg, setMsg] = useState('')

    async function load() {
        setTrainings(await trainerApi.myTrainings())
    }

    useEffect(() => {
        load().catch((e) => setError(e.message))
    }, [])

    async function addSlot(e) {
        e.preventDefault()
        setError('')
        try {
            await trainerApi.addAvailability({ startTime, endTime })
            setMsg('Dodano dostępność (musi być dokładnie 60 min)')
            setStartTime('')
            setEndTime('')
        } catch (err) {
            setError(err.message)
        }
    }

    async function complete(id) {
        await trainerApi.complete(id)
        await load()
    }

    return (
        <div className="stack">
            <h1>Panel trenera</h1>
            {msg && <p className="ok">{msg}</p>}
            {error && <p className="error">{error}</p>}

            <section className="card">
                <h2>Dodaj slot dostępności</h2>
                <form className="form" onSubmit={addSlot}>
                    <label>
                        Start
                        <input
                            type="datetime-local"
                            value={startTime}
                            onChange={(e) => setStartTime(e.target.value)}
                            required
                        />
                    </label>
                    <label>
                        Koniec (+60 min)
                        <input
                            type="datetime-local"
                            value={endTime}
                            onChange={(e) => setEndTime(e.target.value)}
                            required
                        />
                    </label>
                    <button type="submit">Dodaj</button>
                </form>
                <p className="hint">
                    Backend wymaga dokładnie 60 minut. Format ISO: zamień `datetime-local` na
                    `2026-11-10T10:00:00` — poniżej helper.
                </p>
            </section>

            <section className="card">
                <h2>Moje treningi</h2>
                <ul className="list">
                    {trainings.map((t) => (
                        <li key={t.id}>
                            #{t.id} user={t.userId} {t.startTime} [{t.status}]{' '}
                            {t.status === 'BOOKED' && (
                                <button type="button" onClick={() => complete(t.id)}>
                                    Oznacz jako zrealizowany
                                </button>
                            )}
                        </li>
                    ))}
                </ul>
            </section>
        </div>
    )
}