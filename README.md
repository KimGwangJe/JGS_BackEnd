**📅 프로젝트 기간**
2024년 8월 6일 ~ 2024년 10월 22일

</br>

**😎 개발 인원 (역할)**
2명 (프론트 1명, 백엔드 1명)

</br>

**🛠️ 개발 스택**
Java, SpringBoot 3.x.x, Spring Data JPA, Spring Security, Redis, MySQL, JWT, OAuth2, QueryDSL

</br>

### 📖 프로젝트 소개

풋살 및 조기축구 팀들을 위한 서비스입니다.

사용자는 자신의 팀을 생성하거나 다른 사람의 팀에 가입할 수 있습니다. 커뮤니티 기능을 통해 팀을 홍보하고, 팀에 가입 신청을 받을 수 있습니다. 사용자는 자신의 팀을 대표하여 매치 글을 게시할 수 있고, 다른 팀들이 이 글을 보고 매치를 신청할 수 있습니다.

기존 타 서비스에서는 팀 간의 실력을 고려하지 않아 실력이 비슷한 팀끼리의 매치 성사에 어려움이 있었고, 시스템 내부에서 연락을 주고받을 방법이 없었습니다. 
이를 개선하기 위해 레이팅 시스템을 도입해 실력이 비슷한 팀끼리 매치할 수 있도록 하였으며, 실시간 채팅 기능을 추가해 팀 간 소통을 지원했습니다.

이 서비스를 통해 팀들은 더 공정한 매치를 경험하고, 내부 소통을 통해 매치 협의와 운영이 원활해졌습니다.

### 🙋‍♂️ 역할
1) OAuth2를 활용하여 카카오, 구글, 네이버 계정으로의 소셜 로그인을 구현했습니다. 이를 통해 사용자는 자신이 선호하는 외부 플랫폼을 이용하여 손쉽게 로그인 할 수 있도록 하였습니다.

2) Spring Security와 JWT를 통해 사용자 인증을 구현하였습니다.

3) Server-Sent Events (SSE)를 사용하여 실시간 알림 기능을 추가했습니다. 특정 이벤트가 발생하면 SSE를 통해 실시간으로 알림을 전달하였습니다.

4) Firebase FCM을 활용해 채팅 알림을 발송하고, 사용자가 알림을 클릭하면 특정 작업이 실행되도록 구현했습니다.

5) WebSocket과 STOMP를 활용해 사용자 간의 실시간 채팅 기능을 구현했습니다. 이때 Redis를 메시지 브로커로 사용하여 안정적으로 채팅 메시지를 전송할 수 있도록 했습니다.

6) QueryDSL을 활용하여 복잡한 쿼리를 높은 가독성과 타입 안정성을 보장하는 코드로 작성했습니다.

7) 데이터 조회 시 반환 값으로 DTO를 사용하여 불필요한 필드를 제외함으로써 성능을 개선했습니다.

### 👨🏼‍💻 성과 & 배운점
1) 실시간 상호작용 및 알림 시스템 구축
SSE와 FCM, WebSocket을 활용해 실시간 알림과 채팅 기능을 구현하며 사용자 경험을 크게 개선했습니다. 특히 Redis를 메시지 브로커로 사용해 시스템의 안정성과 확장성을 확보했습니다. 이를 통해 실시간 데이터 전송 기술에 대하여 이해할 수 있었습니다.

2) 보안과 인증에 대한 경험
OAuth2를 통해 외부 계정을 연동하면서 사용자 편의성과 보안성을 동시에 고려한 설계 경험을 할 수 있었습니다.

3) 데이터 처리 성능 최적화
QueryDSL과 DTO를 활용해 데이터 조회 시 성능을 최적화했습니다. 복잡한 쿼리를 효율적으로 처리하며 불필요한 데이터 반환을 줄여 서버 부하를 최소화했습니다. 이를 통해 대규모 데이터 처리와 성능 튜닝에 대한 감각을 익혔습니다.
