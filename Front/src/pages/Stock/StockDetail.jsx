import React, { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import {
  Grid,
  Typography,
  Box,
  Chip,
  Button,
  CircularProgress,
  Alert,
  Card,
  CardContent,
  useTheme,
  useMediaQuery,
  IconButton,
  Tooltip,
} from '@mui/material'
import { 
  ArrowBack, 
  TrendingUp, 
  TrendingDown, 
  ShowChart,
  Timeline,
  Bookmark,
  BookmarkBorder,
} from '@mui/icons-material'
import { motion } from 'framer-motion'
import { api } from '../../services/apiClient'
import { useAuth } from '../../contexts/AuthContext'
import StockChart from './components/StockChart'
import TradingPanel from './components/TradingPanel'

const StockDetail = () => {
  const { stockId } = useParams()
  const navigate = useNavigate()
  const { user } = useAuth()
  const theme = useTheme()
  const isMobile = useMediaQuery(theme.breakpoints.down('md'))
  
  const [stockInfo, setStockInfo] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [isInterested, setIsInterested] = useState(false)
  const [interestLoading, setInterestLoading] = useState(false)

  useEffect(() => {
    if (stockId) {
      fetchStockData()
      if (user) {
        checkInterestStatus()
      }
    }
  }, [stockId, user])

  const fetchStockData = async () => {
    try {
      setLoading(true)
      setError(null)

      const response = await api.stocks.getById(stockId)
      
      if (response.data.success) {
        setStockInfo(response.data.data)
      } else {
        setError(response.data.message || '주식 정보를 찾을 수 없습니다.')
      }
    } catch (error) {
      console.error('주식 정보 로딩 실패:', error)
      setError('주식 정보를 불러오는데 실패했습니다.')
    } finally {
      setLoading(false)
    }
  }

  const formatPrice = (price) => {
    return price ? price.toLocaleString() + '원' : '-'
  }

  const formatChangeRate = (rate) => {
    if (!rate) return '0.00%'
    const numRate = parseFloat(rate.replace('%', ''))
    const sign = numRate >= 0 ? '+' : ''
    return `${sign}${rate}`
  }

  const getChangeColor = (rate) => {
    if (!rate) return 'text.secondary'
    const numRate = parseFloat(rate.replace('%', ''))
    if (numRate > 0) return '#d32f2f' // 빨간색 (상승)
    if (numRate < 0) return '#1976d2' // 파란색 (하락)
    return 'text.secondary'
  }

  const handleTradeSuccess = () => {
    window.location.reload()
  }

  // 관심목록 상태 확인
  const checkInterestStatus = async () => {
    try {
      const response = await api.interests.check(stockId)
      if (response.data.success) {
        setIsInterested(response.data.data)
      }
    } catch (error) {
      console.error('관심목록 상태 확인 실패:', error)
    }
  }

  // 관심목록 추가/삭제 토글
  const handleInterestToggle = async () => {
    if (!user) {
      navigate('/login')
      return
    }

    try {
      setInterestLoading(true)
      
      if (isInterested) {
        await api.interests.remove(stockId)
        setIsInterested(false)
      } else {
        await api.interests.add(stockId)
        setIsInterested(true)
      }
    } catch (error) {
      console.error('관심목록 처리 실패:', error)
    } finally {
      setInterestLoading(false)
    }
  }

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="60vh">
        <Box textAlign="center">
          <CircularProgress size={60} thickness={4} />
          <Typography variant="h6" sx={{ mt: 2, color: 'text.secondary' }}>
            주식 정보를 로딩중...
          </Typography>
        </Box>
      </Box>
    )
  }

  if (error || !stockInfo) {
    return (
      <Box textAlign="center" mt={4}>
        <Alert severity="error" sx={{ mb: 2, borderRadius: 2 }}>
          {error || '주식 정보를 찾을 수 없습니다.'}
        </Alert>
        <Button
          variant="outlined"
          startIcon={<ArrowBack />}
          onClick={() => navigate('/stocks')}
          sx={{ borderRadius: 2 }}
        >
          주식 목록으로 돌아가기
        </Button>
      </Box>
    )
  }

  return (
    <Box sx={{ maxWidth: '900px', mx: 'auto' }}>
      {/* 헤더 */}
      <motion.div
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.6 }}
      >
        <Box display="flex" alignItems="center" justifyContent="space-between" mb={2}>
          <Box display="flex" alignItems="center">
            <Button
              startIcon={<ArrowBack />}
              onClick={() => navigate('/stocks')}
              sx={{ 
                mr: 2,
                borderRadius: 2,
              }}
            >
              목록으로
            </Button>
          </Box>
          
          {user && (
            <Tooltip title={isInterested ? "관심목록에서 제거" : "관심목록에 추가"}>
              <IconButton
                onClick={handleInterestToggle}
                disabled={interestLoading}
                sx={{
                  backgroundColor: isInterested ? '#1976d2' : 'rgba(0,0,0,0.04)',
                  color: isInterested ? 'white' : 'text.secondary',
                  '&:hover': {
                    backgroundColor: isInterested ? '#1565c0' : 'rgba(0,0,0,0.08)',
                  },
                  transition: 'all 0.3s ease-in-out',
                }}
              >
                {interestLoading ? (
                  <CircularProgress size={20} color="inherit" />
                ) : isInterested ? (
                  <Bookmark />
                ) : (
                  <BookmarkBorder />
                )}
              </IconButton>
            </Tooltip>
          )}
        </Box>

        <Box>
          <Typography 
            variant={isMobile ? "h5" : "h4"} 
            component="h1"
            fontWeight="700"
            color="#2c3e50"
          >
            {stockInfo.stockName}
          </Typography>
          <Box display="flex" alignItems="center" gap={1} mt={0.5}>
            <Chip
              label={stockInfo.stockId}
              variant="outlined"
              size="small"
              sx={{ fontFamily: 'monospace' }}
            />
            <Chip
              label={stockInfo.marketCode}
              color={stockInfo.marketCode === 'KOSPI' ? 'primary' : 'secondary'}
              size="small"
            />
          </Box>
        </Box>
      </motion.div>

      <Grid container spacing={3}>
        {/* 왼쪽: 주가 정보 + 차트 */}
        <Grid item xs={12} lg={9}>
          {/* 주가 정보 */}
          <motion.div
            initial={{ opacity: 0, x: -20 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ duration: 0.6, delay: 0.1 }}
          >
            <Card 
              sx={{ 
                mb: 3,
                backgroundColor: '#fafafa',
                border: '1px solid #e0e0e0',
                borderRadius: 2,
                boxShadow: '0 2px 12px rgba(0,0,0,0.08)',
              }}
            >
              <CardContent sx={{ p: 3 }}>
                <Box display="flex" alignItems="center" justifyContent="space-between" mb={2}>
                  <Typography variant="subtitle1" color="text.secondary">
                    현재가
                  </Typography>
                  <Box display="flex" alignItems="center">
                    {stockInfo.changeRate && parseFloat(stockInfo.changeRate.replace('%', '')) > 0 ? (
                      <TrendingUp sx={{ color: '#d32f2f', fontSize: 24 }} />
                    ) : (
                      <TrendingDown sx={{ color: '#1976d2', fontSize: 24 }} />
                    )}
                  </Box>
                </Box>
                
                <Box display="flex" alignItems="baseline" mb={2} flexWrap="wrap" gap={2}>
                  <Typography 
                    variant={isMobile ? "h5" : "h4"} 
                    component="div" 
                    fontWeight="bold"
                    color="#2c3e50"
                  >
                    {formatPrice(stockInfo.currentPrice)}
                  </Typography>
                  {stockInfo.priceChange && (
                    <Typography 
                      variant="h6" 
                      component="div"
                      sx={{ 
                        color: getChangeColor(stockInfo.changeRate),
                        fontWeight: 'bold',
                      }}
                    >
                      {stockInfo.priceChange.startsWith('-') ? stockInfo.priceChange : `+${stockInfo.priceChange}`}원
                    </Typography>
                  )}
                </Box>
                
                <Chip
                  label={formatChangeRate(stockInfo.changeRate)}
                  color={stockInfo.changeRate?.startsWith('-') ? 'primary' : 'error'}
                  sx={{ 
                    fontWeight: 'bold',
                    fontSize: '0.9rem',
                  }}
                />
              </CardContent>
            </Card>
          </motion.div>

          {/* 차트 */}
          <motion.div
            initial={{ opacity: 0, x: -20 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ duration: 0.6, delay: 0.2 }}
          >
            <Card 
              sx={{ 
                borderRadius: 2,
                boxShadow: '0 2px 12px rgba(0,0,0,0.08)',
                height: '450px',
              }}
            >
              <CardContent sx={{ p: 3, height: '100%' }}>
                <Box display="flex" alignItems="center" mb={2}>
                  <Timeline sx={{ mr: 1, color: '#1976d2', fontSize: 24 }} />
                  <Typography variant="h6" fontWeight="600" color="#2c3e50">
                    📈 주가 차트
                  </Typography>
                </Box>
                <Box sx={{ height: 'calc(100% - 50px)' }}>
                  <StockChart 
                    stockId={stockId} 
                    stockName={stockInfo.stockName}
                    currentPrice={stockInfo.currentPrice}
                  />
                </Box>
              </CardContent>
            </Card>
          </motion.div>
        </Grid>

        {/* 오른쪽: 거래 패널 */}
        <Grid item xs={12} lg={3}>
          <motion.div
            initial={{ opacity: 0, x: 20 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ duration: 0.6, delay: 0.3 }}
          >
            {user ? (
              <TradingPanel 
                stockInfo={stockInfo}
                onTradeSuccess={handleTradeSuccess}
              />
            ) : (
              <Card 
                sx={{ 
                  backgroundColor: '#e3f2fd',
                  border: '1px solid #bbdefb',
                  borderRadius: 2,
                  boxShadow: '0 2px 12px rgba(0,0,0,0.08)',
                  height: '400px',
                }}
              >
                <CardContent sx={{ p: 3, textAlign: 'center', height: '100%', display: 'flex', flexDirection: 'column', justifyContent: 'center' }}>
                  <ShowChart sx={{ fontSize: 48, mb: 2, color: '#1976d2' }} />
                  <Typography variant="h6" fontWeight="600" gutterBottom color="#2c3e50">
                    🔐 주식 거래
                  </Typography>
                  <Typography sx={{ mb: 3, color: 'text.secondary' }}>
                    주식 거래를 하려면 로그인이 필요합니다.
                  </Typography>
                  <Button
                    variant="contained"
                    onClick={() => navigate('/login')}
                    fullWidth
                    sx={{
                      py: 1.5,
                      backgroundColor: '#1976d2',
                      fontWeight: 'bold',
                      borderRadius: 2,
                      '&:hover': {
                        backgroundColor: '#1565c0',
                      },
                    }}
                  >
                    로그인하기
                  </Button>
                </CardContent>
              </Card>
            )}
          </motion.div>
        </Grid>

        {/* 하단 정보 */}
        <Grid item xs={12}>
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6, delay: 0.4 }}
          >
          </motion.div>
        </Grid>
      </Grid>
    </Box>
  )
}

export default StockDetail