import React from 'react'
import { Box } from '@mui/material'
import {
  Chart as ChartJS,
  ArcElement,
  Tooltip,
  Legend,
} from 'chart.js'
import { Doughnut } from 'react-chartjs-2'

ChartJS.register(ArcElement, Tooltip, Legend)

const PortfolioChart = ({ holdings }) => {
  if (!holdings || holdings.length === 0) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" height="100%">
        보유 주식이 없습니다.
      </Box>
    )
  }

  // 색상 팔레트
  const colors = [
    '#FF6384',
    '#36A2EB',
    '#FFCE56',
    '#4BC0C0',
    '#9966FF',
    '#FF9F40',
    '#FF6384',
    '#C9CBCF'
  ]

  const data = {
    labels: holdings.map(h => h.stockName),
    datasets: [
      {
        data: holdings.map(h => h.totalValue),
        backgroundColor: colors.slice(0, holdings.length),
        borderColor: colors.slice(0, holdings.length).map(color => color + '80'),
        borderWidth: 2,
      },
    ],
  }

  const options = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'bottom',
        labels: {
          padding: 20,
          usePointStyle: true,
        },
      },
      tooltip: {
        callbacks: {
          label: function(context) {
            const label = context.label || ''
            const value = context.parsed
            const total = context.dataset.data.reduce((a, b) => a + b, 0)
            const percentage = ((value / total) * 100).toFixed(1)
            return `${label}: ${value.toLocaleString()}원 (${percentage}%)`
          }
        }
      },
    },
  }

  return (
    <Box height="100%" display="flex" alignItems="center">
      <Doughnut data={data} options={options} />
    </Box>
  )
}

export default PortfolioChart
