import { createContext, useContext, useEffect, useMemo, useState } from 'react'
import { authApi, clearToken, getToken, setToken, userApi } from '../api/client'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
    const [user, setUser] = useState(null)
    const [loading, setLoading] = useState(true)

    async function refreshMe() {
        if (!getToken()) {
            setUser(null)
            setLoading(false)
            return
        }
        try {
            const me = await userApi.me()
            setUser(me)
        } catch {
            clearToken()
            setUser(null)
        } finally {
            setLoading(false)
        }
    }

    useEffect(() => {
        refreshMe()
    }, [])

    async function login(email, password) {
        const res = await authApi.login({ email, password })
        setToken(res.token)
        await refreshMe()
        return res
    }

    async function register(payload) {
        const res = await authApi.register(payload)
        setToken(res.token)
        await refreshMe()
        return res
    }

    function logout() {
        clearToken()
        setUser(null)
    }

    const value = useMemo(
        () => ({ user, loading, login, register, logout, refreshMe }),
        [user, loading]
    )

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
    return useContext(AuthContext)
}