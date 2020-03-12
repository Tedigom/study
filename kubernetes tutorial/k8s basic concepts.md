# 쿠버네티스 기본 개념

마스터와 노드의 구성과 통신 구조  
  
![마스터와 노드의 구성과 통신구조](https://t1.daumcdn.net/cfile/tistory/99A5974C5B0B48DF32)

쿠버네티스의 모든 통신은 kube-apiserver가 중심이다. kube-apiserver를 거쳐 다른 컴포넌트가 서로 필요한 정보를 주고받는다. 특히 etcd는 kube-apiserver만 접근할 수 있다.  
  
마스터에는 kubelet이 마스터에 있는 도커를 관리하고, 도커 안에는 앞에서 소개한 쿠버네티스 관리용 컴포넌트 (kube-scheduler, kube-controller-manager, kube-apiserver, kube-proxy)가 있다. 초기에는 서버 프로세스로 실행했지만, 최근에는 컨테이너로 실행된다.  
  
노드 역시 kublet으로 도커를 관리한다. kublet은 마스터의 kube-apiserver와 통신하면서 pod의 생성, 관리, 삭제를 담당한다. 노드의 kube-proxy는 마스터와 다르게 서버 프로세스로 실행할 수 있다.  

## 쿠버네티스의 주요 컴포넌트  

쿠버네티스는 클러스터를 관리하며, 세가지 컴포넌트로 구분된다. 마스터용, 노드용, 애드온용 컴포넌트이다.  

### 마스터용 컴포넌트
#### etcd  
etcd는 고가용성을 제공하는 key-value 저장소이다. raft 알고리즘을 구현한 것으로, 쿠버네티스에서는 필요한 모든 데이터를 저장하는 데이터베이스 역할을 한다.  
etcd는 서버 하나당 프로세스 1개만 사용할 수 있다. 보통 etcd 자체를 클러스터링 한 후, 여러개의 마스터 서버에 분산하여 실행해 데이터의 안정성을 보장하도록 구성한다.

#### kube-apiserver  
kube-apiserver는 쿠버네티스 클러스터의 API를 사용할 수 있도록 하는 컴포넌트이다. 통신의 중심으로, kube-apiserver를 거쳐 다른 컴포넌트가 서로 필요한 정보를 주고 받는다.  

#### kube-scheduler  
kube-scheduler는 현재 클러스터 안에서 자원 할당이 가능한 노드 중 알맞는 노드를 선택해서 새롭게 만든 파드를 실행한다. pod는 처음 실행할 때, 여러가지 조건을 설정하며, kube-scheduler가 조건에 맞는 노드를 찾는다.  

#### kube-controller-manager  
쿠버네티스는 파드들을 관리하는 컨트롤러가 있다. kube-controller-manager는 이 컨트롤러 각각을 실행하는 컴포넌트이다.  

#### cloud-controller-manager  
Cloud-controller-manager는 쿠버네티스의 컨트롤러들을 클라우드 서비스와 연결해 관리하는 컴포넌트이다. 관련 컴포넌트의 소스 코드는 각 클라우드 서비스에서 직접 관리한다. (Node Controller, Route Controller, Service Controller, Volume Controller를 관리)

### 노드용 컴포넌트  
#### kubelet
kubelet은 클러스터 안 모든 노드에서 실행디는 에이전트로, pod의 실행을 직접 관리한다. kubelet은 파드 스펙이라는 조건이 담긴 설정을 전달받아 컨테이너를 실행하고 컨테이너가 정상적으로 실행되는지 헬스 체크를 진행한다.  

#### kube-proxy  
쿠버네티스는 클러스터 안에 별도의 가상 네트워크를 설정하고 관리한다. kube-proxy는 이런 가상 네트워크의 동작을 관리하는 컴포넌트이다. 호스트의 네트워크 규칙을 관리하거나 연결을 전달할 수도 있다.  


## 오브젝트와 컨트롤러
쿠버네티스는 크게 오브젝트와 오브젝트를 관리한느 컨트롤러로 나뉜다. 사용자는 템플릿으로 "desired state"를 정의하고, 컨트롤러는 바라는 상태와 현재 상태가 일치하도록 오브젝트들을 생성/삭제한다.  
오브젝트에는 pod, service, volume, namespace 등이 있다. 컨트롤러에는 ReplicaSet, Deployment, StatefulSet, DemonSet, Job등이 있다.  

### 네임스페이스
네임스페이스는 쿠버네티스 클러스터 하나를 여러 개 논리적인 단위로 나눠서 사용하는 것이다. 네임스페이스는 팀/사용자 별로 공유할 수 있게하고, 클러스터 안에서 용도에 따라 실행해야 하는 앱을 구분할 때 사용할 수 있다.  
kubectl get namespaces로 네임스페이스를 확인할 수 있다.  
kubectl로 네임스페이스를 지정해서 사용할 때에는 --namespace=kube-system 처럼 네임스페이스를 명시하면 된다.  

### 템플릿
쿠버네티스 클러스터의 오브젝트나 컨트롤러가 어떤 상태여야 하는지를 적용할 때에는 YAML 형식의 템플릿을 사용한다.  
템플릿의 기본 형식은 아래와 같다.  
~~~
---
apiVersion: v1
kind: Pod
metadata:
spec:
~~~
각 항목은 필드라 하고, 필드 각각은 다음과 같은 설정을 한다.  
* apiVersion : 사용하려는 쿠버네티스 API 버전을 명시한다.  
* kind : 어떤 종류의 오브젝트 혹은 컨트롤러에 작업인지 명시한다. Pod라고 설정하면, 파드에 관한 템플릿이다. 여기에는 Pod, Deployment, Ingress 등의 다양한 오브젝트나 컨트롤러를 설정할 수 있다.  
* metadata : 메타데이터를 설정한다. 해당 오브젝트의 이름이나 레이블 등을 설정한다.  
* spec : 파드가 어떤 컨테이너를 갖고 실행하며, 실행할 때 어떻게 할지 명시한다.(.spec과 .metadata는 다양한 하위필드가 있다.)  

## 파드
### 파드의 개념
쿠버네티스는 파드라는 단위로 컨테이너를 묶어서 관리하므로, 보통 컨테이너 하나가 아닌 여러개의 컨테이너로 구성된다.( 컨테이너를 직접 관리하지 않고, 파드 단위로 관리한다.) 파드 하나에 속한 컨테이너들은 모두 노드 하나 안에서 실행된다.(여러 노드에 흩어져서 실행되는 일은 없다.) 파드의 역할 중 하나가 컨테이너들이 같은 목적으로 자원을 공유하는 것이므로 가능한 일이다.  

![pod&container](https://www.google.com/url?sa=i&url=https%3A%2F%2Fen.wikipedia.org%2Fwiki%2FKubernetes&psig=AOvVaw01KhX3KE3cDw8gjQDa80xv&ust=1584061413295000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCPi1luXek-gCFQAAAAAdAAAAABAD)
  
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





