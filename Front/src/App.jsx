import React from 'react'
import { Routes, Route } from 'react-router-dom'
import { Box } from '@mui/material'
import { AuthProvider } from './contexts/AuthContext'
import Layout from './components/Layout/Layout'
import Dashboard from './pages/Dashboard/Dashboard'
import StockList from './pages/Stock/StockList'
import StockDetail from './pages/Stock/StockDetail'
import Portfolio from './pages/Portfolio/Portfolio'
import Login from './pages/Auth/Login'
import Register from './pages/Auth/Register'


function App() {
  return (
    <AuthProvider>
      <Box className="fade-in" sx={{ minHeight: '100vh' }}>
        <Layout>
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/stocks" element={<StockList />} />
            <Route path="/stocks/:stockId" element={<StockDetail />} />
            <Route path="/portfolio" element={<Portfolio />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
          </Routes>
        </Layout>
      </Box>
    </AuthProvider>
  )
}

export default App