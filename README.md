# Chatbot 2

Spring Boot, JSP, MySQL, Spring Data JPA, Spring AI를 이용한 다중 모델 채팅 애플리케이션입니다.

## 주요 기능

- 이메일 기반 회원가입 및 로그인
- BCrypt 비밀번호 암호화
- 사용자별 대화 저장
- 이전 대화 목록 조회
- 대화 제목 수정 및 삭제
- 대화별 메시지 이력 저장
- AI 모델 선택
  - Groq(OpenAI 호환 API)
  - Google Gemini
  - NVIDIA NIM
- ChatGPT 스타일 사이드바 채팅 화면
- Enter 전송 및 Shift+Enter 줄바꿈
- AI 응답 도착 시 최근 메시지로 자동 스크롤

## 기술 스택

- Java 17
- Spring Boot 4.1
- Spring MVC
- Spring Data JPA / Hibernate
- Spring AI 2.0
- JSP / JSTL
- MySQL 8.0+
- Maven Wrapper

## 프로젝트 구조

```text
src/main/java/org/example/chatbot2/
├── chat/api          # 요청/응답 DTO
├── chat/config       # AI 모델 및 비밀번호 설정
├── chat/controller   # 채팅 REST API
├── chat/domain       # AppUser, Conversation, Message 엔티티
├── chat/repository   # JPA Repository
├── chat/service      # 인증 및 채팅 비즈니스 로직
└── web               # 로그인/홈 MVC 컨트롤러

src/main/webapp/WEB-INF/views/
├── index.jsp         # 로그인/회원가입 화면
└── chat.jsp          # 채팅 화면
```

## 사전 준비

- Java 17 이상
- MySQL 8.0 이상
- Groq, Gemini, NVIDIA NIM 중 사용할 API 키

## 데이터베이스 설정

`MySQL.sql` 파일을 MySQL에서 실행해 다음 테이블을 생성합니다.

- `app_users`: 사용자 계정
- `conversations`: 사용자별 대화
- `messages`: 대화 메시지
- `ai_usage_logs`: AI 사용량 로그

메시지가 추가되면 `trg_messages_touch_conversation` 트리거가 대화의 `updated_at`을 갱신합니다.
