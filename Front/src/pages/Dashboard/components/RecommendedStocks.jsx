import React from 'react'
import {
  Box,
  Typography,
  Card,
  CardContent,
  Chip,
  IconButton,
  Avatar,
  LinearProgress,
} from '@mui/material'
import { 
  TrendingUp, 
  TrendingDown, 
  Launch, 
  AutoAwesome,
  ShowChart,
  Star,
  TrendingFlat,
} from '@mui/icons-material'
import { useNavigate } from 'react-router-dom'
import { motion } from 'framer-motion'

const RecommendedStocks = ({ stocks }) => {
  const navigate = useNavigate()

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
    if (!rate) return '#9E9E9E'
    const numRate = parseFloat(rate.replace('%', ''))
    if (numRate > 0) return '#4CAF50'
    if (numRate < 0) return '#F44336'
    return '#9E9E9E'
  }

  const getChangeIcon = (rate) => {
    if (!rate) return <TrendingFlat />
    const numRate = parseFloat(rate.replace('%', ''))
    if (numRate > 0) return <TrendingUp />
    if (numRate < 0) return <TrendingDown />
    return <TrendingFlat />
  }

  const getMarketBadgeColor = (marketCode) => {
    switch (marketCode) {
      case 'KOSPI': return 'linear-gradient(135deg, #2196F3 0%, #21CBF3 100%)'
      case 'KOSDAQ': return 'linear-gradient(135deg, #FF9800 0%, #FFB74D 100%)'
      default: return 'linear-gradient(135deg, #9E9E9E 0%, #BDBDBD 100%)'
    }
  }

  const getStockAvatar = (stockName) => {
    const firstChar = stockName?.charAt(0) || 'S'
    const colors = [
      'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
      'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
      'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
      'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)',
      'linear-gradient(135deg, #fa709a 0%, #fee140 100%)',
    ]
    const colorIndex = stockName?.charCodeAt(0) % colors.length || 0
    return { char: firstChar, gradient: colors[colorIndex] }
  }

  const handleStockClick = (stockId) => {
    navigate(`/stocks/${stockId}`)
  }

  if (!stocks || stocks.length === 0) {
    return (
      <Box 
        display="flex" 
        flexDirection="column" 
        alignItems="center" 
        justifyContent="center" 
        py={6}
      >
        <Box
          sx={{
            width: 80,
            height: 80,
            borderRadius: '50%',
            background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            mb: 2,
            animation: 'pulse 2s ease-in-out infinite',
            '@keyframes pulse': {
              '0%, 100%': { transform: 'scale(1)', opacity: 0.8 },
              '50%': { transform: 'scale(1.05)', opacity: 1 },
            },
          }}
        >
          <AutoAwesome sx={{ fontSize: 40, color: 'white' }} />
        </Box>
        <Typography color="text.secondary" variant="h6" gutterBottom>
          추천 주식을 준비중입니다
        </Typography>
        <Typography color="text.secondary" variant="body2">
          AI가 선별한 투자 기회를 곧 제공해드릴게요
        </Typography>
      </Box>
    )
  }

  return (
    <Box sx={{ height: '100%', overflow: 'auto', px: 1 }}>
      {stocks.slice(0, 8).map((stock, index) => {
        const avatar = getStockAvatar(stock.stockName)
        const changeRate = parseFloat(stock.changeRate?.replace('%', '') || '0')
        
        return (
          <motion.div
            key={stock.stockId}
            initial={{ opacity: 0, y: 20, scale: 0.95 }}
            animate={{ opacity: 1, y: 0, scale: 1 }}
            transition={{ duration: 0.4, delay: index * 0.05 }}
            whileHover={{ y: -2 }}
          >
            <Card
              sx={{
                mb: 1.5,
                background: 'rgba(255,255,255,0.95)',
                backdropFilter: 'blur(10px)',
                border: '1px solid rgba(0,0,0,0.06)',
                borderRadius: 3,
                cursor: 'pointer',
                transition: 'all 0.3s cubic-bezier(0.4, 0, 0.2, 1)',
                '&:hover': {
                  boxShadow: '0 12px 40px rgba(0,0,0,0.15)',
                  transform: 'translateY(-4px)',
                  border: '1px solid rgba(102, 126, 234, 0.2)',
                },
              }}
              onClick={() => handleStockClick(stock.stockId)}
            >
              <CardContent sx={{ p: 2 }}>
                <Box display="flex" alignItems="center" justifyContent="space-between">
                  {/* 왼쪽: 주식 정보 */}
                  <Box display="flex" alignItems="center" flex={1}>
                    <Avatar
                      sx={{
                        width: 44,
                        height: 44,
                        background: avatar.gradient,
                        mr: 2,
                        fontWeight: 'bold',
                        fontSize: '1.1rem',
                        boxShadow: '0 4px 12px rgba(0,0,0,0.15)',
                      }}
                    >
                      {avatar.char}
                    </Avatar>
                    
                    <Box flex={1}>
                      <Box display="flex" alignItems="center" mb={0.5}>
                        <Typography 
                          variant="body1" 
                          fontWeight="bold" 
                          sx={{ 
                            mr: 1,
                            maxWidth: 120,
                            overflow: 'hidden',
                            textOverflow: 'ellipsis',
                            whiteSpace: 'nowrap',
                          }}
                        >
                          {stock.stockName}
                        </Typography>
                        <Chip
                          label={stock.marketCode}
                          size="small"
                          sx={{
                            background: getMarketBadgeColor(stock.marketCode),
                            color: 'white',
                            fontWeight: 'bold',
                            fontSize: '0.65rem',
                            height: 18,
                            '& .MuiChip-label': { px: 1 },
                          }}
                        />
                      </Box>
                      
                      <Box display="flex" alignItems="center">
                        <Typography 
                          variant="caption" 
                          color="text.secondary"
                          sx={{ 
                            fontFamily: 'monospace',
                            backgroundColor: 'rgba(0,0,0,0.04)',
                            px: 1,
                            py: 0.25,
                            borderRadius: 1,
                            mr: 1,
                          }}
                        >
                          {stock.stockId}
                        </Typography>
                        {index < 3 && (
                          <Star sx={{ fontSize: 14, color: '#FFD700' }} />
                        )}
                      </Box>
                    </Box>
                  </Box>

                  {/* 오른쪽: 가격 정보 */}
                  <Box textAlign="right" minWidth={100}>
                    <Typography 
                      variant="body1" 
                      fontWeight="bold"
                      sx={{ mb: 0.5 }}
                    >
                      {formatPrice(stock.currentPrice)}
                    </Typography>
                    
                    <Box display="flex" alignItems="center" justifyContent="flex-end">
                      <Box
                        sx={{
                          display: 'flex',
                          alignItems: 'center',
                          px: 1,
                          py: 0.25,
                          borderRadius: 2,
                          background: `${getChangeColor(stock.changeRate)}15`,
                        }}
                      >
                        {React.cloneElement(getChangeIcon(stock.changeRate), {
                          sx: { 
                            fontSize: 14, 
                            color: getChangeColor(stock.changeRate),
                            mr: 0.5,
                          }
                        })}
                        <Typography 
                          variant="caption" 
                          fontWeight="bold"
                          sx={{ color: getChangeColor(stock.changeRate) }}
                        >
                          {formatChangeRate(stock.changeRate)}
                        </Typography>
                      </Box>
                    </Box>

                    {/* 수익률 미니 프로그레스 */}
                    <LinearProgress
                      variant="determinate"
                      value={Math.abs(changeRate) * 10} // 임시 계산
                      sx={{
                        height: 2,
                        borderRadius: 1,
                        mt: 0.5,
                        backgroundColor: 'rgba(0,0,0,0.1)',
                        '& .MuiLinearProgress-bar': {
                          borderRadius: 1,
                          background: `linear-gradient(90deg, ${getChangeColor(stock.changeRate)}, ${getChangeColor(stock.changeRate)}80)`,
                        },
                      }}
                    />
                  </Box>

                  {/* 액션 버튼 */}
                  <IconButton
                    size="small"
                    onClick={(e) => {
                      e.stopPropagation()
                      handleStockClick(stock.stockId)
                    }}
                    sx={{
                      ml: 1,
                      background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                      color: 'white',
                      width: 32,
                      height: 32,
                      '&:hover': {
                        background: 'linear-gradient(135deg, #5a6fd8 0%, #6a4190 100%)',
                        transform: 'scale(1.1)',
                      },
                      transition: 'all 0.2s ease-in-out',
                    }}
                  >
                    <Launch fontSize="small" />
                  </IconButton>
                </Box>

                {/* 특별 강조 (상위 3개) */}
                {index < 3 && (
                  <Box
                    sx={{
                      position: 'absolute',
                      top: 0,
                      left: 0,
                      right: 0,
                      height: 2,
                      background: index === 0 
                        ? 'linear-gradient(90deg, #FFD700, #FFA000)'
                        : index === 1
                        ? 'linear-gradient(90deg, #C0C0C0, #A0A0A0)'
                        : 'linear-gradient(90deg, #CD7F32, #B8860B)',
                      borderRadius: '3px 3px 0 0',
                    }}
                  />
                )}
              </CardContent>
            </Card>
          </motion.div>
        )
      })}
    </Box>
  )
}

export default RecommendedStocks