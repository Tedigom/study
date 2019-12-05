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

![pod](https://github.com/Tedigom/study/blob/master/kubernetes%20tutorial/pod.PNG)  
  
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
각 pod는 고유의 IP를 가지고 있지만, 이는 Service의 도움없이 클러스터 외부로 노출되지 못한다. service는 pod와 외부를 연결하는 gateway이며, loadbalancer 역할도 함께 해주고 있다.  

service는 podset에 걸쳐 트래픽을 라우트한다. 어플리케이션을 운영하다 보면 쿠버네티스 내 pod가 죽기도 하고, pod가 복제 되기도 한다. service 경계 안에서의 pod의 service discovery와 routing은 Service에 의해 처리된다.  

이때 레이블과 셀렉터를 이용하여 그룹핑해주게 된다.  

![service](https://github.com/Tedigom/study/blob/master/kubernetes%20tutorial/service.PNG)  
  
### 새로운 서비스 만들기
`kubectl get services` - 클러스터내 service들을 listup 한다.(기본적으로 kubernetes라는 service가 생성되어있다.)  
새 서비스를 만들고, 외부에 노출한다. 이때, NodePort를 매개변수로 사용하여 노출명령을 내린다.  

kubernetes-bootcamp라는 이름의 Service를 만들고 노출시키겠다. 
`kubectl expose deployment/kubernetes-bootcamp --type="NodePort" --port 8080`  

생성된 Service는 아래의 명령어로 더 자세하게 결과를 볼 수 있다.  
`kubectl describe services/kubernetes-bootcamp`  

NODE_PORT라는 환경변수에 Node port를 지정해주면 curl을 이용하여 노출된 ip를 통해 통신할 수 있다.  
`export NODE_PORT=$(kubectl get services/kubernetes-bootcamp -o go-template='{{(index .spec.ports 0).nodePort}}')
echo NODE_PORT=$NODE_PORT`  

### 라벨 사용하기
`kubectl describe deployment` 을 통해 label을 확인할 수 있다.  
라벨 확인 후, 특정 라벨을 가진 pod만 list up 해보겠다. (label명 : run=kubernetes-bootcamp)  
`kubectl get pods -l run=kubernetes-bootcamp`  
해당 라벨만 가진 서비스의 검색도 똑같이 할 수 있다.  
`kubectl get services -l run=kubernetes-bootcamp`  

Pod의 name을 가져오고, POD_NAME이라는 환경변수에 저장을 한다.  
`export POD_NAME=$(kubectl get pods -o go-template --template '{{range .items}}{{.metadata.name}}{{"\n"}}{{end}}')
echo Name of the Pod: $POD_NAME`  

새 라벨을 적용하기 위해서는, 매개변수로 개체 유형, 개체이름 및 new label을 입력한다.
`kubectl label pod $POD_NAME app=v1`  

pod에는 새 라벨이 적용되고, describe pod 명령으로 확인할 수 있다.  
`kubectl describe pods $POD_NAME`

### 서비스 지우기
아래의 명령어를 이용하여 특정 라벨의 서비스를 삭제할 수 있다.  
`kubectl delete service -l run=kubernetes-bootcamp`  

서비스를 지웠으므로, cluster의 바깥과는 통신할 수 없다.  


## 5. 앱 스케일링하기
지금까지는 디플로이먼트에서 어플리케이션을 구동하기 위해 단 하나의 pod만을 생성했다. 트래픽이 증가하면 사용자 요청에 맞추어, 어플리케이션의 규모를 조정할 필요가 있다. 디플로이먼트의 복제 수를 변경하면 스케일링이 수행된다.

### deployment를 스케일링한다.
`kubectl get deployments` 를 실행하면, 1개의 pod만 실행중인 것을 확인할 수 있다. ( kubernetes-bootcamp )  

![kubectl-get-deployments](https://github.com/Tedigom/study/blob/master/kubernetes%20tutorial/getdeploymentResult.PNG)

결과에서 READY 열에서는 1/1을 표시하고 있는데, 이는 CURRENT/DESIRED의 의미이다. CURRENT는 현재 가동중인 replica의 갯수이고, DESIRED는 설정된 복제본 갯수이다.  

kubectl scale을 이용하여 Deployment에서 replicas를 4로 scale-out 시킨다.  
`kubectl scale deployemnts/kubernetes-bootcamp --replicas=4`  

`kubectl get deployments` 를 실행시켜 결과를 확인해본다. READY가 1/4 , UP-TO-DATE와 AVAILABLE이 4로 올라가있는 것을 확인할 수 있다.  

![scaleout-result](https://github.com/Tedigom/study/blob/master/kubernetes%20tutorial/scaleoutresult.PNG)

`kubectl get pods -o wide` 를 실행시켜, pod의 상태를 확인해본다.  

![pods](https://github.com/Tedigom/study/blob/master/kubernetes%20tutorial/podresult.PNG)

Deployment의 변경 상세 내용을 알기 위해서는 아래의 명령어를 사용하면 된다.   
`kubectl describe deployments/kubernetes-bootcamp`  

### 로드 밸런싱
exposed IP와 포트를 알기위해서는 service의 상세정보를 알아야 하므로 아래의 명령어를 사용한다.  
`kubectl describe services/kubernetes-bootcamp`  

NODE_PORT라는 환경변수에, service의 Node port값을 저장한다.  
`export NODE_PORT=$(kubectl get services/kubernetes-bootcamp -o go-template='{{(index .spec.ports 0).nodePort}}')
echo NODE_PORT=$NODE_PORT`  

`curl $(minikube ip):$NODE_PORT`을 실행하여 작동하는지 확인한다.  

### 스케일 다운
스케일 다운 역시 스케일 다운과 마찬가지로 `scale` 명령어를 이용하여 진행한다. 이번에는 replica를 2개로 줄여보겠다.  
`kubectl scale deployemts/kubernetes-bootcamp --replicas=2`  

`kubectl get deployments` 명령어를 통해 deployment 현재 상황에 대해 알 수 있으며,  
`kubectl get pods -o wide` 명령어를 통해 2개의 pod가 지워진 것을 확인할 수 있다.  

## 6.앱 업데이트하기
쿠버네티스는 일반적으로 롤링업데이트를 통해 어플리케이션을 업데이트한다. 롤링 업데이트는 pod instance를 점진적으로 새로운것으로 업데이트하여, deployment update가 서비스의 중단 없이 이루어질 수 있도록 한다.  

어플리케이션 스케일링과 비슷하게, deployment가 외부로 노출되면 service는 업데이트가 이루어지는 동안 이용가능한 pod에만 트래픽을 로드밸런스할 것이다. 롤링 업데이트는 아래의 동작들을 허용한다.  

* 하나의 환경에서 또 다른 환경으로의 어플리케이션 프로모션(컨테이너 이미지 업데이트를 통해)
* 이전 버전으로의 롤백
* 서비스 중단이 없는 어플리케이션의 CI/CD

### 앱 버전을 업데이트 하기  
어플리케이션을 더 높은 버전의 이미지로 업데이트하기 위해서는 `set image`명령어를 사용한다. deployement name과 배포할 이미지 버전을 함께 명시한다.  
`kubectl set image deployments/kubernetes-bootcamp kubernetes-bootcamp=jocatalin/kubernetes-bootcamp:v2`  

`kubectl get pods` 명령어를 입력해보면, 

![updating](https://github.com/Tedigom/study/blob/master/kubernetes%20tutorial/updating.PNG)  

이전 버전의 pod들은 순차적으로 terminating이 되고, 새로운 버전의 pod는 container creating 후 running으로 status가 바뀌며,  

![updated](https://github.com/Tedigom/study/blob/master/kubernetes%20tutorial/updated.PNG)  

얼마후 새로운 버전의 pod들이 Running되는 것을 확인할 수 있다.  

update는 rollout 명령어를 통해서도 할수있다.  
`kubectl rollout status deployments/kubernetes-bootcamp`  

### 업데이트 롤백하기
rollout undo 명령어를 통해 업데이트를 롤백할 수 있다.  
`kubectl rollout undo deployments/kubernetes-bootcamp`  




