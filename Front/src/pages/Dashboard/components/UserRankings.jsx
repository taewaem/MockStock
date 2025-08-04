// src/pages/Dashboard/components/UserRankings.jsx
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
  Avatar,
} from '@mui/material'
import { TrendingUp, TrendingDown } from '@mui/icons-material'
import { motion } from 'framer-motion'

const UserRankings = ({ rankings }) => {
  const formatMoney = (money) => {
    return money ? money.toLocaleString() + '원' : '0원'
  }

  const formatPercent = (percent) => {
    if (percent === null || percent === undefined) return '0.00%'
    const sign = percent >= 0 ? '+' : ''
    return `${sign}${percent.toFixed(2)}%`
  }

  const getRankIcon = (rank) => {
    switch (rank) {
      case 1:
        return <Avatar sx={{ bgcolor: '#FFD700', width: 32, height: 32 }}>🥇</Avatar>
      case 2:
        return <Avatar sx={{ bgcolor: '#C0C0C0', width: 32, height: 32 }}>🥈</Avatar>
      case 3:
        return <Avatar sx={{ bgcolor: '#CD7F32', width: 32, height: 32 }}>🥉</Avatar>
      default:
        return (
          <Avatar sx={{ bgcolor: '#E0E0E0', width: 32, height: 32 }}>
            <Typography variant="body2" fontWeight="bold" color="black">
              {rank}
            </Typography>
          </Avatar>
        )
    }
  }

  if (!rankings || rankings.length === 0) {
    return (
      <Box textAlign="center" py={3}>
        <Typography color="text.secondary">
          랭킹 데이터가 없습니다.
        </Typography>
      </Box>
    )
  }

  return (
    <TableContainer sx={{ maxHeight: 400, bgcolor: 'white' }}>
      <Table size="small">
        <TableHead>
          <TableRow>
            <TableCell sx={{ color: '#212121', fontWeight: 'bold', border: 'none' }}>순위</TableCell>
            <TableCell sx={{ color: '#212121', fontWeight: 'bold', border: 'none' }}>사용자명</TableCell>
            <TableCell align="right" sx={{ color: '#212121', fontWeight: 'bold', border: 'none' }}>총자산</TableCell>
            <TableCell align="right" sx={{ color: '#212121', fontWeight: 'bold', border: 'none' }}>수익률</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {rankings.map((user, index) => (
            <motion.tr
              key={user.rank}
              initial={{ opacity: 0, x: -20 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ duration: 0.4, delay: index * 0.1 }}
              style={{
                cursor: 'pointer',
                transition: 'background-color 0.2s',
              }}
              onMouseEnter={(e) => e.currentTarget.style.backgroundColor = 'rgba(0,0,0,0.05)'}
              onMouseLeave={(e) => e.currentTarget.style.backgroundColor = 'transparent'}
            >
              <TableCell sx={{ border: 'none', py: 1 }}>
                <Box display="flex" alignItems="center">
                  {getRankIcon(user.rank)}
                </Box>
              </TableCell>
              <TableCell sx={{ border: 'none', py: 1 }}>
                <Typography variant="body2" fontWeight="medium" color="#0c0d0dff">
                  {user.userName}
                </Typography>
              </TableCell>
              <TableCell align="right" sx={{ border: 'none', py: 1 }}>
                <Typography variant="body2" color="#212121">
                  {formatMoney(user.totalAssets)}
                </Typography>
              </TableCell>
              <TableCell align="right" sx={{ border: 'none', py: 1 }}>
                <Box display="flex" alignItems="center" justifyContent="flex-end">
                  {user.returnRate >= 0 ? (
                    <TrendingUp sx={{ fontSize: 16, color: '#d32f2f', mr: 0.5 }} />
                  ) : (
                    <TrendingDown sx={{ fontSize: 16, color: '#1976d2', mr: 0.5 }} />
                  )}
                  <Typography
                    variant="body2"
                    fontWeight="bold"
                    color={user.returnRate >= 0 ? '#d32f2f' : '#1976d2'}
                  >
                    {formatPercent(user.returnRate)}
                  </Typography>
                </Box>
              </TableCell>
            </motion.tr>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  )
}

export default UserRankings
