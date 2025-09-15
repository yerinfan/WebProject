# 💬 비동기 통신 웹 프로젝트 - Web

이 레포지토리는 **비동기 채팅/메모 웹 애플리케이션의 프론트엔드** 부분을 담당합니다.  
실시간 소통을 지원하는 UI와 Ajax 기반의 비동기 기능을 제공합니다.

---

## 🚀 프로젝트 개요
- **목표**: 실시간 채팅과 메모 기능을 통합한 웹 애플리케이션 구현
- **특징**:
  - Ajax 기반 비동기 통신
  - WebSocket(SockJS + STOMP)을 활용한 실시간 채팅
  - UI 단일화 (로그인/회원가입/채팅방 등)

---

## 🛠️ 기술 스택
- **Frontend**: HTML5, CSS3, JavaScript(ES6)
- **Template Engine**: Thymeleaf
- **Library/Framework**: Bootstrap, jQuery
- **Real-time**: SockJS, STOMP

---

<img width="1264" height="740" alt="architecture" src="https://github.com/user-attachments/assets/d96ff257-740f-43ad-96aa-f2b1960a9d6e" />

---

## 📂 주요 기능
- 회원가입 및 로그인 화면
- 채팅방 입장/퇴장
- 실시간 메시지 송수신
- 이미지 업로드 기능
- 메모 작성 및 Ajax 기반 CRUD
- 관리자 페이지 (권한별 접근 제어)

---

## 📸 스크린샷
> (여기에 UI 캡처 이미지를 넣어주세요)

---

## ⚙️ 실행 방법
1. 서버 레포지토리(Spring Boot) 실행
2. 웹 애플리케이션 빌드 후 접속
   ```bash
   http://localhost:8888
