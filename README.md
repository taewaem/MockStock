# 모의 주식 웹 사이트


## 📋 프로젝트 개요
캡스톤 프로젝트로 개발된 주식 정보 조회 및 관심종목 관리 웹 애플리케이션입니다.
사용자는 주식의 종가 정보를 조회하고, 관심종목을 등록하여 관리할 수 있습니다.

## 🛠 기술 스택
- **Backend**: Spring Boot 3.3.3
- **Language**: Java 17
- **Database**: MariaDB
- **Template Engine**: Thymeleaf
- **ORM**: Spring Data JPA
- **Build Tool**: Gradle
- **External APIs**: KIS API, KRX API

## 🏗 프로젝트 구조

src/main/java/Capstone/Third/
├── api/                    외부 API 연동
│   ├── kis/                한국투자증권 API
│   └── krx/                한국거래소 API
├── user/                   사용자 관리
├── stock/                  주식 정보 관리
├── interest/               관심종목 관리
├── userStock/              사용자-주식 연관 관리
├── term/                   금융용어 관리
└── ThirdApplication.java   메인 애플리케이션


## 📦 주요 기능
### 1. 사용자 관리
- 회원가입 및 로그인
- 사용자 정보 관리
- 세션 기반 인증

### 2. 주식 정보 조회
- 실시간 주식 정보 조회
- KIS API를 통한 주식 데이터 연동
- KRX API를 통한 거래소 정보 연동

### 3. 관심종목 관리
- 사용자별 관심종목 등록/삭제
- 관심종목 목록 조회

### 4. 금융용어 사전
- 주식 관련 용어 조회
- 용어 정의 및 설명 제공

## 🗂 엔터티 구조
- **User**: 사용자 정보
- **Stock**: 주식 정보
- **Interest**: 사용자 관심종목
- **UserStock**: 사용자-주식 매핑
- **Term**: 금융용어

## 📝 API 엔드포인트
- `/`: 메인 홈페이지
- `/users`: 사용자 관리
- `/stocks`: 주식 정보 조회
- `/interests`: 관심종목 관리
- `/login`: 로그인

## 🔗 참고 자료
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/)
- [Spring Data JPA Documentation](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Thymeleaf Documentation](https://www.thymeleaf.org/documentation.html)
