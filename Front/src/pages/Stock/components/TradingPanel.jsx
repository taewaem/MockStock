import React, { useState, useEffect } from 'react'
import {
  Typography,
  Box,
  Button,
  TextField,
  Tabs,
  Tab,
  Alert,
  Divider,
  InputAdornment,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
  CircularProgress,
  Card,
  CardContent,
} from '@mui/material'
import {
  ShoppingCart,
  Sell,
  AccountBalanceWallet,
  TrendingUp,
  TrendingDown,
} from '@mui/icons-material'
import { motion } from 'framer-motion'
import { useAuth } from '../../../contexts/AuthContext'
import { api } from '../../../services/apiClient'

const TradingPanel = ({ stockInfo, onTradeSuccess }) => {
  const { user, checkAuthStatus } = useAuth()
  const [tabValue, setTabValue] = useState(0) // 0: 매수, 1: 매도
  const [quantity, setQuantity] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [confirmDialog, setConfirmDialog] = useState({ open: false, type: '', data: null })
  const [portfolio, setPortfolio] = useState(null)

  useEffect(() => {
    if (user) {
      fetchPortfolio()
    }
  }, [user])

  const fetchPortfolio = async () => {
    try {
      const response = await api.portfolio.get()
      if (response.data.success) {
        setPortfolio(response.data.data)
      }
    } catch (error) {
      console.error('포트폴리오 조회 실패:', error)
    }
  }

  const handleTabChange = (event, newValue) => {
    setTabValue(newValue)
    setQuantity('')
    setError('')
    setSuccess('')
  }

  const formatPrice = (price) => {
    return price ? price.toLocaleString() + '원' : '0원'
  }

  const calculateTotalAmount = () => {
    const qty = parseInt(quantity) || 0
    const price = stockInfo.currentPrice || 0
    return qty * price
  }

  const getMaxBuyQuantity = () => {
    if (!user || !stockInfo.currentPrice) return 0
    return Math.floor(user.availableMoney / stockInfo.currentPrice)
  }

  const getHoldingQuantity = () => {
    if (!portfolio || !portfolio.holdings) return 0
    const holding = portfolio.holdings.find(h => h.stockId === stockInfo.stockId)
    return holding ? holding.quantity : 0
  }

  const handleMaxClick = () => {
    if (tabValue === 0) {
      setQuantity(getMaxBuyQuantity().toString())
    } else {
      setQuantity(getHoldingQuantity().toString())
    }
  }

  const validateTrade = () => {
    const qty = parseInt(quantity)
    
    if (!qty || qty <= 0) {
      setError('수량을 정확히 입력해주세요.')
      return false
    }

    if (tabValue === 0) {
      const totalAmount = calculateTotalAmount()
      if (totalAmount > user.availableMoney) {
        setError('보유 현금이 부족합니다.')
        return false
      }
    } else {
      const holdingQty = getHoldingQuantity()
      if (qty > holdingQty) {
        setError('보유 수량이 부족합니다.')
        return false
      }
    }

    return true
  }

  const handleTradeClick = () => {
    if (!validateTrade()) return

    const tradeData = {
      type: tabValue === 0 ? 'buy' : 'sell',
      stockId: stockInfo.stockId,
      stockName: stockInfo.stockName,
      quantity: parseInt(quantity),
      price: stockInfo.currentPrice,
      totalAmount: calculateTotalAmount()
    }

    setConfirmDialog({
      open: true,
      type: tabValue === 0 ? 'buy' : 'sell',
      data: tradeData
    })
  }

  const executeTrade = async () => {
    try {
      setLoading(true)
      setError('')

      const { type, stockId, quantity: qty } = confirmDialog.data
      const tradeFunction = type === 'buy' ? api.stocks.buy : api.stocks.sell

      await tradeFunction({
        stockId,
        quantity: qty
      })

      setSuccess(`${type === 'buy' ? '매수' : '매도'}가 완료되었습니다!`)
      setQuantity('')
      setConfirmDialog({ open: false, type: '', data: null })
      
      await checkAuthStatus()
      await fetchPortfolio()
      
      if (onTradeSuccess) {
        onTradeSuccess()
      }

    } catch (error) {
      console.error('거래 실패:', error)
      setError(error.response?.data?.message || '거래 중 오류가 발생했습니다.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <>
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.6 }}
      >
        <Card 
          sx={{ 
            borderRadius: 2,
            boxShadow: '0 4px 20px rgba(0,0,0,0.08)',
            height: 'fit-content',
            position: 'sticky',
            top: 20,
          }}
        >
          <CardContent sx={{ p: 2 }}>
            <Typography variant="h6" fontWeight="600" gutterBottom>
              💰 주식 거래
            </Typography>

            {/* 현재 보유현금 - 더 컴팩트하게 */}
            <Box 
              sx={{ 
                mb: 2, 
                p: 1.5,
                background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                color: 'white',
                borderRadius: 1,
              }}
            >
              <Box display="flex" alignItems="center" justifyContent="space-between">
                <Box display="flex" alignItems="center">
                  <AccountBalanceWallet sx={{ mr: 0.5, fontSize: 20 }} />
                  <Typography variant="body2">보유 현금</Typography>
                </Box>
                <Typography variant="subtitle1" fontWeight="bold">
                  {formatPrice(user?.availableMoney)}
                </Typography>
              </Box>
              
              {tabValue === 1 && (
                <Typography variant="caption" sx={{ opacity: 0.9, mt: 0.5, display: 'block' }}>
                  보유 수량: {getHoldingQuantity().toLocaleString()}주
                </Typography>
              )}
            </Box>

            {/* 매수/매도 탭 - 더 작게 */}
            <Tabs
              value={tabValue}
              onChange={handleTabChange}
              variant="fullWidth"
              sx={{ 
                mb: 2,
                minHeight: '40px',
                '& .MuiTab-root': {
                  fontWeight: 'bold',
                  fontSize: '0.9rem',
                  minHeight: '40px',
                  py: 1,
                },
              }}
            >
              <Tab 
                icon={<ShoppingCart sx={{ fontSize: 18 }} />} 
                label="매수" 
                iconPosition="start"
                sx={{ 
                  color: tabValue === 0 ? 'error.main' : 'inherit',
                  '&.Mui-selected': { color: 'error.main' },
                }}
              />
              <Tab 
                icon={<Sell sx={{ fontSize: 18 }} />} 
                label="매도" 
                iconPosition="start"
                sx={{ 
                  color: tabValue === 1 ? 'primary.main' : 'inherit',
                  '&.Mui-selected': { color: 'primary.main' },
                }}
              />
            </Tabs>

            {/* 수량 입력 */}
            <Box mb={2}>
              <Typography variant="body2" gutterBottom fontWeight="medium">
                {tabValue === 0 ? '매수' : '매도'} 수량
              </Typography>
              <TextField
                fullWidth
                size="small"
                type="number"
                value={quantity}
                onChange={(e) => {
                  setQuantity(e.target.value)
                  setError('')
                  setSuccess('')
                }}
                placeholder="수량 입력"
                InputProps={{
                  endAdornment: (
                    <InputAdornment position="end">
                      <Button 
                        size="small" 
                        onClick={handleMaxClick}
                        variant="outlined"
                        sx={{ minWidth: 'auto', px: 1, fontSize: '0.75rem' }}
                      >
                        MAX
                      </Button>
                    </InputAdornment>
                  ),
                }}
                sx={{
                  '& .MuiOutlinedInput-root': { borderRadius: 1 },
                }}
              />
              <Typography variant="caption" color="text.secondary" sx={{ mt: 0.5, display: 'block' }}>
                {tabValue === 0 
                  ? `최대: ${getMaxBuyQuantity().toLocaleString()}주`
                  : `보유: ${getHoldingQuantity().toLocaleString()}주`
                }
              </Typography>
            </Box>

            {/* 계산 결과 - 더 컴팩트하게 */}
            {quantity && parseInt(quantity) > 0 && (
              <motion.div
                initial={{ opacity: 0, y: 10 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.3 }}
              >
                <Box sx={{ mb: 2, p: 1.5, bgcolor: 'grey.50', borderRadius: 1 }}>
                  <Typography variant="body2" fontWeight="bold" gutterBottom>
                    💳 계산 결과
                  </Typography>
                  <Box display="flex" justifyContent="space-between" mb={0.5}>
                    <Typography variant="caption">단가</Typography>
                    <Typography variant="caption" fontWeight="medium">
                      {formatPrice(stockInfo.currentPrice)}
                    </Typography>
                  </Box>
                  <Box display="flex" justifyContent="space-between" mb={0.5}>
                    <Typography variant="caption">수량</Typography>
                    <Typography variant="caption" fontWeight="medium">
                      {parseInt(quantity).toLocaleString()}주
                    </Typography>
                  </Box>
                  <Divider sx={{ my: 0.5 }} />
                  <Box display="flex" justifyContent="space-between">
                    <Typography variant="body2" fontWeight="bold">
                      총 금액
                    </Typography>
                    <Typography 
                      variant="body2" 
                      fontWeight="bold"
                      color={tabValue === 0 ? 'error.main' : 'primary.main'}
                    >
                      {formatPrice(calculateTotalAmount())}
                    </Typography>
                  </Box>
                </Box>
              </motion.div>
            )}

            {/* 에러/성공 메시지 */}
            {error && (
              <Alert severity="error" sx={{ mb: 2, py: 0.5, '& .MuiAlert-message': { fontSize: '0.85rem' } }}>
                {error}
              </Alert>
            )}
            {success && (
              <Alert severity="success" sx={{ mb: 2, py: 0.5, '& .MuiAlert-message': { fontSize: '0.85rem' } }}>
                {success}
              </Alert>
            )}

            {/* 거래 버튼 */}
            <Button
              fullWidth
              variant="contained"
              color={tabValue === 0 ? 'error' : 'primary'}
              onClick={handleTradeClick}
              disabled={loading || !quantity || parseInt(quantity) <= 0}
              sx={{ 
                py: 1.2,
                fontSize: '1rem',
                fontWeight: 'bold',
                borderRadius: 1,
              }}
            >
              {loading ? (
                <CircularProgress size={20} color="inherit" />
              ) : (
                <>
                  {tabValue === 0 ? <TrendingUp sx={{ mr: 1, fontSize: 20 }} /> : <TrendingDown sx={{ mr: 1, fontSize: 20 }} />}
                  {tabValue === 0 ? '매수' : '매도'} 주문
                </>
              )}
            </Button>

            <Typography variant="caption" color="text.secondary" sx={{ display: 'block', textAlign: 'center', mt: 1 }}>
              * 현재 종가 기준 즉시 체결
            </Typography>
          </CardContent>
        </Card>
      </motion.div>

      {/* 확인 다이얼로그 */}
      <Dialog
        open={confirmDialog.open}
        onClose={() => setConfirmDialog({ open: false, type: '', data: null })}
        maxWidth="xs"
        fullWidth
        PaperProps={{ sx: { borderRadius: 2 } }}
      >
        <DialogTitle sx={{ textAlign: 'center', pb: 1 }}>
          {confirmDialog.type === 'buy' ? '📈 매수 확인' : '📉 매도 확인'}
        </DialogTitle>
        <DialogContent>
          <DialogContentText component="div">
            {confirmDialog.data && (
              <Box>
                <Typography gutterBottom sx={{ mb: 2, textAlign: 'center' }}>
                  주문을 실행하시겠습니까?
                </Typography>
                
                <Box sx={{ bgcolor: 'grey.50', p: 2, borderRadius: 1 }}>
                  <Box display="flex" justifyContent="space-between" mb={1}>
                    <Typography variant="body2">종목</Typography>
                    <Typography variant="body2" fontWeight="bold">
                      {confirmDialog.data.stockName}
                    </Typography>
                  </Box>
                  <Box display="flex" justifyContent="space-between" mb={1}>
                    <Typography variant="body2">가격</Typography>
                    <Typography variant="body2">
                      {formatPrice(confirmDialog.data.price)}
                    </Typography>
                  </Box>
                  <Box display="flex" justifyContent="space-between" mb={1}>
                    <Typography variant="body2">수량</Typography>
                    <Typography variant="body2">
                      {confirmDialog.data.quantity.toLocaleString()}주
                    </Typography>
                  </Box>
                  <Divider sx={{ my: 1 }} />
                  <Box display="flex" justifyContent="space-between">
                    <Typography variant="body1" fontWeight="bold">
                      총 금액
                    </Typography>
                    <Typography 
                      variant="body1" 
                      fontWeight="bold"
                      color={confirmDialog.type === 'buy' ? 'error.main' : 'primary.main'}
                    >
                      {formatPrice(confirmDialog.data.totalAmount)}
                    </Typography>
                  </Box>
                </Box>

                <Alert 
                  severity="warning"
                  sx={{ mt: 2, py: 0.5, '& .MuiAlert-message': { fontSize: '0.85rem' } }}
                >
                  주문 후 취소할 수 없습니다.
                </Alert>
              </Box>
            )}
          </DialogContentText>
        </DialogContent>
        <DialogActions sx={{ p: 2, pt: 1 }}>
          <Button
            onClick={() => setConfirmDialog({ open: false, type: '', data: null })}
            disabled={loading}
          >
            취소
          </Button>
          <Button
            onClick={executeTrade}
            variant="contained"
            color={confirmDialog.type === 'buy' ? 'error' : 'primary'}
            disabled={loading}
            startIcon={loading ? <CircularProgress size={16} /> : null}
          >
            {loading ? '처리 중...' : '확정'}
          </Button>
        </DialogActions>
      </Dialog>
    </>
  )
}

export default TradingPanel