import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080';

// axios 인스턴스 생성
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  withCredentials: true, // 세션 쿠키 포함
  headers: {
    'Content-Type': 'application/json',
  },
})

// 요청 인터셉터
apiClient.interceptors.request.use(
  (config) => {
    console.log(`🚀 API 요청: ${config.method.toUpperCase()} ${config.url}`)
    return config
  },
  (error) => {
    console.error('❌ API 요청 오류:', error)
    return Promise.reject(error)
  }
)

// 응답 인터셉터
apiClient.interceptors.response.use(
  (response) => {
    console.log(`✅ API 응답: ${response.status} ${response.config.url}`)
    return response
  },
  (error) => {
    console.error('❌ API 응답 오류:', error.response?.data || error.message)
    return Promise.reject(error)
  }
)

// API 함수들
export const api = {
  // 인증 관련
  auth: {
    login: (loginData) => apiClient.post('/api/auth/login', loginData),
    logout: () => apiClient.post('/api/auth/logout'),
    register: (registerData) => apiClient.post('/api/create', registerData),
    me: () => apiClient.get('/api/auth/me'),
    status: () => apiClient.get('/api/auth/status'),
  },

  // 대시보드
  dashboard: {
    get: () => apiClient.get('/api/dashboard'),
  },

  // 주식 관련
  stocks: {
    getAll: (page = 0, size = 20, sortBy = 'name', sortDir = 'asc') => 
      apiClient.get(`/api/stocks?page=${page}&size=${size}&sortBy=${sortBy}&sortDir=${sortDir}`),
    search: (keyword, page = 0, size = 20, sortBy = 'name', sortDir = 'asc') => 
      apiClient.get(`/api/stocks/search?keyword=${keyword}&page=${page}&size=${size}&sortBy=${sortBy}&sortDir=${sortDir}`),
    getById: (stockId) => apiClient.get(`/api/stocks/${stockId}`),
    getRandom: (count = 10) => apiClient.get(`/api/stocks/random?count=${count}`),
    buy: (data) => apiClient.post('/api/stocks/buy', data),
    sell: (data) => apiClient.post('/api/stocks/sell', data),
    // 거래량 랭킹 추가
    getVolumeRanking: () => apiClient.get('/api/stocks/rank'),
  },

  // 사용자 관련
  users: {
    getById: (userId) => apiClient.get(`/api/users/${userId}`),
    getRankings: () => apiClient.get('/api/users/rankings'),
    getAllRankings: () => apiClient.get('/api/users/rankings/all'),
    // 파산 신청 추가
    declareBankruptcy: () => apiClient.post('/api/bankruptcy'),
  },

  // 포트폴리오
  portfolio: {
    get: () => apiClient.get('/api/portfolio'),
  },

  // 관심목록 관련 (새로 추가)
  interests: {
    getMyInterests: (page = 0, size = 20) => apiClient.get(`/api/interests?page=${page}&size=${size}`),
    add: (stockId) => apiClient.post('/api/interests', { stockId }),
    remove: (stockId) => apiClient.delete(`/api/interests/${stockId}`),
    search: (keyword, page = 0, size = 20) => apiClient.get(`/api/interests/search?keyword=${keyword}&page=${page}&size=${size}`),
    check: (stockId) => apiClient.get(`/api/interests/check/${stockId}`),
    getCount: () => apiClient.get('/api/interests/count'),
    getPopularity: (stockId) => apiClient.get(`/api/interests/popularity/${stockId}`),
  },
}

export default apiClient