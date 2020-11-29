 K8S 기반의 데이터베이스(stateful) 서비스 운영 팁
## 경험 1. 컨테이너 Probe의 비활성화 또는 제거
컨테이너 probe : kubelet에 의해 주기적으로 수행되는 진단.  
Stateful인 DB서비스에는 Container probe는 맞지 않은 기능. ( 재시작 하는 것 자체가 문제)  DB서비스에서는 Container probe가 매우 취약한 구조
 

## 경험 2. Kubectl exec로 장기간 수행되는 명령
백업, 압축, (오브젝트 스토리지) 업로드 등에서 장기간 kubectl exec로 수행중 정상종료가 되지 않음 (kubectl 연결이 끊기는 현상이 발생(1시간 이상 작업시), 네트워크가 불안정하거나, 사용량이 많으면 끊길 수 있음.)
—> 비동기 방식으로 구현을 하는 방식이 가장 이상적


## 경험 3. PodAffinity / taint 설정
노드에 Master/slave pod 배치를 단일 노드에 위치하지 않도록 설정

## 경험 4. K8s 버전 업그레이드 대응
DB 서비스 중단 시간을 최소화하기 위한 대응절차
* 초기 전략 : 다운타임 1~2시간(최대 8시간까지) / 고객과 많은 협의가 필요함
	* 서비스 중단 -> k8s 마스터 업글 -> k8s 워커노드 업글 -> 서비스 구동
* 중기 : 1시간 이내 
	* k8s 마스터 업글 -> k8s 워커노드 주문 -> 기존 노드 cordon 처리 -> pod delete
* Latest : Image polling 시간 줄이기 (노드마다 한번씩 수행)
	* k8s 마스터 업글 -> k8s 워커노드 주문 -> 기존 노드 cordon 처리 -> 임시 pod 구동, - pod delete


## 경험 5. Pod 구동중에 PVC Failed mount
Node에서 Disk(Block storage)와 mount가 해제되지 않음
—> 직접 노드 구동하거나, 동일 노드에 스케쥴하는 것이 빠른 해결임
-> 해결책 : 해당 노드에 스케쥴되도록 처리하거나, 노드 인스턴스를 리부팅 / 리로드


## 경험 6. CPU Steal time
DB 사용량에 대비하여 CPU 사용량이 비정상적으로 높은 경우
Steal time : 클라우드 멀티 테넌트 환경(물리적인 노드를 가상의 머신이 같이 쓰고있는 환경)에서 발생하는 대표적인 현상으로 하이퍼 바이저가 다른 가상 프로세서를 서비스하는 동안 가상 CPU가 실제 CPU를 기다리는 시간이다. ( Steal time is the percentage of time a virtual CPU waits for a real CPU while the hypervisor is servicing another virtual processor)

—> Host migration ( Virtual machine을 다른 host로 이주) 시키는 방법으로 해결 가능함. : 현재 노드를 물리적으로 다른 장비에 배치해야 함으로 서비스 다운타임이 존재함.



## 경험 7. DiskPressure = Pod Restart
특정 쿼리만 돌리면 DB가 죽는(재구동되는) 현상이 나타남. ==> DB에 join이나 sort를 하면 temp 영역의 disk가 필요함.(temp = overlay filesystem의 disk를 사용함(노드의 디스크))
Join, sort, full table scan등 (Worst) SQL로 인한 disk pressure 발생
빠른 해결 : 쿼리 튜닝
임시 해결책 : overlay 영역 디스크 임계치 실시간 모니터링 및 쿼리 강제중지
해결책 : Temp space를 별도의 스토리지 사용 및 설정


## 경험 8. K8s에 맞는 Failover(HA) manager
HA 도구에서는 k8s에서 완벽하게 동작하지 않음
Customaized  HA Manager의 Mechanism
—> POD status 상태를 체크한 후에 Failover를 수행

* Failover(HA) manager 메커니즘
	* Pod 내부에서 서비스를 점검한다.
	* Pod에서 주기적으로 정상정보를 etcd에 보낸다.(TTL = 10/default)
	* Failover(HA) manager는 etc 정보를 기준으로 판단한다.
	* 만약 5회 실패라고 인지하면 Pod status를 확인한다.
	* **Pod status가 Running 상태가 아니면, Failover를 수행한다.**
HA manager에서 Kubectl get pod를 사용하지 않고, etcd를 참조하는 방향으로 구성


## 경험 9. Failback 수행 시 서비스 구동 정지
Failback 수행할 경우에 서비스 중단 발생 ( 현재 구조로서는 중단은 어쩔수 없음)
* 초기 전략 : Data 크기에 따라 서비스 중단 시간 발생 ( 주말 10시간 경험)
	* 트랜잭션 유입 방지 -> 현재 데이터 백업 -> 서비스 중단 -> 데이터 복원 -> 서비스 구동
* 중기 : 스냅샷을 확용하여 복원 시간을 단축 (700GB의 경우 스냅샷으로 1시간 복구) 
	* 서비스 중지 -> 스냅샷 -> 디스크 주문(스냅샷 기반) -> PV/PVC 재생성 -> 서비스 구동
* Latest : 현재 PVC를 변경해서 서비스를 구동하고 Slave 복구하는 방법(10분 이내)
	* 서비스 중지 -> PV속성 변경(Delete -> Retain) -> PV/PVC 재생성 -> 서비스 구동 -> Slave 복구(서비스 다운타임 아님 - snaphost 활용 가능)

 
## 경험 10. 블록 스토리지 성능 저하 문제
Block Storage IP 대역이 존재하고, 방화벽을 거쳐서 Disk I/O가 가능
스토리지 트래픽은 다른 트래픽요청과 격리되어야 하며, 방화벽 및 라우터를 통과하지 않도록 해야함  —> PVC에서 사용하는 네트워크 I/O도 포함됨.





