import React, { useState, useEffect } from 'react'
import {
  Grid,
  Typography,
  Box,
  CircularProgress,
  Alert,
  Card,
  CardContent,
  useTheme,
  useMediaQuery,
} from '@mui/material'
import {
  TrendingUp,
  People,
  ShowChart,
  AccountBalanceWallet,
  BookmarkBorder,
  LocalFireDepartment,
} from '@mui/icons-material'
import { motion } from 'framer-motion'
import { useAuth } from '../../contexts/AuthContext'
import { api } from '../../services/apiClient'
import UserRankings from './components/UserRankings'
import RecommendedStocks from './components/RecommendedStocks'
import UserSummary from './components/UserSummary'
import VolumeRankings from './components/VolumeRankings'
import InterestList from './components/InterestList'

const Dashboard = () => {
  const { user } = useAuth()
  const theme = useTheme()
  const isMobile = useMediaQuery(theme.breakpoints.down('md'))
  
  const [dashboardData, setDashboardData] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    fetchDashboardData()
  }, [])

  const fetchDashboardData = async () => {
    try {
      setLoading(true)
      const response = await api.dashboard.get()
      
      if (response.data.success) {
        setDashboardData(response.data.data)
      } else {
        setError(response.data.message)
      }
    } catch (error) {
      setError('대시보드 데이터를 불러오는데 실패했습니다.')
      console.error('대시보드 오류:', error)
    } finally {
      setLoading(false)
    }
  }

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="60vh">
        <Box textAlign="center">
          <CircularProgress size={60} thickness={4} />
          <Typography variant="h6" sx={{ mt: 2, color: 'text.secondary' }}>
            대시보드를 로딩중...
          </Typography>
        </Box>
      </Box>
    )
  }

  if (error) {
    return (
      <Alert severity="error" sx={{ borderRadius: 2 }}>
        {error}
      </Alert>
    )
  }

  return (
    <Box sx={{ maxWidth: '1400px', mx: 'auto' }}>
      {/* 헤더 */}
      <motion.div
        initial={{ opacity: 0, y: -10 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.4 }}
      >
      </motion.div>

      {/* 가로 1줄 5개 레이아웃 */}
      <Grid container spacing={3}>
        {/* 1. 관심목록 */}
        <Grid item xs={12} lg={2.4}>
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6, delay: 0.1 }}
          >
            <Card 
              sx={{ 
                height: '450px',
                borderRadius: 3,
                boxShadow: '0 2px 12px rgba(0,0,0,0.08)',
                border: '1px solid #e3f2fd',
                backgroundColor: '#fafafa',
              }}
            >
              <CardContent sx={{ p: 3, height: '100%' }}>
                <Box display="flex" alignItems="center" mb={2}>
                  <BookmarkBorder sx={{ mr: 1, fontSize: 20, color: '#1976d2' }} />
                  <Typography variant="h6" fontWeight="600" color="#2c3e50">
                    관심목록
                  </Typography>
                </Box>
                <Box sx={{ height: 'calc(100% - 50px)', overflow: 'auto' }}>
                  <InterestList />
                </Box>
              </CardContent>
            </Card>
          </motion.div>
        </Grid>

        {/* 2. 내 자산현황 */}
        <Grid item xs={12} lg={2.4}>
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6, delay: 0.2 }}
          >
            {user && dashboardData?.currentUser ? (
              <UserSummary user={dashboardData.currentUser} />
            ) : (
              <Card 
                sx={{ 
                  height: '450px',
                  borderRadius: 3,
                  boxShadow: '0 2px 12px rgba(0,0,0,0.08)',
                  border: '1px solid #e8f5e8',
                  backgroundColor: '#f1f8e9',
                }}
              >
                <CardContent sx={{ p: 3, textAlign: 'center', height: '100%', display: 'flex', flexDirection: 'column', justifyContent: 'center' }}>
                  <AccountBalanceWallet sx={{ fontSize: 48, mb: 2, color: '#4caf50' }} />
                  <Typography variant="h6" fontWeight="600" gutterBottom color="#2c3e50">
                    내 자산현황
                  </Typography>
                  <Typography sx={{ mb: 2, color: 'text.secondary', fontSize: '0.9rem' }}>
                    로그인하시면 실시간으로<br/>
                    자산 현황을 확인할 수 있습니다
                  </Typography>
                  <Typography variant="body2" sx={{ color: '#4caf50', fontWeight: 'bold' }}>
                    💰 시드머니 5천만원 지급!
                  </Typography>
                </CardContent>
              </Card>
            )}
          </motion.div>
        </Grid>

        {/* 3. 거래량순위 */}
        <Grid item xs={12} lg={2.4}>
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6, delay: 0.3 }}
          >
            <Card 
              sx={{ 
                height: '450px',
                borderRadius: 3,
                boxShadow: '0 2px 12px rgba(0,0,0,0.08)',
                border: '1px solid #fff3e0',
                backgroundColor: '#fafafa',
              }}
            >
              <CardContent sx={{ p: 3, height: '100%' }}>
                <Box display="flex" alignItems="center" mb={2}>
                  <LocalFireDepartment sx={{ mr: 1, fontSize: 20, color: '#ff9800' }} />
                  <Typography variant="h6" fontWeight="600" color="#2c3e50">
                    거래량순위
                  </Typography>
                </Box>
                <Box sx={{ height: 'calc(100% - 50px)', overflow: 'auto' }}>
                  <VolumeRankings rankings={dashboardData?.volumeRankings || []} />
                </Box>
              </CardContent>
            </Card>
          </motion.div>
        </Grid>

        {/* 4. 주식 추천 */}
        <Grid item xs={12} lg={2.4}>
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6, delay: 0.4 }}
          >
            <Card 
              sx={{ 
                height: '450px',
                borderRadius: 3,
                boxShadow: '0 2px 12px rgba(0,0,0,0.08)',
                border: '1px solid #f3e5f5',
                backgroundColor: '#fafafa',
              }}
            >
              <CardContent sx={{ p: 3, height: '100%' }}>
                <Box display="flex" alignItems="center" mb={2}>
                  <ShowChart sx={{ mr: 1, fontSize: 20, color: '#9c27b0' }} />
                  <Typography variant="h6" fontWeight="600" color="#2c3e50">
                    추천 주식
                  </Typography>
                </Box>
                <Box sx={{ height: 'calc(100% - 50px)', overflow: 'auto' }}>
                  <RecommendedStocks stocks={dashboardData?.recommendedStocks?.slice(0, 6) || []} />
                </Box>
              </CardContent>
            </Card>
          </motion.div>
        </Grid>

        {/* 5. 유저 랭킹 */}
        <Grid item xs={12} lg={2.4}>
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6, delay: 0.5 }}
          >
            <Card 
              sx={{ 
                height: '450px',
                borderRadius: 3,
                boxShadow: '0 2px 12px rgba(0,0,0,0.08)',
                border: '1px solid #e1f5fe',
                backgroundColor: '#f1f8ff',
              }}
            >
              <CardContent sx={{ p: 3, height: '100%' }}>
                <Box display="flex" alignItems="center" mb={2}>
                  <People sx={{ mr: 1, fontSize: 20, color: '#2196f3' }} />
                  <Typography variant="h6" fontWeight="600" color="#2c3e50">
                    사용자 랭킹
                  </Typography>
                </Box>
                <Box sx={{ height: 'calc(100% - 50px)', overflow: 'auto' }}>
                  <UserRankings rankings={dashboardData?.topUsers || []} />
                </Box>
              </CardContent>
            </Card>
          </motion.div>
        </Grid>
      </Grid>

      {/* 하단 간단한 정보 */}
      <Box sx={{ mt: 4, textAlign: 'center' }}>
        <Typography variant="body2" color="text.secondary">
        </Typography>
      </Box>
    </Box>
  )
}

export default Dashboard