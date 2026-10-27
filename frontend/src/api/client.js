const TOKEN_KEY = 'gym_token'

export function getToken() {
    return localStorage.getItem(TOKEN_KEY)
}

export function setToken(token) {
    localStorage.setItem(TOKEN_KEY, token)
}

export function clearToken() {
    localStorage.removeItem(TOKEN_KEY)
}

export async function api(path, options = {}) {
    const headers = {
        'Content-Type': 'application/json',
        ...(options.headers || {}),
    }

    const token = getToken()
    if (token) {
        headers.Authorization = `Bearer ${token}`
    }

    const res = await fetch(path, { ...options, headers })

    if (res.status === 204) return null

    const text = await res.text()
    let data = null
    if (text) {
        try {
            data = JSON.parse(text)
        } catch {
            data = text
        }
    }

    if (!res.ok) {
        const msg = data?.message || data?.error || res.statusText || 'Request failed'
        throw new Error(msg)
    }

    return data
}

export const authApi = {
    register: (body) => api('/api/auth/register', { method: 'POST', body: JSON.stringify(body) }),
    login: (body) => api('/api/auth/login', { method: 'POST', body: JSON.stringify(body) }),
}

export const userApi = {
    me: () => api('/api/users/me'),
    updateMe: (body) => api('/api/users/me', { method: 'PUT', body: JSON.stringify(body) }),
}

export const trainerApi = {
    list: () => api('/api/trainers'),
    availability: (id) => api(`/api/trainers/${id}/availability`),
    addAvailability: (body) => api('/api/trainer/availability', { method: 'POST', body: JSON.stringify(body) }),
    deleteAvailability: (id) => api(`/api/trainer/availability/${id}`, { method: 'DELETE' }),
    myTrainings: () => api('/api/trainer/personal-trainings'),
    complete: (id) => api(`/api/trainer/personal-trainings/${id}/complete`, { method: 'PATCH' }),
}

export const personalTrainingApi = {
    book: (availabilityId) =>
        api('/api/personal-trainings', {
            method: 'POST',
            body: JSON.stringify({ availabilityId }),
        }),
    my: () => api('/api/personal-trainings/me'),
    cancel: (id) => api(`/api/personal-trainings/${id}/cancel`, { method: 'PATCH' }),
}

export const groupClassApi = {
    list: (params = {}) => {
        const q = new URLSearchParams()
        if (params.name) q.set('name', params.name)
        if (params.trainerId) q.set('trainerId', params.trainerId)
        const s = q.toString()
        return api(`/api/group-classes${s ? `?${s}` : ''}`)
    },
    enroll: (id) => api(`/api/group-classes/${id}/bookings`, { method: 'POST' }),
    cancelEnrollment: (id) => api(`/api/group-classes/${id}/bookings/me`, { method: 'DELETE' }),
    myBookings: () => api('/api/group-classes/bookings/me'),
    adminCreate: (body) =>
        api('/api/admin/group-classes', { method: 'POST', body: JSON.stringify(body) }),
    adminCancel: (id) => api(`/api/admin/group-classes/${id}/cancel`, { method: 'PATCH' }),
    adminBookings: (id) => api(`/api/admin/group-classes/${id}/bookings`),
}

export const membershipApi = {
    types: () => api('/api/membership-types'),
    purchase: (membershipTypeId) =>
        api('/api/memberships/purchase', {
            method: 'POST',
            body: JSON.stringify({ membershipTypeId }),
        }),
    my: () => api('/api/memberships/me'),
    adminCreateType: (body) =>
        api('/api/admin/membership-types', { method: 'POST', body: JSON.stringify(body) }),
}

export const adminApi = {
    users: () => api('/api/admin/users'),
    createTrainer: (body) =>
        api('/api/admin/users/trainers', { method: 'POST', body: JSON.stringify(body) }),
}