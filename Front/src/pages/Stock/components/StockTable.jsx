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

const StockTable = ({ stocks }) => {
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

  const handleStockClick = (stockId) => {
    navigate(`/stocks/${stockId}`)
  }

  if (!stocks || stocks.length === 0) {
    return (
      <Box textAlign="center" py={4}>
        <Typography color="text.secondary">
          표시할 주식 데이터가 없습니다.
        </Typography>
      </Box>
    )
  }

  return (
    <TableContainer component={Paper} className="responsive-table">
      <Table>
        <TableHead>
          <TableRow sx={{ backgroundColor: 'grey.50' }}>
            <TableCell>종목명</TableCell>
            <TableCell>종목코드</TableCell>
            <TableCell align="right">현재가</TableCell>
            <TableCell align="right">전일대비</TableCell>
            <TableCell align="right">전일대비율</TableCell>
            <TableCell>시장구분</TableCell>
            <TableCell align="center">상세보기</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {stocks.map((stock) => (
            <TableRow 
              key={stock.stockId}
              hover
              sx={{ cursor: 'pointer' }}
              onClick={() => handleStockClick(stock.stockId)}
            >
              <TableCell>
                <Typography variant="body2" fontWeight="medium">
                  {stock.stockName}
                </Typography>
              </TableCell>
              <TableCell>
                <Typography variant="body2" color="text.secondary">
                  {stock.stockId}
                </Typography>
              </TableCell>
              <TableCell align="right">
                <Typography variant="body2">
                  {formatPrice(stock.currentPrice)}
                </Typography>
              </TableCell>
              <TableCell align="right">
                <Typography 
                  variant="body2"
                  sx={{ 
                    color: stock.priceChange?.startsWith('-') ? 'primary.main' : 'error.main'
                  }}
                >
                  {stock.priceChange || '0'}
                </Typography>
              </TableCell>
              <TableCell align="right">
                <Chip
                  label={formatChangeRate(stock.changeRate)}
                  color={getChangeColor(stock.changeRate)}
                  size="small"
                  variant="outlined"
                />
              </TableCell>
              <TableCell>
                <Chip
                  label={stock.marketCode === 'KOSPI' ? 'KOSPI' : 'KOSDAQ'}
                  color={stock.marketCode === 'KOSPI' ? 'primary' : 'secondary'}
                  size="small"
                />
              </TableCell>
              <TableCell align="center">
                <Button
                  size="small"
                  variant="outlined"
                  onClick={(e) => {
                    e.stopPropagation()
                    handleStockClick(stock.stockId)
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

export default StockTable