# 🔨 Whack-A-Mole (두더지 대작전) - Android Game

> Google Firebase Realtime Database 기반 실시간 다중 유저 랭킹 시스템이 탑재된 안드로이드 아케이드 게임 애플리케이션입니다.

---

## 📌 1. Project Overview (프로젝트 개요)
- **개발 환경:** Android Studio, Java (Android Gradle Plugin 7.4.1)
- **플랫폼:** Android Mobile
- **주요 목적:** 아케이드 미니 게임에 실시간 데이터베이스 파이프라인을 구축하여 다중 유저 간 점수 동기화 및 글로벌 Top 3 랭킹 시스템 구현

---

## 🚀 2. Key Engineering & Contributions (핵심 구현 및 트러블슈팅)

### ① Database Migration: SQLite → Firebase Realtime Database
- **배경 및 한계:** 
  초기 프로토타입에서는 단말기 내장형 로컬 DB인 **SQLite**를 사용했으나, 데이터가 개별 기기에만 격리 저장되어 여러 사용자가 실시간으로 순위를 겨루는 멀티플레이어 환경을 지원하지 못하는 병목에 직면했습니다.
- **아키텍처 개선:** 
  Google **Firebase Realtime Database(NoSQL)**로 데이터 파이프라인을 전면 마이그레이션했습니다. 이를 통해 온라인 서버 기반으로 점수를 실시간 송수신하고, 중앙 집중형 데이터 관리를 완성했습니다.
- **실시간 정렬 쿼리 구현:**
  `scoresRef.orderByValue().limitToLast(3)` 쿼리를 설계하여, 전체 데이터 중 최고 득점자 1~3위를 실시간으로 추출하고 UI(RankingActivity)에 즉시 렌더링하도록 구축했습니다.

### ② Game Loop & UI Concurrency
- `Handler`와 `postDelayed(Runnable, 30ms)`를 활용한 0.03초 주기 비동기 타이머 루프를 구성했습니다.
- 메인 UI 스레드의 블로킹을 방지하면서 두더지 무작위 출몰(`View.VISIBLE`/`INVISIBLE`), 잔여 시간 카운트다운, 스코어 텍스트뷰를 끊김 없이 렌더링했습니다.

### ③ Audio Stream Optimization
- 짧은 지연시간과 빠른 응답이 요구되는 버튼음 및 타격 효과음은 경량 사운드 엔진인 `SoundPool`로 분리하고, 별도 싱글톤 클래스(`SoundPlayer.java`)로 모듈화했습니다.
- 장시간 반복 재생되는 배경음악(BGM)은 `MediaPlayer`로 이원화하여 오디오 버퍼 충돌과 메모리 누수를 방지했습니다.

### ④ Data Integrity & Fallback Logic
- 사용자가 닉네임을 입력하지 않고 플레이할 경우를 대비하여, `SimpleDateFormat` 기반의 현재 타임스탬프(`???yyyy-MM-dd HH:mm:ss`)를 대체 고유 키(Fallback Key)로 자동 생성하는 예외 처리 로직을 적용해 NoSQL 트리 구조 내 데이터 유실 및 덮어쓰기를 방지했습니다.

---

## 🛠 3. Tech Stack
- **OS:** Android (Min SDK 21 / Target SDK 33)
- **Language:** Java
- **Database:** Google Firebase Realtime Database
- **Concurrency & UI:** Android Handler, Runnable
- **Media Engine:** SoundPool, MediaPlayer

---

## 📂 4. Project Structure
```text
WhackAMole/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/whackamole/
│   │       │   ├── MainActivity.java     # 유저 닉네임 입력 및 시작 제어
│   │       │   ├── GameActivity.java     # 두더지 출몰 및 타격 게임 루프 (Handler)
│   │       │   ├── ResultActivity.java   # 플레이 결과 및 Firebase DB 전송
│   │       │   ├── RankingActivity.java  # 실시간 Top 3 랭킹 쿼리 및 뷰
│   │       │   └── SoundPlayer.java      # SoundPool 기반 효과음 모듈
│   │       └── res/
│   │           ├── layout/               # 액티비티 레이아웃 XML
│   │           └── raw/                  # 사운드 리소스 (bgm, hit, button)
│   └── build.gradle                      # Firebase Dependencies & SDK 설정
├── build.gradle                          # Top-level Gradle config
└── settings.gradle