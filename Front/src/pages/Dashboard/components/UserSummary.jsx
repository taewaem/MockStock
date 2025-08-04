import React from 'react'
import {
  Grid,
  Typography,
  Box,
  Card,
  CardContent,
} from '@mui/material'
import {
  AccountBalance,
  TrendingUp,
  ShowChart,
  AccountBalanceWallet,
} from '@mui/icons-material'
import { motion } from 'framer-motion'

const UserSummary = ({ user }) => {
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

  return (
    <Card 
      sx={{ 
        height: '450px',
        borderRadius: 3,
        boxShadow: '0 2px 12px rgba(0,0,0,0.08)',
        border: '1px solid #e8f5e8',
        backgroundColor: '#f1f8e9',
      }}
    >
      <CardContent sx={{ p: 3, height: '100%' }}>
        <Typography 
          variant="h6" 
          gutterBottom 
          sx={{ 
            fontWeight: 600,
            mb: 3,
            color: '#2c3e50',
          }}
        >
          💰 {user.userName}님의 자산현황
        </Typography>
        
        {/* 2x2 그리드 레이아웃 */}
        <Grid container spacing={2} sx={{ height: 'calc(100% - 60px)' }}>
          {/* 첫 번째 줄 */}
          <Grid item xs={6}>
            <motion.div
              initial={{ opacity: 0, scale: 0.9 }}
              animate={{ opacity: 1, scale: 1 }}
              transition={{ duration: 0.4, delay: 0.1 }}
            >
              <Card 
                sx={{ 
                  height: '100%',
                  backgroundColor: 'white',
                  border: '1px solid #e0e0e0',
                  borderRadius: 2,
                  boxShadow: '0 1px 3px rgba(0,0,0,0.1)',
                  display: 'flex',
                  flexDirection: 'column',
                  justifyContent: 'center',
                  alignItems: 'center',
                  p: 2,
                }}
              >
                <AccountBalanceWallet sx={{ fontSize: 24, color: '#4caf50', mb: 1 }} />
                <Typography variant="caption" color="text.secondary" gutterBottom>
                  보유현금
                </Typography>
                <Typography variant="h6" fontWeight="bold" color="#2c3e50">
                  {formatMoney(user.availableMoney)}
                </Typography>
              </Card>
            </motion.div>
          </Grid>

          <Grid item xs={6}>
            <motion.div
              initial={{ opacity: 0, scale: 0.9 }}
              animate={{ opacity: 1, scale: 1 }}
              transition={{ duration: 0.4, delay: 0.2 }}
            >
              <Card 
                sx={{ 
                  height: '100%',
                  backgroundColor: 'white',
                  border: '1px solid #e0e0e0',
                  borderRadius: 2,
                  boxShadow: '0 1px 3px rgba(0,0,0,0.1)',
                  display: 'flex',
                  flexDirection: 'column',
                  justifyContent: 'center',
                  alignItems: 'center',
                  p: 2,
                }}
              >
                <ShowChart sx={{ fontSize: 24, color: '#ff9800', mb: 1 }} />
                <Typography variant="caption" color="text.secondary" gutterBottom>
                  주식평가금액
                </Typography>
                <Typography variant="h6" fontWeight="bold" color="#2c3e50">
                  {formatMoney(user.stockMoney)}
                </Typography>
              </Card>
            </motion.div>
          </Grid>

          {/* 두 번째 줄 */}
          <Grid item xs={6}>
            <motion.div
              initial={{ opacity: 0, scale: 0.9 }}
              animate={{ opacity: 1, scale: 1 }}
              transition={{ duration: 0.4, delay: 0.3 }}
            >
              <Card 
                sx={{ 
                  height: '100%',
                  backgroundColor: 'white',
                  border: '1px solid #e0e0e0',
                  borderRadius: 2,
                  boxShadow: '0 1px 3px rgba(0,0,0,0.1)',
                  display: 'flex',
                  flexDirection: 'column',
                  justifyContent: 'center',
                  alignItems: 'center',
                  p: 2,
                }}
              >
                <AccountBalance sx={{ fontSize: 24, color: '#2196f3', mb: 1 }} />
                <Typography variant="caption" color="text.secondary" gutterBottom>
                  총자산
                </Typography>
                <Typography variant="h6" fontWeight="bold" color="#2c3e50">
                  {formatMoney(user.totalAssets)}
                </Typography>
              </Card>
            </motion.div>
          </Grid>

          <Grid item xs={6}>
            <motion.div
              initial={{ opacity: 0, scale: 0.9 }}
              animate={{ opacity: 1, scale: 1 }}
              transition={{ duration: 0.4, delay: 0.4 }}
            >
              <Card 
                sx={{ 
                  height: '100%',
                  backgroundColor: 'white',
                  border: '1px solid #e0e0e0',
                  borderRadius: 2,
                  boxShadow: '0 1px 3px rgba(0,0,0,0.1)',
                  display: 'flex',
                  flexDirection: 'column',
                  justifyContent: 'center',
                  alignItems: 'center',
                  p: 2,
                }}
              >
                <TrendingUp sx={{ fontSize: 24, color: getReturnColor(user.returnRate), mb: 1 }} />
                <Typography variant="caption" color="text.secondary" gutterBottom>
                  총 수익률
                </Typography>
                <Typography 
                  variant="h6" 
                  fontWeight="bold"
                  sx={{ color: getReturnColor(user.returnRate) }}
                >
                  {formatPercent(user.returnRate)}
                </Typography>
                <Typography 
                  variant="caption" 
                  sx={{ color: getReturnColor(user.returnRate) }}
                >
                  {user.returnRate >= 0 ? '수익' : '손실'}
                </Typography>
              </Card>
            </motion.div>
          </Grid>
        </Grid>
      </CardContent>
    </Card>
  )
}

export default UserSummary