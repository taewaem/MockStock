import React, { createContext, useContext, useState, useEffect } from 'react'
import { api } from '../services/apiClient'

const AuthContext = createContext()

export const useAuth = () => {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider')
  }
  return context
}

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null)
  const [loading, setLoading] = useState(true)
  const [initialized, setInitialized] = useState(false)

  useEffect(() => {
    if (!initialized) {
      checkAuthStatus()
      setInitialized(true)
    }
  }, [initialized])

  const checkAuthStatus = async () => {
    try {
      setLoading(true)
      const response = await api.auth.me()
      
      if (response.data.success) {
        setUser(response.data.data)
        console.log('✅ 로그인 상태:', response.data.data.userName)
      }
    } catch (error) {
      console.log('🔓 비로그인 상태')
      setUser(null)
    } finally {
      setLoading(false)
    }
  }

  const login = async (loginData) => {
    try {
      const response = await api.auth.login(loginData)
      
      if (response.data.success) {
        setUser(response.data.data)
        return { success: true, data: response.data.data }
      } else {
        return { success: false, error: response.data.message }
      }
    } catch (error) {
      const errorMessage = error.response?.data?.message || '로그인에 실패했습니다.'
      return { success: false, error: errorMessage }
    }
  }

  const logout = async () => {
    try {
      await api.auth.logout()
    } catch (error) {
      console.error('로그아웃 오류:', error)
    } finally {
      setUser(null)
    }
  }

  const register = async (registerData) => {
    try {
      const response = await api.auth.register(registerData)
      
      if (response.data.success) {
        return { success: true, data: response.data.data }
      } else {
        return { success: false, error: response.data.message }
      }
    } catch (error) {
      const errorMessage = error.response?.data?.message || '회원가입에 실패했습니다.'
      return { success: false, error: errorMessage }
    }
  }

  const value = {
    user,
    loading,
    login,
    logout,
    register,
    checkAuthStatus,
    isAuthenticated: !!user,
  }

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  )
}