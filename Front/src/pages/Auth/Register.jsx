import React, { useState } from 'react'
import {
  Container,
  Paper,
  TextField,
  Button,
  Typography,
  Box,
  Alert,
  Link,
  Card,
  CardContent,
  InputAdornment,
  IconButton,
  LinearProgress,
} from '@mui/material'
import {
  PersonAdd,
  AccountCircle,
  Lock,
  Person,
  Visibility,
  VisibilityOff,
  CheckCircle,
} from '@mui/icons-material'
import { useNavigate, Link as RouterLink } from 'react-router-dom'
import { useAuth } from '../../contexts/AuthContext'

const Register = () => {
  const navigate = useNavigate()
  const { register } = useAuth()
  const [formData, setFormData] = useState({
    loginId: '',
    password: '',
    confirmPassword: '',
    userName: '',
  })
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [showPassword, setShowPassword] = useState(false)
  const [showConfirmPassword, setShowConfirmPassword] = useState(false)

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    })
    setError('') // 입력시 에러 메시지 제거
  }

  const getPasswordStrength = () => {
    const password = formData.password
    let strength = 0
    if (password.length >= 4) strength += 25
    if (password.length >= 8) strength += 25
    if (/[A-Z]/.test(password)) strength += 25
    if (/[0-9]/.test(password)) strength += 25
    return strength
  }

  const getPasswordStrengthColor = () => {
    const strength = getPasswordStrength()
    if (strength < 25) return 'error'
    if (strength < 50) return 'warning'
    if (strength < 75) return 'info'
    return 'success'
  }

  const validateForm = () => {
    if (formData.password !== formData.confirmPassword) {
      setError('비밀번호가 일치하지 않습니다.')
      return false
    }
    if (formData.password.length < 4) {
      setError('비밀번호는 4자 이상이어야 합니다.')
      return false
    }
    if (formData.loginId.length < 3) {
      setError('아이디는 3자 이상이어야 합니다.')
      return false
    }
    if (formData.userName.length < 2) {
      setError('이름은 2자 이상이어야 합니다.')
      return false
    }
    return true
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    
    if (!validateForm()) {
      return
    }

    setLoading(true)
    setError('')

    try {
      const result = await register({
        loginId: formData.loginId,
        password: formData.password,
        userName: formData.userName,
      })
      
      if (result.success) {
        setSuccess('회원가입이 완료되었습니다! 로그인 페이지로 이동합니다.')
        setTimeout(() => {
          navigate('/login')
        }, 2000)
      } else {
        setError(result.error)
      }
    } catch (error) {
      setError('회원가입 중 오류가 발생했습니다.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <Container component="main" maxWidth="sm">
      <Box sx={{ mt: 6, mb: 4, textAlign: 'center' }}>
        <Box
          sx={{
            width: 80,
            height: 80,
            borderRadius: '50%',
            background: 'linear-gradient(135deg, #4CAF50 0%, #8BC34A 100%)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            margin: '0 auto 24px',
            boxShadow: '0 8px 32px rgba(76, 175, 80, 0.3)',
          }}
        >
          <PersonAdd sx={{ fontSize: 40, color: 'white' }} />
        </Box>
        
        <Typography 
          variant="h4" 
          component="h1"
          sx={{ 
            fontWeight: 800,
            background: 'linear-gradient(135deg, #4CAF50 0%, #8BC34A 100%)',
            backgroundClip: 'text',
            WebkitBackgroundClip: 'text',
            WebkitTextFillColor: 'transparent',
            mb: 1,
          }}
        >
          회원가입
        </Typography>
        <Typography variant="body1" color="text.secondary">
          MockStock과 함께 투자를 시작하세요
        </Typography>
      </Box>

      <Card
        sx={{
          borderRadius: 4,
          boxShadow: '0 20px 60px rgba(0,0,0,0.1)',
          background: 'rgba(255,255,255,0.9)',
          backdropFilter: 'blur(10px)',
        }}
      >
        <CardContent sx={{ p: 4 }}>
          {error && (
            <Alert 
              severity="error" 
              sx={{ 
                mb: 3, 
                borderRadius: 2,
              }}
            >
              {error}
            </Alert>
          )}

          {success && (
            <Alert 
              severity="success" 
              sx={{ 
                mb: 3, 
                borderRadius: 2,
              }}
              icon={<CheckCircle />}
            >
              {success}
            </Alert>
          )}

          <Box component="form" onSubmit={handleSubmit}>
            <TextField
              margin="normal"
              required
              fullWidth
              id="loginId"
              label="아이디"
              name="loginId"
              autoComplete="username"
              autoFocus
              value={formData.loginId}
              onChange={handleChange}
              disabled={loading}
              helperText="영문, 숫자 3자 이상으로 입력해주세요"
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                    <AccountCircle color="action" />
                  </InputAdornment>
                ),
              }}
              sx={{
                mb: 2,
                '& .MuiOutlinedInput-root': {
                  borderRadius: 2,
                },
              }}
            />

            <TextField
              margin="normal"
              required
              fullWidth
              id="userName"
              label="이름"
              name="userName"
              autoComplete="name"
              value={formData.userName}
              onChange={handleChange}
              disabled={loading}
              helperText="실명 또는 닉네임을 입력해주세요"
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                    <Person color="action" />
                  </InputAdornment>
                ),
              }}
              sx={{
                mb: 2,
                '& .MuiOutlinedInput-root': {
                  borderRadius: 2,
                },
              }}
            />

            <TextField
              margin="normal"
              required
              fullWidth
              name="password"
              label="비밀번호"
              type={showPassword ? 'text' : 'password'}
              id="password"
              autoComplete="new-password"
              value={formData.password}
              onChange={handleChange}
              disabled={loading}
              helperText="4자 이상 입력해주세요"
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                    <Lock color="action" />
                  </InputAdornment>
                ),
                endAdornment: (
                  <InputAdornment position="end">
                    <IconButton
                      onClick={() => setShowPassword(!showPassword)}
                      edge="end"
                    >
                      {showPassword ? <VisibilityOff /> : <Visibility />}
                    </IconButton>
                  </InputAdornment>
                ),
              }}
              sx={{
                mb: 1,
                '& .MuiOutlinedInput-root': {
                  borderRadius: 2,
                },
              }}
            />

            {/* 비밀번호 강도 표시 */}
            {formData.password && (
              <Box sx={{ mb: 2 }}>
                <Typography variant="caption" color="text.secondary">
                  비밀번호 강도
                </Typography>
                <LinearProgress
                  variant="determinate"
                  value={getPasswordStrength()}
                  color={getPasswordStrengthColor()}
                  sx={{ height: 6, borderRadius: 3, mt: 0.5 }}
                />
              </Box>
            )}

            <TextField
              margin="normal"
              required
              fullWidth
              name="confirmPassword"
              label="비밀번호 확인"
              type={showConfirmPassword ? 'text' : 'password'}
              id="confirmPassword"
              value={formData.confirmPassword}
              onChange={handleChange}
              disabled={loading}
              error={formData.confirmPassword && formData.password !== formData.confirmPassword}
              helperText={
                formData.confirmPassword && formData.password !== formData.confirmPassword
                  ? '비밀번호가 일치하지 않습니다'
                  : '비밀번호를 다시 입력해주세요'
              }
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                    <Lock color="action" />
                  </InputAdornment>
                ),
                endAdornment: (
                  <InputAdornment position="end">
                    <IconButton
                      onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                      edge="end"
                    >
                      {showConfirmPassword ? <VisibilityOff /> : <Visibility />}
                    </IconButton>
                  </InputAdornment>
                ),
              }}
              sx={{
                mb: 3,
                '& .MuiOutlinedInput-root': {
                  borderRadius: 2,
                },
              }}
            />

            <Button
              type="submit"
              fullWidth
              variant="contained"
              size="large"
              disabled={loading}
              sx={{
                py: 1.5,
                mb: 3,
                borderRadius: 2,
                fontSize: '1.1rem',
                fontWeight: 'bold',
                background: 'linear-gradient(135deg, #4CAF50 0%, #8BC34A 100%)',
                boxShadow: '0 8px 32px rgba(76, 175, 80, 0.3)',
                '&:hover': {
                  background: 'linear-gradient(135deg, #43A047 0%, #7CB342 100%)',
                  transform: 'translateY(-2px)',
                  boxShadow: '0 12px 40px rgba(76, 175, 80, 0.4)',
                },
                transition: 'all 0.3s ease-in-out',
              }}
            >
              {loading ? '가입 중...' : '회원가입'}
            </Button>

            <Box textAlign="center">
              <Link 
                component={RouterLink} 
                to="/login" 
                variant="body2"
                sx={{
                  color: 'primary.main',
                  textDecoration: 'none',
                  fontWeight: 600,
                  '&:hover': {
                    textDecoration: 'underline',
                  },
                }}
              >
                이미 계정이 있으신가요? 로그인
              </Link>
            </Box>
          </Box>
        </CardContent>
      </Card>

      {/* 혜택 안내 */}
      <Box mt={4}>
        <Card 
          sx={{ 
            background: 'linear-gradient(135deg, #FF9800 0%, #FFB74D 100%)',
            color: 'white',
            borderRadius: 3,
          }}
        >
          <CardContent sx={{ p: 3, textAlign: 'center' }}>
            <Typography variant="h6" fontWeight="bold" gutterBottom>
              🎉 회원가입 혜택
            </Typography>
            <Typography variant="body1" sx={{ mb: 2, opacity: 0.9 }}>
              <strong>시드머니 5천만원</strong>을 즉시 지급해드립니다!
            </Typography>
            <Box sx={{ display: 'flex', justifyContent: 'center', gap: 3, flexWrap: 'wrap' }}>
              <Box textAlign="center">
                <Typography variant="h6" fontWeight="bold">
                  📈
                </Typography>
                <Typography variant="caption">
                  실시간 주가
                </Typography>
              </Box>
              <Box textAlign="center">
                <Typography variant="h6" fontWeight="bold">
                  💰
                </Typography>
                <Typography variant="caption">
                  24시간 거래
                </Typography>
              </Box>
              <Box textAlign="center">
                <Typography variant="h6" fontWeight="bold">
                  🏆
                </Typography>
                <Typography variant="caption">
                  랭킹 시스템
                </Typography>
              </Box>
            </Box>
          </CardContent>
        </Card>
      </Box>
    </Container>
  )
}

export default Register