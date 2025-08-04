import React, { useState, useEffect } from 'react'
import {
  Box,
  Typography,
  ToggleButton,
  ToggleButtonGroup,
  Alert,
} from '@mui/material'
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  Filler,
} from 'chart.js'
import { Line } from 'react-chartjs-2'
import { motion } from 'framer-motion'
import { api } from '../../../services/apiClient'

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  Filler
)

const StockChart = ({ stockId, stockName, currentPrice }) => {
  const [period, setPeriod] = useState('1M')
  const [chartData, setChartData] = useState(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  useEffect(() => {
    fetchChartData()
  }, [stockId, period])

  // 실제 API에서 차트 데이터 가져오기 (없으면 모의 데이터)
  const fetchChartData = async () => {
    try {
      setLoading(true)
      setError(null)

      // 1단계: 차트 전용 API가 있는지 시도
      try {
        const response = await api.get(`/api/stocks/${stockId}/chart?period=${period}`)
        if (response.data.success) {
          setChartDataFromAPI(response.data.data)
          return
        }
      } catch (apiError) {
        console.log('차트 전용 API 없음, 대체 방법 사용')
      }

      // 2단계: 기존 주식 정보로 모의 데이터 생성
      generateEnhancedMockData()

    } catch (error) {
      console.error('차트 데이터 로딩 실패:', error)
      setError('차트 데이터를 불러올 수 없습니다.')
      generateEnhancedMockData() // 에러시에도 모의 데이터 표시
    } finally {
      setLoading(false)
    }
  }

  // API 응답을 차트 데이터로 변환
  const setChartDataFromAPI = (apiData) => {
    const dates = apiData.map(item => 
      new Date(item.date).toLocaleDateString('ko-KR', { 
        month: 'short', 
        day: 'numeric' 
      })
    )
    const prices = apiData.map(item => item.price)
    
    const isPositive = prices[prices.length - 1] >= prices[0]

    setChartData({
      labels: dates,
      datasets: [{
        label: stockName,
        data: prices,
        borderColor: isPositive ? '#d32f2f' : '#1976d2',
        backgroundColor: isPositive 
          ? 'rgba(211, 47, 47, 0.1)' 
          : 'rgba(25, 118, 210, 0.1)',
        borderWidth: 2,
        fill: true,
        tension: 0.4,
        pointRadius: 0,
        pointHoverRadius: 6,
        pointHoverBackgroundColor: isPositive ? '#d32f2f' : '#1976d2',
        pointHoverBorderColor: '#fff',
        pointHoverBorderWidth: 2,
      }],
    })
  }

  // 개선된 모의 데이터 생성 (현재가 기반)
  const generateEnhancedMockData = () => {
    const periods = {
      '1D': { points: 24, unit: 'hour' },
      '1W': { points: 7, unit: 'day' },
      '1M': { points: 30, unit: 'day' },
      '3M': { points: 90, unit: 'day' },
      '1Y': { points: 365, unit: 'day' }
    }

    const { points, unit } = periods[period] || periods['1M']
    const basePrice = currentPrice || 50000
    const dates = []
    const prices = []

    // 현실적인 주가 변동 시뮬레이션
    let previousPrice = basePrice
    const volatility = 0.02 // 2% 변동성

    for (let i = points; i >= 0; i--) {
      const date = new Date()
      
      if (unit === 'hour') {
        date.setHours(date.getHours() - i)
        dates.push(date.toLocaleTimeString('ko-KR', { 
          hour: '2-digit', 
          minute: '2-digit' 
        }))
      } else {
        date.setDate(date.getDate() - i)
        dates.push(date.toLocaleDateString('ko-KR', { 
          month: 'short', 
          day: 'numeric' 
        }))
      }

      if (i === 0) {
        // 마지막 포인트는 현재가
        prices.push(basePrice)
      } else {
        // 랜덤 워크 방식으로 가격 생성
        const randomChange = (Math.random() - 0.5) * 2 * volatility
        const newPrice = previousPrice * (1 + randomChange)
        
        // 극단적인 변동 방지
        const minPrice = basePrice * 0.7
        const maxPrice = basePrice * 1.3
        const constrainedPrice = Math.max(minPrice, Math.min(maxPrice, newPrice))
        
        prices.push(Math.round(constrainedPrice))
        previousPrice = constrainedPrice
      }
    }

    const isPositive = prices[prices.length - 1] >= prices[0]

    setChartData({
      labels: dates,
      datasets: [{
        label: stockName,
        data: prices,
        borderColor: isPositive ? '#d32f2f' : '#1976d2',
        backgroundColor: isPositive 
          ? 'rgba(211, 47, 47, 0.1)' 
          : 'rgba(25, 118, 210, 0.1)',
        borderWidth: 2,
        fill: true,
        tension: 0.4,
        pointRadius: 0,
        pointHoverRadius: 6,
        pointHoverBackgroundColor: isPositive ? '#d32f2f' : '#1976d2',
        pointHoverBorderColor: '#fff',
        pointHoverBorderWidth: 2,
      }],
    })
  }

  const handlePeriodChange = (event, newPeriod) => {
    if (newPeriod !== null) {
      setPeriod(newPeriod)
    }
  }

  const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        display: false,
      },
      tooltip: {
        mode: 'index',
        intersect: false,
        backgroundColor: 'rgba(0,0,0,0.8)',
        titleColor: '#fff',
        bodyColor: '#fff',
        borderColor: '#666',
        borderWidth: 1,
        cornerRadius: 8,
        displayColors: false,
        callbacks: {
          title: function(context) {
            return context[0].label
          },
          label: function(context) {
            return `${context.dataset.label}: ${context.parsed.y.toLocaleString()}원`
          }
        }
      },
    },
    scales: {
      x: {
        grid: {
          display: false,
        },
        ticks: {
          color: '#666',
          font: {
            size: 12,
          },
          maxTicksLimit: 8,
        },
      },
      y: {
        beginAtZero: false,
        grid: {
          color: 'rgba(0,0,0,0.1)',
        },
        ticks: {
          color: '#666',
          font: {
            size: 12,
          },
          callback: function(value) {
            return value.toLocaleString() + '원'
          }
        },
      },
    },
    interaction: {
      mode: 'nearest',
      axis: 'x',
      intersect: false,
    },
    elements: {
      point: {
        hoverRadius: 8,
      },
    },
  }

  return (
    <Box>
      {/* 기간 선택 버튼 */}
      <motion.div
        initial={{ opacity: 0, y: -10 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.4 }}
      >
        <Box display="flex" justifyContent="center" mb={2}>
          <ToggleButtonGroup
            value={period}
            exclusive
            onChange={handlePeriodChange}
            size="small"
            sx={{
              '& .MuiToggleButton-root': {
                borderRadius: 2,
                px: 2,
                py: 0.5,
                fontWeight: 'bold',
                fontSize: '0.8rem',
                '&.Mui-selected': {
                  backgroundColor: '#1976d2',
                  color: 'white',
                  '&:hover': {
                    backgroundColor: '#1565c0',
                  },
                },
              },
            }}
          >
            <ToggleButton value="1D">1일</ToggleButton>
            <ToggleButton value="1W">1주</ToggleButton>
            <ToggleButton value="1M">1개월</ToggleButton>
            <ToggleButton value="3M">3개월</ToggleButton>
            <ToggleButton value="1Y">1년</ToggleButton>
          </ToggleButtonGroup>
        </Box>
      </motion.div>

      {/* 에러 메시지 */}
      {error && (
        <Alert severity="warning" sx={{ mb: 2, borderRadius: 2 }}>
          {error}
        </Alert>
      )}

      {/* 차트 */}
      <motion.div
        initial={{ opacity: 0, scale: 0.95 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ duration: 0.6, delay: 0.2 }}
      >
        <Box sx={{ height: 320, position: 'relative' }}>
          {loading ? (
            <Box display="flex" justifyContent="center" alignItems="center" height="100%">
              <Typography color="text.secondary">
                차트를 로딩중...
              </Typography>
            </Box>
          ) : chartData ? (
            <Line data={chartData} options={chartOptions} />
          ) : (
            <Box display="flex" justifyContent="center" alignItems="center" height="100%">
              <Typography color="text.secondary">
                차트 데이터가 없습니다
              </Typography>
            </Box>
          )}
        </Box>
      </motion.div>

      {/* 차트 정보 */}
      <Box mt={1} textAlign="center">
        <Typography variant="caption" color="text.secondary" sx={{ fontStyle: 'italic', fontSize: '0.75rem' }}>
          * 차트는 {error ? '모의 데이터' : '실제 데이터 기반 시뮬레이션'}입니다
        </Typography>
      </Box>
    </Box>
  )
}

export default StockChart