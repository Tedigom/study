## 클러스터의 구성  
**마스터노드** : 노드들의 상태를 관리하고 제어함. 쿠버네티스의 데이터 저장소로 사용하는 etcd를 함께 설치하거나, 별도 노드에 분리해서 설치하기도 한다.
마스터 노드를 1대만 설치할 수도있지만, 상용서비스라면 보통 고가용성을 고려해 3대나 5대로 구성한다. 홀수로 구성하면 7대 이상도 가능하지만, 
kube-controller-manager 활성화 상태로 동작할 수있는 리더 마스터 노드는 1대이다. (3~5대가 적당)  
  
**워커노드** : kublet이라는 에이전트가 동작하며, 마스터 노드의 명령을 받아 사용자가 선언한 파드나 잡을 실제 실행한다.


## 마스터 노드 구성요소
**ETCD** : etcd는 고가용성을 제공하는 키밸류 저장소이다. 쿠버네티스에서 필요한 모든 데이터를 저장하는 실질적인 데이터베이스로, 일반적으로 
여러 장비에 분산해서 클러스터링 구성한다.  
  
**kube-apiserver** : 쿠버네티스 클러스터의 api를 사용할 수 있게 해주는 프로세스이다. 클러스터로 요청이 왔을때 그 요청이 유효한지 검증하며,
쿠버네티스로의 모든 요청은 kube-apiserver를 통해 다른 곳으로 전달되도록 되어있다. kube-apiserver는 수평적으로 확장이 가능하게 설계되어 있어,
여러대의 장비에 여러개를 띄워놓고 사용할 수 있다.  

**kube-scheduler** : 새로운 포드들이 만들어질 때, 현재 클러스터내에서 자원할당이 가능한 노드들 중에서 알맞은 노드를 선택해서 그곳에 포드를 띄우는 역할을 한다. kube-scheduler는 조건에 맞는 노드를 찾아주는 역할을 한다.(필요한 하드웨어 요구사항, affinity/anti-affinty 조건, 특정 데이터가 있는 노드에 할당 등)  
  
**kube-controller-manager** : 각각의 컨트롤러들을 실행하는 역할을 함(컨트롤러 : pod들을 관리하는 역할)  
  
**cloud-controller-manager** : 클라우드 서비스를 제공해 주는 곳에서 쿠버네티스의 컨트롤러들을 자신들의 서비스와 연계해서 사용하기 위해 사용함. - 노드 컨트롤러, 라우트 컨트롤러, 서비스 컨트롤러, 볼륨 컨트롤러  
  
## 워커 노드 구성요소
**kubelet** : 클러스터 내의 모든 노드에서 실행되는 에이전트. pod 내의 컨테이너들이 실행되는 것을 직접적으로 관리하는 역할을 함. kubelet은 PodSpecs라는 설정을 받아서 그 조건에 맞게 컨테이너를 실행하고 컨테이너가 정상적으로 실행되고 있는지 상태체크를 진행함.  
  
**kube-proxy** : 쿠버네티스는 클러스터 내부에 별도의 가상 네트워크를 설정하고 관리한다. kube-proxy는 이런 가상 네트워크가 동작할 수 있게 하는 실질적인 역할을 하는 프로세스. 호스트의 네트워크 규칙을 관리하거나 커넥션 포워딩을 하기도 함.  
  
**container runtime** : 실제로 컨테이너를 실행시키는 역할을 함. Docker 외에 rkt, runc같은 런타임도 지원함. 그 외에도 컨테이너에 관한 표준을 제정하는 역할을 하는 OCI(Open Container Initiative)의 런타임 규격을 구현하고 있는 컨테이너 런타임이라면 쿠버네티스에서 사용가능함.

## Kubespray로 쿠버네티스 클러스터 구성 Practice  
**1. 마스터 노드 #1인 Master1 서버에서 다른 서버에 원격접속(SSH)이 가능하도록 설정**  
**2. KubeSpray 설치**  
**3. 클러스터로 구성할 서버 정보를 설정**  
**4. 클러스터 설치 옵션 확인**  
**5. Kubespray가 제공하는 앤서블 플레이북을 실행**  

### 1. SSH 키 생성과 배포
master1에서 공개키와 비밀키를 생성한다. 여기서 생성한 공개키를 다른 서버에 모두 배포할 것이며, SSH 키를 생성하는 명령은  
~~~
$ ssh-keygen -t rsa
~~~
이다. -t rsa 옵션은 RSA 방식의 암호화 키를 만들겠다는 뜻이다. 명령을 실행하면 키를 생성할 위치와 최초 생성할 때 사용할 비밀번호를 묻는다. 아무것도 입력하지 않고 Enter 키를 눌러 기본값으로 생성한다.  
성공했다면 .ssh 디렉터리 안에 비밀 키 id_rsa 파일과 공개 키 id_rsa.pub 파일을 생성했을 것이다.
~~~
$ ls -al .ssh/ 
~~~
명령으로 확인한다.  
이후 생성한 공개키를 다른 서버에 배포한다  cat .ssh/id_rsa.pub 으로 생성한 공개키의 내용을 확인할 수 있으며, 해당 내용을 클립보드에 복사해놓는다.  
  
Google Cloud platform의 경우 메타데이터 내에 SSH 공개키를 저장하여 SSH 연동을 할 수 있다.

### 2. Kubespray 설치
sudo apt update로 우분투 패키지 매니저를 최신상태로 업데이트  
sudo apt -y install python-pip 명령으로 pip 설치  
  
git clone https://github.com/kubernetes-sigs/kubespray.git 으로 클론  
cd kubespay 후 git checkout -b v.2.11.0  
sudo pip install -r requirements.txt 명령으로 Kubespray에서 필요한 패키지 설치  

### 3. Kubespray 설정
Master1을 포함한 클러스터로 구성할 모든 서버의 정보와 설치 옵션을 설정 -- cluster 이름을 mycluster로 한다.  
inventory/sample 디렉터리에 설정에 필요한 기본 템플릿이 있으므로, cp -rfp inventory/sample inventory/mycluster 명령을 실행한다.
ls inventory/mycluster 명령을 실행해 mycluster 디렉터리 안을 확인한다.  
group_vars 디렉터리 안에는 클러스터 설치에 필요한 설정 내용이 있고, inventory.ini 파일에는 설치 대상 서버들의 정보를 설정한다.  
서버 설정 후 ansible-playbook -i inventory/mycluster/inventory.ini -v --become --become-user=root cluster.yml 명령으로 cluster.yml 스크립트 파일을 실행한다.


## 쿠버네티스로 컨테이너 실행하기
### kubectl
쿠버네티스 클러스터를 관리하는 동작 대부분은 kubectl CLI로 실행할 수 있다. kubectl에서으 ㅣ지원하는 명령은 아래와 같이 구분할 수 있다.  

- 쿠버네티스 자원들의 생성, 업데이트, 삭제(create, update, delete)
- 디버그, 모니터링, 트러블슈팅(log, exec, cp, top, attach, ...)
- 클러스터 관리(cordon, toop, drain, taint...)  
  
kubectl 자동완성 명령 : echo 'source<(kubectl completion bash)' >>~/.bashrc  
  

## 디플로이먼트를 이용해 컨테이너 실행하기
쿠버네티스를 이용해서 컨테이너를 실행하는 방법 두가지가 있음  
  
1. kubectl run 명령으로 직접 컨테이너를 실행  
2. YAML 형식의 템플릿으로 컨테이너를 실행(템플릿으로 컨테이너를 관리하면 버전 관리 시스템과 연동해서 자원정의 변동 사항을 추적하기 쉬움)  
  
#### 1. kubectl run 명령으로 직접 컨테이너를 실행  
~~~
$ kubectl run nginx-app --image nginx --port=80
~~~  
파드의 갯수 늘리기  
~~~
$ kubectl scale deploy nginx-app --replicas=2
~~~  

#### 2.템플릿(yaml)으로 컨테이너 실행하기
deployment/nginx-app.yaml 파일을 만듦
~~~
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nginx-app
  labels:
    app: nginx-app
  spec:
    replicas: 1
    selector:
      matchLabels:
        app: nginx-app
    template:
      metadata:
        labels:
          app: nginx-app
      spec:
        containers:
        - name: nginx-app
          image: nginx
          ports:
          - containerPort :80
~~~

kubectl apply -f nginx-app.yaml 명령 실행  
쿠버네티스의 자원들은 관련 설정을 정의한 템플릿(매니페스트)과 kubectl apply 명령을 이용해 
선언적 형태로 관리하는 것을 권장한다.  

## 클러스터 외부에서 클러스터 안 앱에 접근하기
### 서비스
서비스의 타입에는 ClusterIP, NodePort, LoadBalancer, ExternalName이 있다. 처으멩는 서비스 하나에 모든 노드의 지정된 포트를 할당하는 NodePort를 설정하겠다.  
kubectl expose deployment nginx-app --type=Nodeport 를 실행한다.
