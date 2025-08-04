import React from 'react'
import {
  Paper,
  Typography,
  Grid,
  Box,
  Divider,
  Chip,
} from '@mui/material'

const StockInfo = ({ stockDetail }) => {
  const formatNumber = (num) => {
    if (!num || num === '-') return '-'
    
    // 문자열에서 숫자 추출
    const numValue = parseFloat(num.replace(/[^0-9.-]/g, ''))
    if (isNaN(numValue)) return '-'
    
    // 억/조 단위로 변환
    if (numValue >= 1000000000000) {
      return (numValue / 1000000000000).toFixed(1) + '조'
    } else if (numValue >= 100000000) {
      return (numValue / 100000000).toFixed(1) + '억'
    } else if (numValue >= 10000) {
      return (numValue / 10000).toFixed(1) + '만'
    }
    
    return numValue.toLocaleString()
  }

  const formatVolume = (volume) => {
    if (!volume || volume === '-') return '-'
    const num = parseInt(volume.replace(/,/g, ''))
    if (isNaN(num)) return '-'
    
    if (num >= 100000000) {
      return (num / 100000000).toFixed(1) + '억주'
    } else if (num >= 10000) {
      return (num / 10000).toFixed(1) + '만주'
    }
    return num.toLocaleString() + '주'
  }

  if (!stockDetail) {
    return (
      <Paper sx={{ p: 3 }}>
        <Typography variant="h6" gutterBottom>
          📊 상세 정보
        </Typography>
        <Typography color="text.secondary">
          상세 정보를 불러오는 중...
        </Typography>
      </Paper>
    )
  }

  return (
    <Paper sx={{ p: 3 }}>
      <Typography variant="h6" gutterBottom>
        📊 상세 정보
      </Typography>
      
      <Grid container spacing={3}>
        {/* 거래 정보 */}
        <Grid item xs={12} md={6}>
          <Typography variant="subtitle1" gutterBottom sx={{ fontWeight: 'bold' }}>
            거래 정보
          </Typography>
          <Divider sx={{ mb: 2 }} />
          
          <Grid container spacing={2}>
            <Grid item xs={6}>
              <Typography variant="body2" color="text.secondary">
                누적거래량
              </Typography>
              <Typography variant="body1">
                {formatVolume(stockDetail.accumulatedVolume)}
              </Typography>
            </Grid>
            <Grid item xs={6}>
              <Typography variant="body2" color="text.secondary">
                시가총액
              </Typography>
              <Typography variant="body1">
                {formatNumber(stockDetail.marketCap)}원
              </Typography>
            </Grid>
            <Grid item xs={6}>
              <Typography variant="body2" color="text.secondary">
                52주 최고가
              </Typography>
              <Typography variant="body1" sx={{ color: 'error.main' }}>
                {stockDetail.week52High ? 
                  parseInt(stockDetail.week52High).toLocaleString() + '원' : '-'
                }
              </Typography>
            </Grid>
            <Grid item xs={6}>
              <Typography variant="body2" color="text.secondary">
                52주 최저가
              </Typography>
              <Typography variant="body1" sx={{ color: 'primary.main' }}>
                {stockDetail.week52Low ? 
                  parseInt(stockDetail.week52Low).toLocaleString() + '원' : '-'
                }
              </Typography>
            </Grid>
          </Grid>
        </Grid>

        {/* 투자 지표 */}
        <Grid item xs={12} md={6}>
          <Typography variant="subtitle1" gutterBottom sx={{ fontWeight: 'bold' }}>
            투자 지표
          </Typography>
          <Divider sx={{ mb: 2 }} />
          
          <Grid container spacing={2}>
            <Grid item xs={6}>
              <Typography variant="body2" color="text.secondary">
                PER (주가수익비율)
              </Typography>
              <Box display="flex" alignItems="center">
                <Typography variant="body1" sx={{ mr: 1 }}>
                  {stockDetail.per || '-'}
                </Typography>
                {stockDetail.per && parseFloat(stockDetail.per) < 15 && (
                  <Chip label="저평가" color="success" size="small" />
                )}
              </Box>
            </Grid>
            <Grid item xs={6}>
              <Typography variant="body2" color="text.secondary">
                PBR (주가순자산비율)
              </Typography>
              <Box display="flex" alignItems="center">
                <Typography variant="body1" sx={{ mr: 1 }}>
                  {stockDetail.pbr || '-'}
                </Typography>
                {stockDetail.pbr && parseFloat(stockDetail.pbr) < 1 && (
                  <Chip label="저평가" color="success" size="small" />
                )}
              </Box>
            </Grid>
          </Grid>

          {/* 투자 지표 설명 */}
          <Box mt={2} p={2} bgcolor="grey.50" borderRadius={1}>
            <Typography variant="caption" color="text.secondary">
              💡 투자 팁<br/>
              • PER 15 미만: 저평가 가능성<br/>
              • PBR 1 미만: 저평가 가능성<br/>
              * 이는 참고용이며, 투자 결정은 신중히 하세요.
            </Typography>
          </Box>
        </Grid>
      </Grid>
    </Paper>
  )
}

export default StockInfo