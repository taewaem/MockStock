import React, { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import {
  Grid,
  Typography,
  Box,
  Button,
  CircularProgress,
  Alert,
  Card,
  CardContent,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Chip,
  useTheme,
  useMediaQuery,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
} from '@mui/material'
import {
  AccountBalance,
  TrendingUp,
  ShowChart,
  AccountBalanceWallet,
  Refresh,
  Launch,
  Warning,
} from '@mui/icons-material'
import { motion } from 'framer-motion'
import { useAuth } from '../../contexts/AuthContext'
import { api } from '../../services/apiClient'
import PortfolioChart from './components/PortfolioChart'

const Portfolio = () => {
  const navigate = useNavigate()
  const { user } = useAuth()
  const theme = useTheme()
  const isMobile = useMediaQuery(theme.breakpoints.down('md'))
  
  const [portfolio, setPortfolio] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [bankruptcyDialog, setBankruptcyDialog] = useState(false)
  const [bankruptcyLoading, setBankruptcyLoading] = useState(false)

  useEffect(() => {
    if (user) {
      fetchPortfolio()
    } else {
      navigate('/login')
    }
  }, [user, navigate])

  const fetchPortfolio = async () => {
    try {
      setLoading(true)
      setError(null)
      const response = await api.portfolio.get()
      if (response.data.success) {
        setPortfolio(response.data.data)
      } else {
        setError(response.data.message || '포트폴리오를 불러오는데 실패했습니다.')
      }
    } catch (error) {
      console.error('포트폴리오 조회 실패:', error)
      setError('포트폴리오를 불러오는데 실패했습니다.')
    } finally {
      setLoading(false)
    }
  }

  const formatMoney = (money) => {
    return money ? money.toLocaleString() + '원' : '0원'
  }

  const formatPercent = (percent) => {
    if (percent === null || percent === undefined) return '0.00%'
    const sign = percent >= 0 ? '+' : ''
    return `${sign}${percent.toFixed(2)}%`
  }

  const getReturnColor = (returnRate) => {
    if (returnRate > 0) return '#d32f2f' // 빨간색 (수익)
    if (returnRate < 0) return '#1976d2' // 파란색 (손실)
    return '#757575' // 회색 (보합)
  }

  const handleStockClick = (stockId) => {
    navigate(`/stocks/${stockId}`)
  }

  // 파산 신청 처리
  const handleBankruptcy = async () => {
    try {
      setBankruptcyLoading(true)
      const response = await api.users.declareBankruptcy()
      
      if (response.data.success) {
        alert('파산 처리가 완료되었습니다! 초기 자금 5천만원으로 새로운 시작을 하세요! 🚀')
        setBankruptcyDialog(false)
        // 사용자 정보와 포트폴리오 새로고침
        await fetchPortfolio()
        window.location.reload() // 전체 페이지 새로고침으로 사용자 정보 업데이트
      } else {
        alert('파산 처리 중 오류가 발생했습니다: ' + response.data.message)
      }
    } catch (error) {
      console.error('파산 처리 실패:', error)
      alert('파산 처리 중 오류가 발생했습니다.')
    } finally {
      setBankruptcyLoading(false)
    }
  }

  // 파산 가능 여부 확인 - 항상 가능하게 변경
  const canDeclareBankruptcy = () => {
    return true // 조건 없이 항상 파산 가능
  }

  if (!user) {
    return (
      <Box textAlign="center" mt={4}>
        <Alert severity="warning">
          포트폴리오를 보려면 로그인이 필요합니다.
        </Alert>
        <Button 
          variant="contained" 
          onClick={() => navigate('/login')}
          sx={{ mt: 2 }}
        >
          로그인하기
        </Button>
      </Box>
    )
  }

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="60vh">
        <Box textAlign="center">
          <CircularProgress size={60} />
          <Typography variant="h6" sx={{ mt: 2, color: 'text.secondary' }}>
            포트폴리오를 로딩중...
          </Typography>
        </Box>
      </Box>
    )
  }

  if (error) {
    return (
      <Box textAlign="center" mt={4}>
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
        <Button
          variant="outlined"
          startIcon={<Refresh />}
          onClick={fetchPortfolio}
        >
          다시 시도
        </Button>
      </Box>
    )
  }

  return (
    <Box sx={{ maxWidth: '1400px', mx: 'auto' }}>
      {/* 상단 타이틀 - 여백 최소화 */}
      <motion.div
        initial={{ opacity: 0, y: -10 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.4 }}
      >
        <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
          <Typography 
            variant={isMobile ? "h5" : "h4"} 
            fontWeight="700" 
            color="#2c3e50"
          >
          {user.userName}님의 포트폴리오
          </Typography>
          <Box display="flex" gap={1}>
            {/* 파산 신청 버튼 - 조건부 표시 */}
            {canDeclareBankruptcy() && (
              <Button
                variant="outlined"
                color="error"
                startIcon={<Warning />}
                onClick={() => setBankruptcyDialog(true)}
                size="small"
                sx={{ 
                  borderRadius: 2,
                  borderColor: '#d32f2f',
                  color: '#d32f2f',
                  '&:hover': {
                    backgroundColor: '#d32f2f',
                    color: 'white',
                  }
                }}
              >
                파산신청
              </Button>
            )}
              </Box>
        </Box>
      </motion.div>

      {/* 자산현황 4개 카드 - 1줄로 배치, 여백 최소화 */}
      <motion.div
        initial={{ opacity: 0, y: 10 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.4, delay: 0.1 }}
      >
        <Grid container spacing={1.5} sx={{ mb: 2 }}>
          {/* 보유현금 */}
          <Grid item xs={6} md={3}>
            <Card 
              sx={{ 
                backgroundColor: '#e3f2fd',
                border: '1px solid #bbdefb',
                borderRadius: 2,
                height: '100px', // 고정 높이로 통일
              }}
            >
              <CardContent sx={{ p: 2, textAlign: 'center', height: '100%', display: 'flex', flexDirection: 'column', justifyContent: 'center' }}>
                <AccountBalanceWallet sx={{ fontSize: 24, color: '#1976d2', mb: 0.5 }} />
                <Typography variant="caption" color="text.secondary">
                  보유현금
                </Typography>
                <Typography variant="h6" fontWeight="bold" color="#2c3e50" sx={{ fontSize: isMobile ? '1rem' : '1.25rem' }}>
                  {formatMoney(portfolio?.availableCash)}
                </Typography>
              </CardContent>
            </Card>
          </Grid>

          {/* 주식평가금액 */}
          <Grid item xs={6} md={3}>
            <Card 
              sx={{ 
                backgroundColor: '#fff3e0',
                border: '1px solid #ffcc02',
                borderRadius: 2,
                height: '100px',
              }}
            >
              <CardContent sx={{ p: 2, textAlign: 'center', height: '100%', display: 'flex', flexDirection: 'column', justifyContent: 'center' }}>
                <ShowChart sx={{ fontSize: 24, color: '#ff9800', mb: 0.5 }} />
                <Typography variant="caption" color="text.secondary">
                  주식평가금액
                </Typography>
                <Typography variant="h6" fontWeight="bold" color="#2c3e50" sx={{ fontSize: isMobile ? '1rem' : '1.25rem' }}>
                  {formatMoney(portfolio?.stockValue)}
                </Typography>
              </CardContent>
            </Card>
          </Grid>

          {/* 총자산 */}
          <Grid item xs={6} md={3}>
            <Card 
              sx={{ 
                backgroundColor: '#e8f5e8',
                border: '1px solid #c8e6c9',
                borderRadius: 2,
                height: '100px',
              }}
            >
              <CardContent sx={{ p: 2, textAlign: 'center', height: '100%', display: 'flex', flexDirection: 'column', justifyContent: 'center' }}>
                <AccountBalance sx={{ fontSize: 24, color: '#4caf50', mb: 0.5 }} />
                <Typography variant="caption" color="text.secondary">
                  총자산
                </Typography>
                <Typography variant="h6" fontWeight="bold" color="#2c3e50" sx={{ fontSize: isMobile ? '1rem' : '1.25rem' }}>
                  {formatMoney(portfolio?.totalAssets)}
                </Typography>
              </CardContent>
            </Card>
          </Grid>

          {/* 총 수익률 */}
          <Grid item xs={6} md={3}>
            <Card 
              sx={{ 
                backgroundColor: portfolio?.totalReturnRate >= 0 ? '#ffebee' : '#e3f2fd',
                border: portfolio?.totalReturnRate >= 0 ? '1px solid #ffcdd2' : '1px solid #bbdefb',
                borderRadius: 2,
                height: '100px',
              }}
            >
              <CardContent sx={{ p: 2, textAlign: 'center', height: '100%', display: 'flex', flexDirection: 'column', justifyContent: 'center' }}>
                <TrendingUp sx={{ fontSize: 24, color: getReturnColor(portfolio?.totalReturnRate), mb: 0.5 }} />
                <Typography variant="caption" color="text.secondary">
                  총 수익률
                </Typography>
                <Typography 
                  variant="h6" 
                  fontWeight="bold"
                  sx={{ 
                    color: getReturnColor(portfolio?.totalReturnRate),
                    fontSize: isMobile ? '1rem' : '1.25rem'
                  }}
                >
                  {formatPercent(portfolio?.totalReturnRate)}
                </Typography>
              </CardContent>
            </Card>
          </Grid>
        </Grid>
      </motion.div>

      {/* 하단 2분할: 포트폴리오 차트 + 보유주식 목록 - 여백 없이 딱 붙게 */}
      <motion.div
        initial={{ opacity: 0, y: 10 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.4, delay: 0.2 }}
      >
        <Grid container spacing={1.5}>
          {/* 왼쪽: 포트폴리오 차트 */}
          <Grid item xs={12} md={5}>
            <Card 
              sx={{ 
                height: '420px', // 고정 높이
                borderRadius: 2,
                boxShadow: '0 2px 12px rgba(0,0,0,0.08)',
              }}
            >
              <CardContent sx={{ p: 2, height: '100%' }}>
                <Typography variant="h6" fontWeight="600" gutterBottom color="#2c3e50">
                  📈 포트폴리오 구성
                </Typography>
                <Box sx={{ height: 'calc(100% - 40px)' }}>
                  {portfolio?.holdings && portfolio.holdings.length > 0 ? (
                    <PortfolioChart holdings={portfolio.holdings} />
                  ) : (
                    <Box display="flex" alignItems="center" justifyContent="center" height="100%">
                      <Typography color="text.secondary">
                        보유한 주식이 없습니다
                      </Typography>
                    </Box>
                  )}
                </Box>
              </CardContent>
            </Card>
          </Grid>

          {/* 오른쪽: 보유주식 목록 */}
          <Grid item xs={12} md={7}>
            <Card 
              sx={{ 
                height: '420px', // 차트와 같은 높이
                borderRadius: 2,
                boxShadow: '0 2px 12px rgba(0,0,0,0.08)',
              }}
            >
              <CardContent sx={{ p: 2, height: '100%' }}>
                <Typography variant="h6" fontWeight="600" gutterBottom color="#2c3e50">
                  📋 보유 주식 ({portfolio?.holdings?.length || 0}개)
                </Typography>
                
                {!portfolio?.holdings || portfolio.holdings.length === 0 ? (
                  <Box 
                    display="flex" 
                    flexDirection="column" 
                    alignItems="center" 
                    justifyContent="center" 
                    height="calc(100% - 40px)"
                  >
                    <ShowChart sx={{ fontSize: 48, color: 'text.disabled', mb: 2 }} />
                    <Typography color="text.secondary" gutterBottom>
                      보유한 주식이 없습니다.
                    </Typography>
                    <Button
                      variant="contained"
                      onClick={() => navigate('/stocks')}
                      size="small"
                      sx={{ borderRadius: 2 }}
                    >
                      주식 둘러보기
                    </Button>
                  </Box>
                ) : (
                  <Box sx={{ height: 'calc(100% - 40px)', overflow: 'auto' }}>
                    <TableContainer>
                      <Table size="small">
                        <TableHead>
                          <TableRow>
                            <TableCell sx={{ fontWeight: 'bold', py: 1 }}>종목명</TableCell>
                            <TableCell align="right" sx={{ fontWeight: 'bold', py: 1 }}>수량</TableCell>
                            <TableCell align="right" sx={{ fontWeight: 'bold', py: 1 }}>평균가</TableCell>
                            <TableCell align="right" sx={{ fontWeight: 'bold', py: 1 }}>현재가</TableCell>
                            <TableCell align="right" sx={{ fontWeight: 'bold', py: 1 }}>평가손익</TableCell>
                            <TableCell align="center" sx={{ fontWeight: 'bold', py: 1 }}>거래</TableCell>
                          </TableRow>
                        </TableHead>
                        <TableBody>
                          {portfolio.holdings.map((holding) => (
                            <TableRow 
                              key={holding.stockId}
                              hover
                              sx={{ 
                                cursor: 'pointer',
                                '&:hover': {
                                  backgroundColor: 'action.hover',
                                },
                              }}
                              onClick={() => handleStockClick(holding.stockId)}
                            >
                              <TableCell sx={{ py: 1 }}>
                                <Box>
                                  <Typography variant="body2" fontWeight="medium">
                                    {holding.stockName}
                                  </Typography>
                                  <Typography variant="caption" color="text.secondary">
                                    {holding.stockId}
                                  </Typography>
                                </Box>
                              </TableCell>
                              <TableCell align="right" sx={{ py: 1 }}>
                                <Typography variant="body2">
                                  {holding.quantity?.toLocaleString()}주
                                </Typography>
                              </TableCell>
                              <TableCell align="right" sx={{ py: 1 }}>
                                <Typography variant="body2">
                                  {formatMoney(holding.averagePrice)}
                                </Typography>
                              </TableCell>
                              <TableCell align="right" sx={{ py: 1 }}>
                                <Typography variant="body2">
                                  {formatMoney(holding.currentPrice)}
                                </Typography>
                              </TableCell>
                              <TableCell align="right" sx={{ py: 1 }}>
                                <Box display="flex" flexDirection="column" alignItems="flex-end">
                                  <Typography 
                                    variant="body2"
                                    fontWeight="bold"
                                    sx={{ color: getReturnColor(holding.profitLoss) }}
                                  >
                                    {holding.profitLoss >= 0 ? '+' : ''}{formatMoney(holding.profitLoss)}
                                  </Typography>
                                  <Chip
                                    label={formatPercent(holding.returnRate)}
                                    color={holding.returnRate >= 0 ? 'error' : 'primary'}
                                    size="small"
                                    variant="outlined"
                                    sx={{ 
                                      fontSize: '0.7rem',
                                      height: '18px',
                                    }}
                                  />
                                </Box>
                              </TableCell>
                              <TableCell align="center" sx={{ py: 1 }}>
                                <Button
                                  size="small"
                                  variant="outlined"
                                  startIcon={<Launch />}
                                  onClick={(e) => {
                                    e.stopPropagation()
                                    handleStockClick(holding.stockId)
                                  }}
                                  sx={{ 
                                    fontSize: '0.75rem',
                                    px: 1,
                                    py: 0.5,
                                  }}
                                >
                                  거래
                                </Button>
                              </TableCell>
                            </TableRow>
                          ))}
                        </TableBody>
                      </Table>
                    </TableContainer>
                  </Box>
                )}
              </CardContent>
            </Card>
          </Grid>
        </Grid>
      </motion.div>

      {/* 파산 신청 확인 다이얼로그 */}
      <Dialog
        open={bankruptcyDialog}
        onClose={() => setBankruptcyDialog(false)}
        maxWidth="sm"
        fullWidth
        PaperProps={{ sx: { borderRadius: 3 } }}
      >
        <DialogTitle sx={{ textAlign: 'center', pb: 1 }}>
          <Warning sx={{ fontSize: 48, color: '#d32f2f', mb: 1 }} />
          <Typography variant="h6" fontWeight="bold" color="#d32f2f">
            파산 신청 확인
          </Typography>
        </DialogTitle>
        <DialogContent>
          <DialogContentText component="div" sx={{ textAlign: 'center' }}>
            <Typography gutterBottom sx={{ mb: 2 }}>
              정말로 파산을 신청하시겠습니까?
            </Typography>
            
            <Box sx={{ p: 2, backgroundColor: '#fff3e0', borderRadius: 2, mb: 2 }}>
              <Typography variant="subtitle2" fontWeight="bold" gutterBottom>
                📋 파산 처리 내용:
              </Typography>
              <Typography variant="body2" sx={{ mb: 1 }}>
                • 보유한 모든 주식이 매도됩니다
              </Typography>
              <Typography variant="body2" sx={{ mb: 1 }}>
                • 초기 자금 5천만원이 지급됩니다
              </Typography>
              <Typography variant="body2">
                • 새로운 시작을 할 수 있습니다
              </Typography>
            </Box>

            <Alert severity="warning" sx={{ textAlign: 'left' }}>
              이 작업은 되돌릴 수 없습니다. 신중하게 결정해주세요.
            </Alert>
          </DialogContentText>
        </DialogContent>
        <DialogActions sx={{ p: 3, pt: 1 }}>
          <Button
            onClick={() => setBankruptcyDialog(false)}
            disabled={bankruptcyLoading}
            sx={{ borderRadius: 2 }}
          >
            취소
          </Button>
          <Button
            onClick={handleBankruptcy}
            color="error"
            variant="contained"
            disabled={bankruptcyLoading}
            startIcon={bankruptcyLoading ? <CircularProgress size={16} /> : <Warning />}
            sx={{ borderRadius: 2 }}
          >
            {bankruptcyLoading ? '처리 중...' : '파산 신청'}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  )
}

export default Portfolio