# kubernetes basic
kubernetes.io 튜토리얼을 기반으로 작성함.

## 1. 클러스터 생성하기

`minikube version` - minikube version 확인  
`minikube start` - cluster 생성함  
`kubectl version` - kubectl version 확인  

kubectl은 kubernetes의 커맨드라인 인터페이스

`kubectl cluster-info` - 클러스터의 디테일을 확인할 수 있음  
`kubectl get nodes` - 클러스터의 node를 확인  

## 2. 앱 배포하기 ( kubernetes deployment)
`--help` 를 커맨드 뒤에 붙이면, 더 상세한 정보를 얻을 수 있다.  
ex) `kubectl get nodes --help`

`kubectl create deployment` command를 통해 deploy를 시작한다.  
이때, deployment name과 app image location을 줘야한다.

ex) `kubectl create deployment kubernetes-bootcamp --image=gcr.io/google-samples/kubernetes-bootcamp:v1`  

`kubectl get deployments` - 클러스터 내 deployment list 확인  


## 3. 앱 살펴보기 (Explore)
pod는 하나 또는 그 이상의 어플리케이션 컨테이너들의 그룹이고, 공유 스토리지 (Volume), IP 주소, 그리고 그것을 동작시키는 방식에 대한 정보( 컨테이너 이미지 버전 또는 사용할 특정 포트 등, 각 컨테이너가 동작하는 방식에 대한 정보)를 포함한다.  

pod 내 컨테이너는 IP 주소, 포트스페이스를 공유하고, 항상 함께 위치하고, 함께 스케쥴링되고, 동일 노드 상의 컨텍스트를 공유하면서 동작한다.   
!(https://github.com/Tedigom/study/blob/master/kubernetes%20tutorial/pod.PNG
가장 보편적인 운용 업무는 아래의 kubectl 명령어를 이용하여 보통 처리한다.  

`kubectl get` - 자원을 나열한다.  
`kubectl describe` - 나원에 대해 상세한 정보를 보여준다.  
`kubectl logs` - 파드 내 컨테이너들의 로그들을 출력한다.  
`kubectl exec` - 파드 내 컨테이너에 대한 명령을 실행한다.  

현재 존재하는 pod에 대해 정보를 알기위해서는 아래와 같은 명령어를 실행한다.
`kubectl get pods`  
좀더 세부적인 내용은  
`kubectl describer pods`  
명령어를 실행하여 확인할수 있다.  

pod는 격리된 private network에 위치하여 실행된다. 따라서 디버그와 interact를 하기 위해서는 proxy access를 만들어야한다.  
`kubectl proxy`  

파드 내 컨테이너의 로그들을 출력하기 위해서는 아래와 같은 명령어를 실행한다.  
`kubectl logs $POD_NAME`  

파드가 가동되면 컨테이너에서 직접 명령을 실행할 수 있다. 이때 exec 명령을 사용하고, 파드 이름을 매개변수로 사용하게 된다.  
`kubectl exec $POD_NAME env`

pod에 들어있는 컨테이너의 bash session을 실행한다.  
`kubectl exec -ti $POD_NAME bash`  

해당 튜토리얼에서는 NodeJS application을 실행하는 컨테이너의 콘솔을 열었다.  
`cat server.js`  

> pod 내 컨테이너가 하나밖에 없기 때문에 위와같은 코드가 사용가능하다. 컨테이너가 여러개일 경우에는 컨테이너 지정이 필요하다.


## 4. 앱을 외부로 노출하기 (service 이용하기)
Service는 하나의 논리적인 podset과 그 pod에 접근할 수 있는 정책을 정의하는 추상적개념이다.
각 pod는 고유의 IP를 가지고 있지만, 이는 Service의 도움없이 클러스터 외부로 노출되지 못한다. service는 pod와 외부를 연결하는 api gateway이며, loadbalancer 역할도 함께 해주고 있다.


