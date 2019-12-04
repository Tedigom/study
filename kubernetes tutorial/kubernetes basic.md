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

