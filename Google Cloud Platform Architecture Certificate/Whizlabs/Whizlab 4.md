1. 자주 업데이트 되는 환경에서는 Instance Template를 쓰지 않는 것이 좋다. 이미 배포된 후에는 update할 수 없고, 새로 만들어야 하기 때문이다.  

2. Online chat 구현시 필요한 사항으로는 Session Affinity와 Websocket이 있다. 채팅 어플리케이션을 개발하려면, 클라이언트와 App Engine 사이에 permanent Connection이 이루여 져야하기 때문이다. 이는 WebSocket을 통해 이루어지고, "upgrade" 헤더를 통해 HTTP 요청으로 시작되는 전이중 통신을 가능하게 한다.  

3. Cloud Tasks는 Queue를 사용하여 작업을 인코딩하고, 실행하는 비동기 작업 실행 서비스이다. (서버리스 모듈)  

4. Default network를 삭제하면 VM을 새로 생성할 수는 없지만, Cloud function과 Storage Bucket은 새로 생성할 수 있다.  

5. Cloud NAT는 주로 인스턴스가 인터넷에 도달할 수 있도록 한다. 

6. Workload Identity는 GKE 애플리케이션이 다른 Google Cloud 서비스를 인증하고 이용하는 새로운 방법이다.  
