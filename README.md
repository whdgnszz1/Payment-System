# Payment System - Docker 실행 가이드

## 📋 개요

이 프로젝트는 Docker Compose를 사용하여 전체 Payment System을 컨테이너로 실행할 수 있습니다.

## 🏗️ 아키텍처

- **membership-service**: Spring Boot 기반 회원 관리 서비스
- **mysql**: MySQL 8.0 데이터베이스
- **axon-server**: Axon Framework 이벤트 스토어

## 🚀 빠른 시작

### 1. 전체 시스템 실행

```bash
./docker-run.sh
```

### 2. 개별 명령어로 실행

```bash
# 1. Docker 이미지 빌드
./docker-build.sh

# 2. 서비스 시작
docker-compose up -d

# 3. 서비스 상태 확인
docker-compose ps

# 4. 로그 확인
docker-compose logs -f membership-service
```

### 3. 시스템 정지

```bash
./docker-stop.sh
```

## 🔗 접속 정보

| 서비스             | URL                                         | 설명               |
| ------------------ | ------------------------------------------- | ------------------ |
| Membership Service | http://localhost:9000                       | 회원 관리 API      |
| Swagger UI         | http://localhost:9000/swagger-ui/index.html | API 문서           |
| Axon Server        | http://localhost:8024                       | 이벤트 스토어 관리 |
| MySQL              | localhost:3306                              | 데이터베이스       |

## 📊 서비스 상태 확인

### Health Check

```bash
# Membership Service
curl http://localhost:9000/actuator/health

# MySQL 연결 확인
docker-compose exec mysql mysql -u root -prootpassword -e "SHOW DATABASES;"
```

### 로그 확인

```bash
# 모든 서비스 로그
docker-compose logs

# 특정 서비스 로그
docker-compose logs membership-service
docker-compose logs mysql
docker-compose logs axon-server

# 실시간 로그 확인
docker-compose logs -f membership-service
```

## 🛠️ 개발 모드

### 코드 변경 후 재배포

```bash
# 1. 서비스 정지
docker-compose stop membership-service

# 2. 이미지 재빌드
./docker-build.sh

# 3. 서비스 재시작
docker-compose up -d membership-service
```

### 데이터베이스 초기화

```bash
# 볼륨과 함께 완전 정리
docker-compose down -v

# 다시 시작
./docker-run.sh
```

## 🔧 설정 파일

### 환경 변수 (.env)

```env
MYSQL_ROOT_PASSWORD=rootpassword
MYSQL_DATABASE=payment
MYSQL_USER=test
MYSQL_PASSWORD=test
TZ=Asia/Seoul
```

### Docker Compose 주요 설정

- **포트 매핑**:
    - membership-service: 9000 → 9000
    - mysql: 3306 → 3306
    - axon-server: 8024, 8124 → 8024, 8124
- **볼륨**: MySQL 데이터, Axon Server 데이터 영구 저장
- **네트워크**: payment_system 브리지 네트워크
- **Health Check**: 모든 서비스에 헬스체크 설정

## 🐛 문제 해결

### 포트 충돌

```bash
# 사용 중인 포트 확인
lsof -i :9000
lsof -i :3306
lsof -i :8024

# 기존 프로세스 종료 후 재시작
```

### 컨테이너 상태 확인

```bash
# 컨테이너 상태
docker-compose ps

# 컨테이너 내부 접속
docker-compose exec membership-service bash
docker-compose exec mysql bash
```

### 이미지 재빌드

```bash
# 캐시 없이 완전 재빌드
docker build --no-cache -t membership-service:0.0.1-SNAPSHOT -f membership-service/Dockerfile .
```

## 📝 API 테스트

### 회원 등록

```bash
curl -X POST http://localhost:9000/membership/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "홍길동",
    "email": "hong@example.com",
    "address": "서울시 강남구"
  }'
```

### 회원 조회

```bash
curl http://localhost:9000/membership/{membershipId}
```

## 🔄 업데이트

### 새 버전 배포

1. 코드 변경
2. `./docker-build.sh` 실행
3. `docker-compose up -d membership-service` 실행

### 전체 시스템 업데이트

1. `./docker-stop.sh` 실행
2. 코드 변경
3. `./docker-run.sh` 실행
