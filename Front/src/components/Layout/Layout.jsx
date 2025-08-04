import React, { useState } from 'react'
import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  Box,
  IconButton,
  Menu,
  MenuItem,
  Container,
  useTheme,
  useMediaQuery,
  Drawer,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  ListItemButton,
  Avatar,
  Divider,
  Badge,
} from '@mui/material'
import {
  TrendingUp,
  AccountCircle,
  Menu as MenuIcon,
  Dashboard as DashboardIcon,
  List as ListIcon,
  AccountBalanceWallet,
  Login as LoginIcon,
  PersonAdd,
  Notifications,
  Settings,
  ExitToApp,
  AutoAwesome,
} from '@mui/icons-material'
import { useNavigate, useLocation } from 'react-router-dom'
import { useAuth } from '../../contexts/AuthContext'
import { motion } from 'framer-motion'

const Layout = ({ children }) => {
  const navigate = useNavigate()
  const location = useLocation()
  const { user, logout, loading } = useAuth()
  const theme = useTheme()
  const isMobile = useMediaQuery(theme.breakpoints.down('md'))
  
  const [anchorEl, setAnchorEl] = useState(null)
  const [mobileOpen, setMobileOpen] = useState(false)

  const handleMenu = (event) => {
    setAnchorEl(event.currentTarget)
  }

  const handleClose = () => {
    setAnchorEl(null)
  }

  const handleLogout = async () => {
    await logout()
    setAnchorEl(null)
    navigate('/')
  }

  const formatMoney = (money) => {
    return money ? money.toLocaleString() + '원' : '0원'
  }

  const navigationItems = [
    { 
      label: '대시보드', 
      path: '/', 
      icon: <DashboardIcon />, 
      gradient: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' 
    },
    { 
      label: '주식 목록', 
      path: '/stocks', 
      icon: <ListIcon />, 
      gradient: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)' 
    },
    ...(user ? [{ 
      label: '포트폴리오', 
      path: '/portfolio', 
      icon: <AccountBalanceWallet />, 
      gradient: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)' 
    }] : []),
  ]

  const authItems = user ? [] : [
    { 
      label: '로그인', 
      path: '/login', 
      icon: <LoginIcon />, 
      gradient: 'linear-gradient(135deg, #a8edea 0%, #fed6e3 100%)' 
    },
    { 
      label: '회원가입', 
      path: '/register', 
      icon: <PersonAdd />, 
      gradient: 'linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%)' 
    },
  ]

  const handleDrawerToggle = () => {
    setMobileOpen(!mobileOpen)
  }

  const drawer = (
    <Box sx={{ width: 280, height: '100%', background: 'linear-gradient(180deg, #667eea 0%, #764ba2 100%)' }}>
      {/* 드로어 헤더 */}
      <Box sx={{ p: 3, textAlign: 'center', borderBottom: '1px solid rgba(255,255,255,0.1)' }}>
        <motion.div
          initial={{ scale: 0.8, opacity: 0 }}
          animate={{ scale: 1, opacity: 1 }}
          transition={{ duration: 0.5 }}
        >
          <AutoAwesome sx={{ fontSize: 40, color: 'white', mb: 1 }} />
          <Typography variant="h6" sx={{ color: 'white', fontWeight: 700 }}>
            모의주식
          </Typography>
        </motion.div>
      </Box>

      {/* 네비게이션 메뉴 */}
      <List sx={{ px: 2, py: 2 }}>
        {[...navigationItems, ...authItems].map((item, index) => (
          <motion.div
            key={item.path}
            initial={{ x: -50, opacity: 0 }}
            animate={{ x: 0, opacity: 1 }}
            transition={{ duration: 0.3, delay: index * 0.1 }}
          >
            <ListItem disablePadding sx={{ mb: 1 }}>
              <ListItemButton
                selected={location.pathname === item.path}
                onClick={() => {
                  navigate(item.path)
                  setMobileOpen(false)
                }}
                sx={{
                  borderRadius: 2,
                  color: 'white',
                  '&.Mui-selected': {
                    background: 'rgba(255,255,255,0.15)',
                    backdropFilter: 'blur(10px)',
                    '&:hover': {
                      background: 'rgba(255,255,255,0.2)',
                    },
                  },
                  '&:hover': {
                    background: 'rgba(255,255,255,0.1)',
                  },
                }}
              >
                <ListItemIcon sx={{ color: 'white', minWidth: 40 }}>
                  {item.icon}
                </ListItemIcon>
                <ListItemText 
                  primary={item.label} 
                  primaryTypographyProps={{ fontWeight: 600 }}
                />
              </ListItemButton>
            </ListItem>
          </motion.div>
        ))}
      </List>

      {/* 푸터 */}
      <Box sx={{ position: 'absolute', bottom: 0, left: 0, right: 0, p: 2, textAlign: 'center' }}>
        <Typography variant="caption" sx={{ color: 'rgba(255,255,255,0.6)' }}>
          © 2025 모의주식 Platform
        </Typography>
      </Box>
    </Box>
  )

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
      {/* 프리미엄 헤더 */}
      <AppBar 
        position="sticky" 
        elevation={0}
        sx={{ 
          background: 'rgba(255, 255, 255, 0.95)',
          backdropFilter: 'blur(20px)',
          borderBottom: '1px solid rgba(0,0,0,0.08)',
          color: 'text.primary',
        }}
      >
        <Toolbar sx={{ px: { xs: 2, sm: 3 } }}>
          {isMobile && (
            <IconButton
              edge="start"
              onClick={handleDrawerToggle}
              sx={{ 
                mr: 2,
                background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                color: 'white',
                '&:hover': {
                  background: 'linear-gradient(135deg, #5a6fd8 0%, #6a4190 100%)',
                },
              }}
            >
              <MenuIcon />
            </IconButton>
          )}
          
          {/* 로고 */}
          <Box 
            display="flex" 
            alignItems="center" 
            sx={{ cursor: 'pointer' }}
            onClick={() => navigate('/')}
          >
            <motion.div
              whileHover={{ scale: 1.05 }}
              whileTap={{ scale: 0.95 }}
            >
              <Box
                sx={{
                  width: 40,
                  height: 40,
                  borderRadius: 2,
                  background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  mr: 2,
                  boxShadow: '0 4px 20px rgba(102, 126, 234, 0.3)',
                }}
              >
                <AutoAwesome sx={{ fontSize: 24, color: 'white' }} />
              </Box>
            </motion.div>
            <Box>
              <Typography 
                variant="h6" 
                component="div" 
                sx={{ 
                  fontWeight: 800,
                  fontSize: '1.4rem',
                  background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                  backgroundClip: 'text',
                  WebkitBackgroundClip: 'text',
                  WebkitTextFillColor: 'transparent',
                }}
              >
                모의주식
              </Typography>
            </Box>
          </Box>

          <Box sx={{ flexGrow: 1 }} />

          {/* 데스크탑 네비게이션 */}
          {!isMobile && (
            <Box sx={{ display: 'flex', gap: 1, mr: 2 }}>
              {navigationItems.map((item) => (
                <motion.div
                  key={item.path}
                  whileHover={{ y: -2 }}
                  whileTap={{ scale: 0.95 }}
                >
                  <Button
                    onClick={() => navigate(item.path)}
                    sx={{
                      px: 2,
                      py: 1,
                      borderRadius: 2,
                      fontWeight: 600,
                      color: location.pathname === item.path ? 'white' : 'text.primary',
                      background: location.pathname === item.path 
                        ? 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
                        : 'transparent',
                      '&:hover': {
                        background: location.pathname === item.path 
                          ? 'linear-gradient(135deg, #5a6fd8 0%, #6a4190 100%)'
                          : 'rgba(102, 126, 234, 0.1)',
                      },
                      transition: 'all 0.2s ease-in-out',
                    }}
                  >
                    {item.label}
                  </Button>
                </motion.div>
              ))}
            </Box>
          )}

          {/* 사용자 영역 */}
          {!loading && (
            <Box>
              {user ? (
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                  {/* 알림 아이콘 */}
                  <IconButton
                    sx={{
                      background: 'linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%)',
                      color: 'white',
                      '&:hover': {
                        background: 'linear-gradient(135deg, #ff8a95 0%, #fdbde5 100%)',
                      },
                    }}
                  >
                    <Badge badgeContent={3} color="error">
                      <Notifications />
                    </Badge>
                  </IconButton>

                  {/* 사용자 메뉴 */}
                  <Button
                    onClick={handleMenu}
                    sx={{
                      borderRadius: 3,
                      px: 2,
                      py: 1,
                      background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                      color: 'white',
                      '&:hover': {
                        background: 'linear-gradient(135deg, #5a6fd8 0%, #6a4190 100%)',
                      },
                    }}
                  >
                    <Avatar 
                      sx={{ 
                        width: 28, 
                        height: 28, 
                        mr: 1,
                        background: 'rgba(255,255,255,0.2)',
                        fontSize: '0.9rem',
                      }}
                    >
                      {user.userName?.charAt(0)}
                    </Avatar>
                    <Typography variant="body2" fontWeight="600">
                      {user.userName}
                    </Typography>
                  </Button>

                  <Menu
                    anchorEl={anchorEl}
                    open={Boolean(anchorEl)}
                    onClose={handleClose}
                    PaperProps={{
                      sx: {
                        mt: 1,
                        borderRadius: 3,
                        boxShadow: '0 20px 40px rgba(0,0,0,0.1)',
                        minWidth: 280,
                        background: 'rgba(255,255,255,0.95)',
                        backdropFilter: 'blur(20px)',
                      }
                    }}
                  >
                    {/* 사용자 정보 */}
                    <Box sx={{ p: 3, borderBottom: '1px solid rgba(0,0,0,0.06)' }}>
                      <Box display="flex" alignItems="center" mb={2}>
                        <Avatar 
                          sx={{ 
                            width: 48, 
                            height: 48, 
                            mr: 2,
                            background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                          }}
                        >
                          {user.userName?.charAt(0)}
                        </Avatar>
                        <Box>
                          <Typography variant="subtitle1" fontWeight="bold">
                            {user.userName}님
                          </Typography>
                          <Typography variant="caption" color="text.secondary">
                            프리미엄 회원
                          </Typography>
                        </Box>
                      </Box>
                      
                      <Box 
                        sx={{ 
                          p: 2, 
                          borderRadius: 2, 
                          background: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
                          color: 'white',
                        }}
                      >
                        <Typography variant="caption" sx={{ opacity: 0.9 }}>보유현금</Typography>
                        <Typography variant="h6" fontWeight="bold">
                          {formatMoney(user.availableMoney)}
                        </Typography>
                        <Box display="flex" justifyContent="space-between" mt={1}>
                          <Box>
                            <Typography variant="caption" sx={{ opacity: 0.9 }}>총자산</Typography>
                            <Typography variant="body2" fontWeight="600">
                              {formatMoney(user.totalAssets)}
                            </Typography>
                          </Box>
                          <Box textAlign="right">
                            <Typography variant="caption" sx={{ opacity: 0.9 }}>수익률</Typography>
                            <Typography variant="body2" fontWeight="600">
                              {user.returnRate >= 0 ? '+' : ''}{user.returnRate?.toFixed(2)}%
                            </Typography>
                          </Box>
                        </Box>
                      </Box>
                    </Box>

                    <MenuItem onClick={() => { navigate('/portfolio'); handleClose(); }}>
                      <AccountBalanceWallet sx={{ mr: 2 }} />
                      포트폴리오
                    </MenuItem>
                    <MenuItem onClick={handleClose}>
                      <Settings sx={{ mr: 2 }} />
                      설정
                    </MenuItem>
                    <Divider />
                    <MenuItem onClick={handleLogout} sx={{ color: 'error.main' }}>
                      <ExitToApp sx={{ mr: 2 }} />
                      로그아웃
                    </MenuItem>
                  </Menu>
                </Box>
              ) : (
                !isMobile && (
                  <Box sx={{ display: 'flex', gap: 1 }}>
                    <Button 
                      onClick={() => navigate('/login')}
                      variant="outlined"
                      sx={{ 
                        borderRadius: 2,
                        borderColor: 'primary.main',
                        color: 'primary.main',
                        '&:hover': {
                          background: 'primary.main',
                          color: 'white',
                        }
                      }}
                    >
                      로그인
                    </Button>
                    <Button 
                      onClick={() => navigate('/register')}
                      sx={{ 
                        borderRadius: 2,
                        background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                        color: 'white',
                        '&:hover': {
                          background: 'linear-gradient(135deg, #5a6fd8 0%, #6a4190 100%)',
                        }
                      }}
                    >
                      회원가입
                    </Button>
                  </Box>
                )
              )}
            </Box>
          )}
        </Toolbar>
      </AppBar>

      {/* 모바일 드로어 */}
      <Drawer
        variant="temporary"
        open={mobileOpen}
        onClose={handleDrawerToggle}
        ModalProps={{ keepMounted: true }}
        PaperProps={{
          sx: { 
            background: 'transparent',
            boxShadow: 'none',
          }
        }}
      >
        {drawer}
      </Drawer>
      
      {/* 메인 컨텐츠 */}
      <Container 
        maxWidth="xl" 
        sx={{ 
          mt: 3, 
          mb: 3, 
          flexGrow: 1,
          px: { xs: 2, sm: 3 }
        }}
      >
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6 }}
        >
          {children}
        </motion.div>
      </Container>

      {/* 프리미엄 푸터 */}
      <Box 
        sx={{ 
          background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
          color: 'white',
          py: 3,
          mt: 'auto',
        }}
      >
        <Container maxWidth="xl">
          <Box display="flex" justifyContent="center" alignItems="center">
            <Typography variant="body2" sx={{ opacity: 0.9 }}>
              모의주식 웹 프로젝트 
            </Typography>
          </Box>
        </Container>
      </Box>
    </Box>
  )
}

export default Layout