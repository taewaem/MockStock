import React from 'react'
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
  Box,
  Chip,
  Button,
  Paper,
} from '@mui/material'
import { useNavigate } from 'react-router-dom'

const RankingTable = ({ rankings }) => {
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
    if (!rate) return 'default'
    const numRate = parseFloat(rate.replace('%', ''))
    if (numRate > 0) return 'error'
    if (numRate < 0) return 'primary'
    return 'default'
  }

  const formatVolume = (volume) => {
    if (!volume) return '-'
    const num = parseInt(volume.replace(/,/g, ''))
    if (num >= 100000000) {
      return (num / 100000000).toFixed(1) + '억'
    } else if (num >= 10000) {
      return (num / 10000).toFixed(0) + '만'
    }
    return num.toLocaleString()
  }

  const handleStockClick = (stockId) => {
    navigate(`/stocks/${stockId}`)
  }

  const getRankBadge = (rank) => {
    if (rank <= 3) {
      const colors = ['gold', 'silver', '#CD7F32'] // 금, 은, 동
      const badges = ['🥇', '🥈', '🥉']
      return badges[rank - 1]
    }
    return rank
  }

  if (!rankings || rankings.length === 0) {
    return (
      <Box textAlign="center" py={4}>
        <Typography color="text.secondary">
          표시할 랭킹 데이터가 없습니다.
        </Typography>
      </Box>
    )
  }

  return (
    <TableContainer component={Paper} className="responsive-table">
      <Table>
        <TableHead>
          <TableRow sx={{ backgroundColor: 'grey.50' }}>
            <TableCell>순위</TableCell>
            <TableCell>종목명</TableCell>
            <TableCell align="right">현재가</TableCell>
            <TableCell align="right">전일대비</TableCell>
            <TableCell align="right">전일대비율</TableCell>
            {rankings[0]?.accumulatedVolume && (
              <TableCell align="right">거래량</TableCell>
            )}
            <TableCell align="center">상세보기</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {rankings.map((item) => (
            <TableRow 
              key={item.stockId}
              hover
              sx={{ cursor: 'pointer' }}
              onClick={() => handleStockClick(item.stockId)}
            >
              <TableCell>
                <Box display="flex" alignItems="center">
                  <Typography variant="h6" component="span">
                    {getRankBadge(item.rank)}
                  </Typography>
                  {item.rank > 3 && (
                    <Typography variant="body2" sx={{ ml: 1 }}>
                      {item.rank}위
                    </Typography>
                  )}
                </Box>
              </TableCell>
              <TableCell>
                <Box>
                  <Typography variant="body2" fontWeight="medium">
                    {item.stockName}
                  </Typography>
                  <Typography variant="caption" color="text.secondary">
                    {item.stockId}
                  </Typography>
                </Box>
              </TableCell>
              <TableCell align="right">
                <Typography variant="body2">
                  {formatPrice(item.currentPrice)}
                </Typography>
              </TableCell>
              <TableCell align="right">
                <Typography 
                  variant="body2"
                  sx={{ 
                    color: item.priceChange?.startsWith('-') ? 'primary.main' : 'error.main'
                  }}
                >
                  {item.priceChange || '0'}
                </Typography>
              </TableCell>
              <TableCell align="right">
                <Chip
                  label={formatChangeRate(item.changeRate)}
                  color={getChangeColor(item.changeRate)}
                  size="small"
                  variant="outlined"
                />
              </TableCell>
              {rankings[0]?.accumulatedVolume && (
                <TableCell align="right">
                  <Typography variant="body2" color="text.secondary">
                    {formatVolume(item.accumulatedVolume)}
                  </Typography>
                </TableCell>
              )}
              <TableCell align="center">
                <Button
                  size="small"
                  variant="outlined"
                  onClick={(e) => {
                    e.stopPropagation()
                    handleStockClick(item.stockId)
                  }}
                >
                  상세
                </Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  )
}

export default RankingTable