
## 프로젝트 개요

모의주식 웹사이트는 실제 주가 데이터를 기반으로 한 모의 주식 거래 시스템입니다. 사용자는 가상의 자금으로 실제 주식을 매매하며 투자 경험을 쌓을 수 있습니다.

### 주요 기능

- **🔐 사용자 인증**: 회원가입/로그인 (세션 기반)
- **💰 시드머니**: 가입시 5천만원 지급
- **📊 실시간 주가**: KRX API 연동으로 실제 주가 데이터 제공
- **💹 주식 거래**: 24시간 언제든 매수/매도 가능
- **📈 포트폴리오**: 보유 주식 현황 및 수익률 확인
- **🔖 관심목록**: 관심 있는 주식 저장 및 관리
- **🏆 랭킹 시스템**: 사용자간 수익률 순위 경쟁
- **📊 거래량 랭킹**: 실시간 거래량 상위 종목 조회
- **💸 파산 신청**: 손실시 초기 자금으로 재시작

## 🛠 기술 스택

### Backend
- **Framework**: Spring Boot
- **Language**: Java 17
- **Database**: MariaDB
- **Cache**: Redis
- **Security**: Spring Session (Cookie 기반)
- **External API**: 
  - KRX 공공데이터 API (주가 정보)
  - 한국투자증권 API (거래량 랭킹)

### Frontend  
- **Framework**: React
- **Language**: JavaScript


## 📱 주요 화면

### 대시보드
<img width="1907" height="888" alt="Image" src="https://github.com/user-attachments/assets/e12958a2-3049-40cb-90aa-9f7c922a4fc5" />

### 주식 목록  
<img width="1899" height="909" alt="Image" src="https://github.com/user-attachments/assets/90fd6a19-9c63-4aac-97c9-b0d8ff9bc320" />
### 주식 검색
<img width="1903" height="896" alt="Image" src="https://github.com/user-attachments/assets/5014792a-eaf9-4fbd-a91f-999a0d60321a" />

### 주식 상세 
<img width="968" height="824" alt="Image" src="https://github.com/user-attachments/assets/af574804-d565-41e6-a23a-f5881ddd0fcc" />
### 매수
<img width="440" height="394" alt="Image" src="https://github.com/user-attachments/assets/f2aa8e80-fe36-4f6d-9786-e5fd122d2a95" />
### 매도
<img width="447" height="383" alt="Image" src="https://github.com/user-attachments/assets/4af0f24d-7dc9-4ed2-9e82-ace57602339c" />

### 포트폴리오
<img width="1434" height="626" alt="Image" src="https://github.com/user-attachments/assets/4b7e6221-382b-457f-9c3c-5d8c2d398b2e" />

## 주요 API 엔드포인트

### 인증
- POST /api/create - 회원가입
- POST /api/auth/login - 로그인
- POST /api/auth/logout - 로그아웃
- GET /api/auth/me - 현재 사용자 정보

### 주식
- GET /api/stocks - 주식 목록
- GET /api/stocks/search - 주식 검색
- GET /api/stocks/{stockId} - 주식 상세
- POST /api/stocks/buy - 매수
- POST /api/stocks/sell - 매도

### 포트폴리오
- GET /api/portfolio - 내 포트폴리오
- POST /api/bankruptcy - 파산 신청

### 관심목록
- GET /api/interests - 내 관심목록
- POST /api/interests - 관심목록 추가
- DELETE /api/interests/{stockId} - 관심목록 삭제

### 사용자
- GET /api/users/rankings - 사용자 랭킹

