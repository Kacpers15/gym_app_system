import { useEffect, useState } from 'react'
import {
    groupClassApi,
    membershipApi,
    personalTrainingApi,
    trainerApi,
} from '../api/client'

export default function UserDashboard() {
    const [trainers, setTrainers] = useState([])
    const [selectedTrainer, setSelectedTrainer] = useState(null)
    const [slots, setSlots] = useState([])
    const [myTrainings, setMyTrainings] = useState([])
    const [classes, setClasses] = useState([])
    const [types, setTypes] = useState([])
    const [memberships, setMemberships] = useState([])
    const [msg, setMsg] = useState('')
    const [error, setError] = useState('')

    async function load() {
        setError('')
        try {
            const [t, mt, c, m, typesList] = await Promise.all([
                trainerApi.list(),
                personalTrainingApi.my(),
                groupClassApi.list(),
                membershipApi.my(),
                membershipApi.types(),
            ])
            setTrainers(t)
            setMyTrainings(mt)
            setClasses(c)
            setMemberships(m)
            setTypes(typesList)
        } catch (err) {
            setError(err.message)
        }
    }

    useEffect(() => {
        load()
    }, [])

    async function openTrainer(t) {
        setSelectedTrainer(t)
        setSlots(await trainerApi.availability(t.id))
    }

    async function book(slotId) {
        try {
            await personalTrainingApi.book(slotId)
            setMsg('Zarezerwowano trening')
            await load()
            if (selectedTrainer) await openTrainer(selectedTrainer)
        } catch (err) {
            setError(err.message)
        }
    }

    async function cancelPt(id) {
        await personalTrainingApi.cancel(id)
        await load()
    }

    async function enroll(id) {
        try {
            await groupClassApi.enroll(id)
            setMsg('Zapisano na zajęcia')
            await load()
        } catch (err) {
            setError(err.message)
        }
    }

    async function buy(typeId) {
        try {
            await membershipApi.purchase(typeId)
            setMsg('Karnet kupiony')
            await load()
        } catch (err) {
            setError(err.message)
        }
    }

    return (
        <div className="stack">
            <h1>Panel użytkownika</h1>
            {msg && <p className="ok">{msg}</p>}
            {error && <p className="error">{error}</p>}

            <section className="card">
                <h2>Trenerzy personalni</h2>
                <ul className="list">
                    {trainers.map((t) => (
                        <li key={t.id}>
                            <button type="button" onClick={() => openTrainer(t)}>
                                {t.firstName} {t.lastName}
                            </button>
                        </li>
                    ))}
                </ul>
                {selectedTrainer && (
                    <>
                        <h3>
                            Wolne terminy: {selectedTrainer.firstName} {selectedTrainer.lastName}
                        </h3>
                        <ul className="list">
                            {slots.map((s) => (
                                <li key={s.id}>
                                    {s.startTime} – {s.endTime}{' '}
                                    <button type="button" onClick={() => book(s.id)}>
                                        Rezerwuj
                                    </button>
                                </li>
                            ))}
                            {slots.length === 0 && <li>Brak wolnych slotów</li>}
                        </ul>
                    </>
                )}
            </section>

            <section className="card">
                <h2>Moje treningi personalne</h2>
                <ul className="list">
                    {myTrainings.map((t) => (
                        <li key={t.id}>
                            #{t.id} {t.startTime} [{t.status}]{' '}
                            {t.status === 'BOOKED' && (
                                <button type="button" onClick={() => cancelPt(t.id)}>
                                    Anuluj
                                </button>
                            )}
                        </li>
                    ))}
                </ul>
            </section>

            <section className="card">
                <h2>Zajęcia grupowe</h2>
                <ul className="list">
                    {classes.map((c) => (
                        <li key={c.id}>
                            <strong>{c.name}</strong> — {c.startTime}, wolne: {c.freeSpots}/{c.maxParticipants}{' '}
                            <button type="button" onClick={() => enroll(c.id)} disabled={c.freeSpots < 1}>
                                Zapisz się
                            </button>
                        </li>
                    ))}
                </ul>
            </section>

            <section className="card">
                <h2>Karnety</h2>
                <ul className="list">
                    {types.map((t) => (
                        <li key={t.id}>
                            {t.name} — {t.price} zł / {t.durationDays} dni{' '}
                            <button type="button" onClick={() => buy(t.id)}>
                                Kup
                            </button>
                        </li>
                    ))}
                </ul>
                <h3>Moje zakupy</h3>
                <ul className="list">
                    {memberships.map((m) => (
                        <li key={m.id}>
                            {m.planName}: {m.startDate} → {m.endDate} ({m.status}), zapłacono {m.pricePaid}
                        </li>
                    ))}
                </ul>
            </section>
        </div>
    )
}