# BD-Assignment2

## 목차
### 1. [요구사항](#-1.요구사항)
### 2. [좋아요 기능 구현](#-2.좋아요-기능-구현)
### 3. [실시간 저장 기능 구현](#-3.실시간-저장-기능-구현)

# 1.요구사항

### 필수 요구사항

- 회원가입
- 게임 제작
    - 프로젝트는 실시간으로 반영이 되어야 합니다
        - 프로젝트 수정 중 의도치 않은 사이트 종료 시에도 작업 내역은 보존되어야 합니다
- 게임 출시하기
    - **프로젝트 당 퍼블리싱 할 수 있는 개수는 하나**입니다.
    - 퍼블리싱한 게임은 수정할 수 있어야 하며, 수정 후 재출시시 기존에 퍼블리싱된 게임도 수정됩니다
    - 출시하는 게임은 다른 사용자들도 볼 수 있으며, 사용자들의 **조회수 / 좋아요 등을 기록**할 수 있어야 합니다
    - '게임 혹은 사용자 **검색**'을 통해서 찾을 수 있어야 합니다

### 개발 요구사항

**아래의 문제를 풀어야 합니다.**

- `참고 - 문제 1,2번은 필수 문제이며, 3번은 선택입니다`
    
    `문제 1. '회원가입'부터 '게임 출시'까지 필요한 테이블을 설계하세요`
    
    `문제 2. 다음에 필요한 API를 설계하세요`	
    
    `1) 게임 제작 API`
    
    `2) 조회수 수정, 좋아요 API`
    
    `3) 게임/사용자 검색 API`
    
    `- option -`
    
    `문제 3.`
    
    `(1) 프로젝트 실시간 반영을 위한 Architecture를 설계하세요( 그림이 있다면 좋습니다 )`
    
    `(2) 위의 Architecture를 토대로 기능을 구현하세요`

# 2.좋아요 기능 구현

## 요구사항 및 구현

- 유저는 게임에 좋아요를 누를 수 있음
  - 한 사용자는 여러 게임에 좋아요 가능
  - 한 게임에 여러 사용자가 좋아요 가능

  → N : N 관계

  → 1:N 대 N:1로 매핑할 중간 테이블(Heart) 생성


### 다대다 관계를 피해야하는 이유?

- 데이터의 중복이 발생할 수 있음

### 식별, 비식별?

- Heart 테이블은 userId와 gameId를 외래키로 갖게됨
- 식별 관계
  - 부모 테이블의 기본키를 자식 테이블이 기본키로 사용
  - 자식 데이터가 존재하면 부모 데이터가 반드시 존재
  - 부모 테이블이 자식 테이블에 종속

  - 데이터베이스에서 데이터 정합성 검증 가능
  - 수정된 요구사항 반영이 어려움

- 비식별 관계
  - 부모 테이블의 기본키를 자식 테이블이 외래키로 사용
  - 별도의 인조키를 생성해서 기본키로 사용
  - 자식 데이터가 독립적으로 존재 가능
  - 부모와의 의존성을 줄여 조금 더 자유로운 데이터 생성, 수정 가능

  - 데이터 무결성 보장 x
  - 수정된 요구사항 반영이 유동적

### 궁금한점

식별, 비식별 관계를 성능 측면에서 고려

- 단일 칼럼으로 조회
  - 유저가 좋아요를 누른 모든 게시글 조회
  - 어떤 게시글에 좋아요를 누른 모든 사용자 조회
- 복합키 또는 두개의 칼럼으로 조회
  - 어떤 유저가 어떤 게시글을 조회했을때 이미 좋아요를 누른 게시글인지 조회

# 3.실시간 저장 기능 구현

## 요구사항 및 구현

- 유저가 프로젝트를 수정할 때 실시간으로 저장이 가능해야함
  - websocket을 이용한 실시간 통신 구현
- 구현 아키텍처
  - 클라이언트에서 프로젝트를 수정할 때마다(또는 일정 시간마다) websocket으로 수정사항 전송
  - 서버는 전달받은 수정사항을 바로 DB에 저장하지 않고 캐시 메모리에 저장
  - 일정 시간마다 DB에 저장하거나, websocket 연결이 끊기는 것을 감지했을 때 DB에 저장

## http vs websocket

- http
  - 클라이언트 request → http request 연결 생성 → 서버 response → 연결 해제
  - 반이중 통신
- http를 이용한 실시간 통신
  - polling : 클라이언트가 서버로 계속 request를 보냄
    - 지속적인 요청 때문에 서버의 부담이 큼
    - http request connection을 위한 부담과 시간이 오래 걸림
    
  - long polling : 클라이언트가 요청을 보내고 기다리다가 서버에서 이벤트가 발생하면 응답을 보냄
    - polling보다는 서버의 부담이 줄지만 클라이언트로 보내는 이벤트의 시간간격이 좁으면 polling과 별 차이가 없음
    
  - streaming : 클라이언트가 요청을 보내고 기다리다가 서버에서 이벤트가 발생하면 응답을 보내지만 연결을 끊지 않고 메시지를 계속 보낼수 있음(Flush)
    - polling, long polling과 달리 요청을 계속 보내지 않아도 되고 http request 연결도 다시 하지 않아도 됨
    
- websocket
  - 클라이언트가 접속 요청을 하고 서버가 응답한 후 연결을 끊지 않고 계속 유지
  - 실시간, 변경 사항의 빈도가 잦고, 짧은 대기 시간, 고주파수, 대용량의 경우 적합
  
## simple websocket

- `WebSocketHandler`
  - `ConcurrentHashMap` : 세션을 저장할 캐시 메모리, 프로젝트 수정사항을 저장할 캐시 메모리
  - `afterConnectionEstablished` : websocket 연결 직후 동작
  - `handleTextMessage` : websocket으로 전달받은 메시지 처리
    - 메시지에 포함된 수정사항 캐시 메모리에 저장
  - `afterConnectionClosed` : websocket 연결 종료 후 동작
    - 캐시 메모리에 저장된 수정사항 db에 반영
- `WebSocketConfig`  : websocket을 위한 url로 들어오는 요청을 처리할 핸들러 등록

→ 세션을 직접 관리해야 한다는 단점

## STOMP websocket

MessageBroker를 이용한 pub, sub 구조의 채널링 가능

- `StompWebSocketConfig`
  - `configureMessageBroker`  : /pub, /sub으로 들어오는 요청 브로커에 등록
    - pub 요청은 특정 채널로 메시지 전송
    - sub 요청은 채널에 전송된 메시지 수신
  - `registerStompEndpoints` : stomp를 위한 url(/ws) 등록
- `MessageController`
  - `@MessageMapping` 으로 등록한 함수로 메시지 처리
  - 수정사항을 여기서 처리하도록

### websocket 통신의 jwt 구현

- HTTP 요청에서 jwt 인증을 구현하기 위해 interceptor에서 처리해주었음
- websocket 요청은 interceptor를 거치지 않음

  → websocket 요청을 가로챌 interceptor가 필요

- `StompHandler`  : ChannelInterceptor 상속
  - `preSend()` 에서 `StompHeaderAccesor` 를 이용해 매 요청마다 헤더의 토큰을 검사할 수 있음
- `StompWebSocketConfig` 의 `configureClientInboundChannel` 에서 핸들러 등록


### 문제점

- jwt 인증
  - `StompHandler` 에서 토큰을 이용한 인증 로직을 처리
- 캐시 메모리 업데이트
  - `MessageController`에서 구현
  - 캐시를 수정할 때 토큰을 이용해서 프로젝트의 주인이 맞는지 검사해야함
  - `MessageController`에서 `@Header`를 통해 메시지 헤더에 접근할 수 있는 것 같음
    - Apic이라는 websocket 테스트 도구로 테스트하다보니 SEND 메시지에 헤더를 붙일 수 없어서 JWT 인증 불가
- 수정 사항 DB 반영
  - `StompHandler의 preHandle()`에서 구현
  - WebSocket 연결이 `DISCONNECT` 되었을 때 해당 세션에서 작업하던 project를 캐시 메모리에서 찾아서 DB에 반영
    - 마찬가지로 테스트 도구에서 DISCONNECT 메시지에 헤더를 붙일 수 없어서 JWT 인증 불가
    - 클라이언트에서의 일방적인 DISCONNECT로 인해 어느 프로젝트를 편집하던 창이 종료되었는지 확인 불가
      - 원래는 메시지 내용에 projectId가 포함되었는데 테스트 도구에서 DISCONNECT에는 메시지를 못붙임
      - 방법 1
        - 모든 메시지의 헤더에는 세션ID가 포함됨
        - 세션ID와 프로젝트ID를 매핑하는 HashMap 관리
        - DISCONNECT로 세션이 종료되면 HashMap에서 프로젝트ID를 받아옴
        - 프로젝트ID로 캐시 메모리에서 프로젝트 수정사항을 가져와 DB에 저장
      - 방법 2
        - 헤더에 프로젝트ID를 포함시켜서 헤더값으로 확인

## 이후에 해볼 수 있는 것

- 메시지 큐를 이용한 메시지 브로커 대체

- ConcurrentHashMap으로 구현한 캐시 메모리 Redis로 대체