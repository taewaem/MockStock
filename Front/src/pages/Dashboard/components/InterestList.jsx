import React, { useState, useEffect } from 'react'
import {
  Box,
  Typography,
  List,
  ListItem,
  ListItemAvatar,
  ListItemText,
  ListItemSecondaryAction,
  Avatar,
  IconButton,
  Button,
  Alert,
  Chip,
} from '@mui/material'
import { 
  TrendingUp, 
  TrendingDown, 
  Launch, 
  BookmarkBorder,
  Bookmark,
  TrendingFlat,
  Login as LoginIcon,
} from '@mui/icons-material'
import { useNavigate } from 'react-router-dom'
import { motion } from 'framer-motion'
import { useAuth } from '../../../contexts/AuthContext'
import { api } from '../../../services/apiClient'

const InterestList = () => {
  const navigate = useNavigate()
  const { user } = useAuth()
  const [interests, setInterests] = useState([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')

  useEffect(() => {
    if (user) {
      fetchInterests()
    }
  }, [user])

  const fetchInterests = async () => {
    try {
      setLoading(true)
      const response = await api.interests.getMyInterests(0, 8)
      
      if (response.data.success) {
        setInterests(response.data.data.content || [])
      }
    } catch (error) {
      console.error('관심목록 조회 실패:', error)
      setError('관심목록을 불러오는데 실패했습니다.')
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
    if (!rate) return '#757575'
    const numRate = parseFloat(rate.replace('%', ''))
    if (numRate > 0) return '#d32f2f' // 빨간색 (상승)
    if (numRate < 0) return '#1976d2' // 파란색 (하락)
    return '#757575'
  }

  const getChangeIcon = (rate) => {
    if (!rate) return <TrendingFlat />
    const numRate = parseFloat(rate.replace('%', ''))
    if (numRate > 0) return <TrendingUp />
    if (numRate < 0) return <TrendingDown />
    return <TrendingFlat />
  }

  const handleStockClick = (stockId) => {
    navigate(`/stocks/${stockId}`)
  }

  const handleRemoveInterest = async (stockId, e) => {
    e.stopPropagation()
    try {
      await api.interests.remove(stockId)
      setInterests(prev => prev.filter(item => item.stockId !== stockId))
    } catch (error) {
      console.error('관심목록 삭제 실패:', error)
    }
  }

  // 비로그인 상태
  if (!user) {
    return (
      <Box 
        display="flex" 
        flexDirection="column" 
        alignItems="center" 
        justifyContent="center" 
        py={4}
      >
        <BookmarkBorder sx={{ fontSize: 48, color: '#1976d2', mb: 2 }} />
        <Typography variant="subtitle1" fontWeight="600" gutterBottom color="#2c3e50">
          로그인이 필요합니다
        </Typography>
        <Typography variant="body2" color="text.secondary" sx={{ mb: 2, textAlign: 'center' }}>
          관심 있는 주식을 저장하고<br/>
          실시간으로 확인하세요
        </Typography>
        <Button
          variant="contained"
          startIcon={<LoginIcon />}
          onClick={() => navigate('/login')}
          sx={{
            borderRadius: 2,
            backgroundColor: '#1976d2',
            '&:hover': {
              backgroundColor: '#1565c0',
            }
          }}
        >
          로그인하기
        </Button>
      </Box>
    )
  }

  // 로딩 상태
  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" py={4}>
        <Typography color="text.secondary">로딩중...</Typography>
      </Box>
    )
  }

  // 에러 상태
  if (error) {
    return (
      <Box py={2}>
        <Alert severity="error" sx={{ borderRadius: 2 }}>
          {error}
        </Alert>
      </Box>
    )
  }

  // 관심목록이 비어있는 경우
  if (interests.length === 0) {
    return (
      <Box 
        display="flex" 
        flexDirection="column" 
        alignItems="center" 
        justifyContent="center" 
        py={4}
      >
        <BookmarkBorder sx={{ fontSize: 48, color: '#757575', mb: 2 }} />
        <Typography variant="subtitle1" fontWeight="600" gutterBottom color="#2c3e50">
          관심목록이 비어있어요
        </Typography>
        <Typography variant="body2" color="text.secondary" sx={{ mb: 2, textAlign: 'center' }}>
          마음에 드는 주식을 찾아서<br/>
          관심목록에 추가해보세요
        </Typography>
        <Button
          variant="outlined"
          onClick={() => navigate('/stocks')}
          sx={{
            borderRadius: 2,
            borderColor: '#1976d2',
            color: '#1976d2',
            '&:hover': {
              backgroundColor: '#1976d2',
              color: 'white',
            }
          }}
        >
          주식 둘러보기
        </Button>
      </Box>
    )
  }

  return (
    <List sx={{ width: '100%', p: 0 }}>
      {interests.slice(0, 8).map((item, index) => (
        <motion.div
          key={item.stockId}
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.3, delay: index * 0.05 }}
        >
          <ListItem
            button
            onClick={() => handleStockClick(item.stockId)}
            sx={{
              mb: 1,
              borderRadius: 2,
              border: '1px solid #e3f2fd',
              backgroundColor: 'white',
              '&:hover': {
                backgroundColor: '#f5f5f5',
                boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
              },
              transition: 'all 0.2s ease-in-out',
            }}
          >
            <ListItemAvatar>
              <Avatar
                sx={{
                  backgroundColor: '#1976d2',
                  color: 'white',
                  width: 36,
                  height: 36,
                  fontSize: '0.9rem',
                }}
              >
                {item.stockName?.charAt(0) || 'S'}
              </Avatar>
            </ListItemAvatar>
            
            <ListItemText
              primary={
                <Box display="flex" alignItems="center" gap={1}>
                  <Typography 
                    variant="body2" 
                    fontWeight="600"
                    sx={{ 
                      maxWidth: 80,
                      overflow: 'hidden',
                      textOverflow: 'ellipsis',
                      whiteSpace: 'nowrap',
                    }}
                  >
                    {item.stockName}
                  </Typography>
                  <Chip
                    label={item.marketCode}
                    size="small"
                    color={item.marketCode === 'KOSPI' ? 'primary' : 'secondary'}
                    sx={{ fontSize: '0.7rem', height: 16 }}
                  />
                </Box>
              }
              secondary={
                <Box display="flex" alignItems="center" gap={1}>
                  <Typography variant="caption" color="text.secondary">
                    {formatPrice(item.currentPrice)}
                  </Typography>
                  <Box display="flex" alignItems="center">
                    {React.cloneElement(getChangeIcon(item.changeRate), {
                      sx: { fontSize: 12, color: getChangeColor(item.changeRate) }
                    })}
                    <Typography 
                      variant="caption" 
                      sx={{ color: getChangeColor(item.changeRate), ml: 0.5 }}
                    >
                      {formatChangeRate(item.changeRate)}
                    </Typography>
                  </Box>
                </Box>
              }
            />
            
            <ListItemSecondaryAction>
              <Box display="flex" gap={0.5}>
                <IconButton
                  size="small"
                  onClick={(e) => handleRemoveInterest(item.stockId, e)}
                  sx={{
                    color: '#1976d2',
                    '&:hover': { backgroundColor: '#e3f2fd' },
                  }}
                >
                  <Bookmark fontSize="small" />
                </IconButton>
                <IconButton
                  size="small"
                  onClick={(e) => {
                    e.stopPropagation()
                    handleStockClick(item.stockId)
                  }}
                  sx={{
                    color: '#757575',
                    '&:hover': { backgroundColor: '#f5f5f5' },
                  }}
                >
                  <Launch fontSize="small" />
                </IconButton>
              </Box>
            </ListItemSecondaryAction>
          </ListItem>
        </motion.div>
      ))}
    </List>
  )
}

export default InterestList 