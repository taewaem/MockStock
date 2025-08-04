import React from 'react'
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
} from '@mui/material'
import { 
  TrendingUp, 
  TrendingDown, 
  Launch, 
  LocalFireDepartment,
  TrendingFlat,
} from '@mui/icons-material'
import { useNavigate } from 'react-router-dom'
import { motion } from 'framer-motion'

const VolumeRankings = ({ rankings }) => {
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

  const formatVolume = (volume) => {
    if (!volume) return '-'
    const num = typeof volume === 'string' ? parseInt(volume.replace(/,/g, '')) : volume
    if (num >= 100000000) {
      return (num / 100000000).toFixed(1) + '억'
    } else if (num >= 10000) {
      return (num / 10000).toFixed(0) + '만'
    }
    return num.toLocaleString()
  }

  const getRankColor = (rank) => {
    if (rank === 1) return '#ff9800' // 1위 주황색
    if (rank <= 3) return '#ff9800' // 상위 3위 주황색
    return '#757575' // 나머지 회색
  }

  const handleStockClick = (stockId) => {
    navigate(`/stocks/${stockId}`)
  }

  if (!rankings || rankings.length === 0) {
    return (
      <Box 
        display="flex" 
        flexDirection="column" 
        alignItems="center" 
        justifyContent="center" 
        py={4}
      >
        <LocalFireDepartment sx={{ fontSize: 48, color: '#ff9800', mb: 2 }} />
        <Typography variant="subtitle1" fontWeight="600" gutterBottom color="#2c3e50">
          거래량 랭킹을 준비중입니다
        </Typography>
        <Typography variant="body2" color="text.secondary">
          실시간 거래량 데이터를 불러오고 있어요
        </Typography>
      </Box>
    )
  }

  return (
    <List sx={{ width: '100%', p: 0 }}>
      {rankings.slice(0, 10).map((item, index) => (
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
              border: '1px solid #fff3e0',
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
                  backgroundColor: getRankColor(item.rank),
                  color: 'white',
                  width: 36,
                  height: 36,
                  fontSize: '0.9rem',
                  fontWeight: 'bold',
                }}
              >
                {item.rank}
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
                  {item.rank <= 3 && (
                    <LocalFireDepartment sx={{ fontSize: 14, color: '#ff9800' }} />
                  )}
                </Box>
              }
              secondary={
                <Box>
                  <Typography variant="caption" color="text.secondary">
                    {formatPrice(item.currentPrice)}
                  </Typography>
                  <br />
                  <Typography variant="caption" color="text.secondary">
                    거래량: {formatVolume(item.totalVolume)}
                  </Typography>
                </Box>
              }
            />
            
            <ListItemSecondaryAction>
              <Box display="flex" flexDirection="column" alignItems="flex-end" gap={0.5}>
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

export default VolumeRankings