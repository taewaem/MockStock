import React, { useState, useEffect } from 'react'
import {
  Box,
  Typography,
  TextField,
  InputAdornment,
  Grid,
  Card,
  CardContent,
  CircularProgress,
  Alert,
  useTheme,
  useMediaQuery,
  Chip,
  Pagination,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  IconButton,
} from '@mui/material'
import { Search, TrendingUp, ShowChart, Timeline, Clear } from '@mui/icons-material'
import { motion } from 'framer-motion'
import { api } from '../../services/apiClient'
import StockTable from './components/StockTable'

const StockList = () => {
  const theme = useTheme()
  const isMobile = useMediaQuery(theme.breakpoints.down('md'))
  
  const [searchKeyword, setSearchKeyword] = useState('')
  const [searchQuery, setSearchQuery] = useState('') // 실제 검색에 사용되는 쿼리
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [currentPage, setCurrentPage] = useState(1) // 1부터 시작 (UI용)
  const [pageSize, setPageSize] = useState(20)
  const [sortBy, setSortBy] = useState('name')
  const [sortDir, setSortDir] = useState('asc')
  const [data, setData] = useState({
    stocks: [],
    totalPages: 0,
    totalElements: 0,
    isSearch: false,
  })

  useEffect(() => {
    fetchStockData()
  }, [currentPage, pageSize, sortBy, sortDir])

  useEffect(() => {
    if (searchQuery.trim()) {
      setCurrentPage(1) // 검색시 첫 페이지로
      searchStocks()
    } else {
      setCurrentPage(1) // 검색 해제시 첫 페이지로
      fetchStockData()
    }
  }, [searchQuery]) // searchQuery가 변경될 때만 실행

  const fetchStockData = async () => {
    try {
      setLoading(true)
      setError(null)
      
      const apiPage = currentPage - 1 // API는 0부터 시작
      const response = await api.stocks.getAll(apiPage, pageSize, sortBy, sortDir)
      
      if (response.data.success) {
        const pageData = response.data.data
        console.log('📊 API 응답 데이터:', pageData) // 디버깅용
        setData({
          stocks: pageData.content || [],
          totalPages: pageData.totalPages || 0,
          totalElements: pageData.totalElements || 0,
          isSearch: false,
        })
      } else {
        setError(response.data.message || '주식 데이터를 불러오는데 실패했습니다.')
      }
    } catch (error) {
      console.error('데이터 로딩 실패:', error)
      setError('주식 데이터를 불러오는데 실패했습니다.')
    } finally {
      setLoading(false)
    }
  }

  const searchStocks = async () => {
    if (!searchQuery.trim()) return
    
    try {
      setLoading(true)
      const apiPage = currentPage - 1
      const response = await api.stocks.search(searchQuery, apiPage, pageSize, sortBy, sortDir)
      
      if (response.data.success) {
        const pageData = response.data.data
        setData({
          stocks: pageData.content || [],
          totalPages: pageData.totalPages || 0,
          totalElements: pageData.totalElements || 0,
          isSearch: true,
        })
      }
    } catch (error) {
      console.error('검색 실패:', error)
      setError('검색 중 오류가 발생했습니다.')
    } finally {
      setLoading(false)
    }
  }

  const getCurrentTitle = () => {
    if (data.isSearch) {
      return `"${searchQuery}" 검색 결과`
    }
    return '전체 주식 목록'
  }

  // 검색 실행 함수
  const handleSearch = () => {
    setSearchQuery(searchKeyword.trim())
  }

  // 검색 초기화 함수  
  const handleClearSearch = () => {
    setSearchKeyword('')
    setSearchQuery('')
  }

  // 엔터키 검색
  const handleKeyDown = (event) => {
    if (event.key === 'Enter') {
      event.preventDefault()
      handleSearch()
    }
  }

  const getValidStocksCount = () => {
    return data.stocks.filter(stock => stock.currentPrice && stock.currentPrice > 0).length
  }

  const handlePageChange = (event, newPage) => {
    setCurrentPage(newPage)
  }

  const handlePageSizeChange = (event) => {
    setPageSize(event.target.value)
    setCurrentPage(1) // 페이지 사이즈 변경시 첫 페이지로
  }

  const handleSortChange = (event) => {
    setSortBy(event.target.value)
    setCurrentPage(1) // 정렬 변경시 첫 페이지로
  }

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="60vh">
        <Box textAlign="center">
          <CircularProgress size={60} />
          <Typography variant="h6" sx={{ mt: 2, color: 'text.secondary' }}>
            주식 데이터를 로딩중...
          </Typography>
        </Box>
      </Box>
    )
  }

  if (error) {
    return (
      <Box textAlign="center" mt={4}>
        <Alert severity="error" sx={{ borderRadius: 2 }}>
          {error}
        </Alert>
      </Box>
    )
  }

  return (
    <Box sx={{ maxWidth: '1400px', mx: 'auto' }}>
      {/* 헤더 */}
      <motion.div
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.6 }}
      >
        <Typography variant={isMobile ? "h5" : "h4"} gutterBottom fontWeight="700" color="#2c3e50">
          📊 주식 목록
        </Typography>
      </motion.div>

      {/* 통계 카드 */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.6, delay: 0.1 }}
      >
        <Grid container spacing={2} sx={{ mb: 3 }}>
          <Grid item xs={6} md={4}>
            <Card sx={{ backgroundColor: '#e3f2fd', border: '1px solid #bbdefb' }}>
              <CardContent sx={{ p: 2, textAlign: 'center' }}>
                <ShowChart sx={{ fontSize: 32, mb: 1, color: '#1976d2' }} />
                <Typography variant="h6" fontWeight="bold" color="#2c3e50">
                  {data.stocks.length.toLocaleString()}
                </Typography>
                <Typography variant="caption" color="text.secondary">현재 페이지</Typography>
              </CardContent>
            </Card>
          </Grid>
          <Grid item xs={6} md={4}>
            <Card sx={{ backgroundColor: '#e8f5e8', border: '1px solid #c8e6c9' }}>
              <CardContent sx={{ p: 2, textAlign: 'center' }}>
                <TrendingUp sx={{ fontSize: 32, mb: 1, color: '#4caf50' }} />
                <Typography variant="h6" fontWeight="bold" color="#2c3e50">
                  {data.totalElements.toLocaleString()}
                </Typography>
                <Typography variant="caption" color="text.secondary">총 종목 수</Typography>
              </CardContent>
            </Card>
          </Grid>
          <Grid item xs={12} md={4}>
            <Card sx={{ backgroundColor: '#fff3e0', border: '1px solid #ffcc02' }}>
              <CardContent sx={{ p: 2, textAlign: 'center' }}>
                <Timeline sx={{ fontSize: 32, mb: 1, color: '#ff9800' }} />
                <Typography variant="h6" fontWeight="bold" color="#2c3e50">
                  {currentPage} / {data.totalPages}
                </Typography>
                <Typography variant="caption" color="text.secondary">페이지</Typography>
              </CardContent>
            </Card>
          </Grid>
        </Grid>
      </motion.div>

      {/* 메인 컨텐츠 */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.6, delay: 0.2 }}
      >
        <Card sx={{ borderRadius: 2, boxShadow: '0 2px 12px rgba(0,0,0,0.08)' }}>
          <CardContent sx={{ p: 3 }}>
            {/* 검색창 */}
            <Box mb={3}>
              <TextField
                fullWidth
                placeholder="종목명 또는 종목코드로 검색... (예: 삼성전자, 005930)"
                value={searchKeyword}
                onChange={(e) => setSearchKeyword(e.target.value)}
                onKeyDown={handleKeyDown}
                InputProps={{
                  startAdornment: (
                    <InputAdornment position="start">
                      <IconButton
                        onClick={handleSearch}
                        size="small"
                        sx={{ color: 'action.active' }}
                      >
                        <Search />
                      </IconButton>
                    </InputAdornment>
                  ),
                  endAdornment: searchKeyword && (
                    <InputAdornment position="end">
                      <IconButton
                        onClick={handleClearSearch}
                        size="small"
                        sx={{ color: 'action.active' }}
                      >
                        <Clear />
                      </IconButton>
                    </InputAdornment>
                  ),
                }}
                sx={{
                  '& .MuiOutlinedInput-root': {
                    borderRadius: 2,
                    backgroundColor: 'grey.50',
                    '&:hover': {
                      backgroundColor: 'grey.100',
                    },
                    '&.Mui-focused': {
                      backgroundColor: 'white',
                    },
                  },
                }}
              />
              {/* 검색 도움말
              <Box display="flex" alignItems="center" justifyContent="space-between" mt={1}>
                <Typography variant="caption" color="text.secondary">
                  💡 Enter키를 누르거나 검색 아이콘을 클릭하세요
                </Typography>
                {data.isSearch && (
                  <Chip
                    label={`"${searchQuery}" 검색중`}
                    size="small"
                    color="primary"
                    onDelete={handleClearSearch}
                    deleteIcon={<Clear />}
                    sx={{ fontSize: '0.75rem' }}
                  />
                )}
              </Box> */}
            </Box>

            {/* 필터링 옵션 */}
            <Box display="flex" gap={2} mb={3} flexWrap="wrap">
              <FormControl size="small" sx={{ minWidth: 120 }}>
                <InputLabel>페이지 크기</InputLabel>
                <Select
                  value={pageSize}
                  label="페이지 크기"
                  onChange={handlePageSizeChange}
                >
                  <MenuItem value={10}>10개</MenuItem>
                  <MenuItem value={20}>20개</MenuItem>
                  <MenuItem value={50}>50개</MenuItem>
                  <MenuItem value={100}>100개</MenuItem>
                </Select>
              </FormControl>

              <FormControl size="small" sx={{ minWidth: 120 }}>
                <InputLabel>정렬 기준</InputLabel>
                <Select
                  value={sortBy}
                  label="정렬 기준"
                  onChange={handleSortChange}
                >
                  <MenuItem value="name">종목명</MenuItem>
                  <MenuItem value="code">종목코드</MenuItem>
                  <MenuItem value="price">현재가</MenuItem>
                  <MenuItem value="market">시장구분</MenuItem>
                </Select>
              </FormControl>
            </Box>

            {/* 결과 헤더 */}
            <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
              <Typography variant="h6" fontWeight="600" color="#2c3e50">
                {getCurrentTitle()}
              </Typography>
              <Box display="flex" gap={1}>
                <Chip 
                  label={`${data.totalElements.toLocaleString()}개 중 ${data.stocks.length}개 표시`} 
                  color="primary" 
                  variant="outlined" 
                  size="small"
                />
                <Chip 
                  label={`${currentPage}/${data.totalPages} 페이지`} 
                  color="secondary" 
                  variant="outlined" 
                  size="small"
                />
              </Box>
            </Box>

            {/* 데이터 테이블 */}
            <Box sx={{ minHeight: '400px' }}>
              {data.stocks.length === 0 ? (
                <Box 
                  display="flex" 
                  flexDirection="column" 
                  alignItems="center" 
                  justifyContent="center" 
                  minHeight="400px"
                >
                  <Search sx={{ fontSize: 64, color: 'text.disabled', mb: 2 }} />
                  <Typography color="text.secondary" variant="h6">
                    {data.isSearch ? '검색 결과가 없습니다.' : '주식 데이터가 없습니다.'}
                  </Typography>
                  {data.isSearch && (
                    <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
                      다른 키워드로 검색해보세요.
                    </Typography>
                  )}
                </Box>
              ) : (
                <>
                  <StockTable stocks={data.stocks} />
                  
                  {/* 페이지네이션 - 디버깅 정보 추가 */}
                  {/* <Box display="flex" justifyContent="center" mt={3}> */}
                    {/* 디버깅용 정보 표시 */}
                    {/* <Box textAlign="center" mb={2}> */}
                      {/* <Typography variant="caption" color="text.secondary"> */}
                        {/* 디버깅: 현재페이지={currentPage}, 총페이지={data.totalPages}, 총데이터={data.totalElements} */}
                      {/* </Typography> */}
                    {/* </Box> */}
                  {/* </Box> */}
                  
                  {/* 페이지네이션 - 조건 완화해서 항상 보이게 */}
                  {data.totalPages >= 1 && (
                    <Box display="flex" justifyContent="center" mt={2}>
                      <Pagination
                        count={Math.max(data.totalPages, 1)} // 최소 1페이지
                        page={currentPage}
                        onChange={handlePageChange}
                        color="primary"
                        size="large"
                        showFirstButton
                        showLastButton
                        sx={{
                          '& .MuiPaginationItem-root': {
                            borderRadius: 2,
                          },
                        }}
                      />
                    </Box>
                  )}
                </>
              )}
            </Box>
          </CardContent>
        </Card>
      </motion.div>
    </Box>
  )
}

export default StockList