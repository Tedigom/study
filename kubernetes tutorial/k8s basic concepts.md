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

