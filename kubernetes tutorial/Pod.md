
## 파드
### 파드의 개념
쿠버네티스는 파드라는 단위로 컨테이너를 묶어서 관리하므로, 보통 컨테이너 하나가 아닌 여러개의 컨테이너로 구성된다.( 컨테이너를 직접 관리하지 않고, 파드 단위로 관리한다.) 파드 하나에 속한 컨테이너들은 모두 노드 하나 안에서 실행된다.(여러 노드에 흩어져서 실행되는 일은 없다.) 파드의 역할 중 하나가 컨테이너들이 같은 목적으로 자원을 공유하는 것이므로 가능한 일이다.  

![pod&container](https://en.wikipedia.org/wiki/File:Pod-networking.png)
  
파드 안에 컨테이너 들이 있고, 파드 하나 안에 있는 컨테이너들은 IP 하나를 공유한다. 즉, 외부에서 파드에 접근할 때는 pod에 부여된 IP로 접근하며, 파드 안 컨테이너와 통신할 때는 컨테이너마다 다르게 설정한 포트를 사용한다.  
  
일반적으로, 컨테이너 하나에 여러가지 프로세스를 실행하도록 설정하지 않는다(컨테이너의 관리효율이 낮음)  

### 파드 사용하기
아래는 파드의 템플릿 설정의 예이다. (pod/pod-sample.yaml)

~~~
apiVersion: v1
kind: Pod
metadata:
  name: kubernetes-simple-pod                     ## 1
  labels:
    app: kubernetes-simple-pod                    ## 2
spec:
  containers:
  - name: kubernetes-simple-pod                   ## 3
    image: arisu1000/simple-container-app:latest  ## 4
    ports:
    - containerPort: 8080                         ## 5
~~~
  
1 .metadata.name 필드는 파드 이름을 설정함  
2 .metadata.labels.app 필드는 오브젝트를 식별하는 레이블을 설정함.  
3 .spec.containers[].name 필드는 컨테이너의 이름을 설정함. name 앞에 설정한 '-'은 .spec.containers의 하위필드를 배열 형태로 묶겠다는 뜻  
4 .spec.containers[].image 필드는 컨테이너에서 사용할 이미지를 정함. 
5 .spec.containers[].ports[].containerPort 필드는 해당 컨테이너에 접속할 포트 번호를 설정함.

위 yaml 파일을 저장후, kubectl apply -f pod-sample.yaml명령을 실행해 클러스터에 적용한다.  

### 파드 생명주기
pod lifecycle  
1. pending : 쿠버네티스 시스템에 파드를 생성하는 중   
2. running : 파드 안 모든 컨테이너가 실행중인 상태  
3. succeeded : 파드 안 모든 컨테이너가 정상 실행 종료된 상태로 재시작되지 않음  
4. failed : 파드 안 모든 컨테이너 중 정상적으로 실행 종료되지 않은 컨테이너가 있는 상태(비정상 종료)  
5. unknown : 파드의 상태를 확인할 수 없는 상태. 보통 파드가 있는 노드와 통신할 수 없을 때  

kubectl describe pods 파드이름(kubernetes-simple-pod) 를 실행 후, status 항목을 보면 확인할 수 있음.  

### kubelet으로 컨테이너 진단하기
컨테이너가 실행된 후에는 kubelet이 컨테이너를 주기적으로 진단한다. 이때 필요한 probe에는 두가지가 있다.

#### livenessProbe  
컨테이너가 실행되었는지 확인하고, 이 진단이 실패할 경우 kubelet은 컨테이너를 종료시키고, 재시작 정책에 따라 컨테이너를 재시작함. 컨테이너에 liveness probe를 어떻게 할지 명시되지 않았다면 기본 상태값은 Success이다.  
#### readinessProbe  
컨테이너가 실행된 후 실제로 서비스 요청에 응답할 수 있는지 진단함. 이 진단이 실패하면 endpoint controller는 해당 파드에 연결된 모든 서비스를 대상으로 엔드포인트 정보를 제거한다. 첫번째 readinessProbe를 하기 전까지의 기본 상태값은 Failure이다. readinessProbe를 지원하지 않는 컨테이너는 기본 상태값이 Success이다.  
  
쿠버네티스에는 진단을 위해 앞 두가지 프로브가 있다. readinessProbe를 지원하는 컨테이너는 컨테이너가 실행된 다음 바로 서비스에 투입되어서 트래픽을 받지 않는다. 실제 트래픽을 받을 준비가 되었음을 확인 후, 트래픽을 받을 수 있다.  
  
### 파드에 CPU와 메모리 자원 할당
파드를 설정할 때, 파드 안 각 컨테이너가 CPU나 메모리를 얼마나 사용할 수 있을지 조건을 미리 지정해 놓을 수 있다. 파드에는 CPU와 메모리를 대상으로 자원 사용량을 설정하도록 .limits와 .requests 필드를 사용할 수 있다.  

~~~
apiVersion: v1
kind: Pod
metadata:
  name: kubernetes-simple-pod                     
  labels:
    app: kubernetes-simple-pod                    
spec:
  containers:
  - name: kubernetes-simple-pod                   
    image: arisu1000/simple-container-app:latest
    resources:
      requests:
        cpu: 0.1
        memory: 200M
      limits:
        cpu:0.5
        memory: 1G
    ports:
    - containerPort: 8080                         
~~~

**.spec.containers[].resources.request** : 최소 자원 요구량, 파드가 실행될 때 설정된만큼 자원 여유가 있는 노드가 있어야 파드를 그곳에 스케쥴링해 실행함.  
**.spec.containers[].resources.limits**: 자원을 최대로 얼마까지 사용할 수 있는지 제한함.

### 파드에 환경변수 설정하기
컨테이너를 사용할 때 장점 중 하나는 개발 환경에서 만든 컨테이너의 환경 변수만 변경해 실제 환경에서 실행하더라도 개발 환경에서 동작하던 그대로 동작한다는 점이다. 

파드 환경 변수 설정의 예는 아래와 같다.  
~~~
apiVersion: v1
kind: Pod
metadata:
  name: kubernetes-simple-pod                     
  labels:
    app: kubernetes-simple-pod                    
spec:
  containers:
  - name: kubernetes-simple-pod                   
    image: arisu1000/simple-container-app:latest
    ports:
    - containerPort: 8080
    env:
    - name: TESTENV01
      value: "testvalue01"
    - name: HOSTNAME
      valueFrom:
        fieldRef:
          fieldPath: spec.nodeName
    - name: POD_NAME
      valueFrom:
        fieldRef:
          fieldPath: metadata.name
    - name: POD_IP
      valueFrom:
        fieldRef:
          fieldPath: status.podIP
    - name: CPU_REQUEST
      valueFrom:
        resourceFieldRef:
          containerName: kubernetes-simple-pod
          resource: requests.cpu
    - name: CPU_LIMIT
      valueFrom:
        resourceFieldRef:
          containerName: kubernetes-simple-pod
          resource: limits.cpu
~~~

name: 사용할 환경변수의 이름을 설정함  
value: 문자열이나 숫자 형식의 값을 설정함  
valueFrom: 값을 직접 할당하는 것이 아니라 다른 곳에서 참조하는 값을 설정함  
fieldRef: 파드의 현재 설정 내용을 값으로 설정함  
fieldPath: fieldRef에서 어디서 값을 가져올것인지 지정함(값을 참조하려는 항목의 위치를 지정함)  
containerName: 환경변수 설정을 가져올 컨테이너 이름을 설정함  
resource: 어떤 자원의 정보를 가져올지 설정함  
  
kubectl apply -f pod-al.yaml 명령 실행후,  
kubectl exec -it kubernetes-simple-pod sh 명령을 실행해 컨테이너 안으로 접속한다. 그리고 env 명령어를 실행하여 환경변수들을 확인합니다.  
