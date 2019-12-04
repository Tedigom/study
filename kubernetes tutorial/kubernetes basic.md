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
ex) `kubectl create deployment kubernetes-bootcamp --image=gcr.io/google-samples/kubernetes-bootcamp:vq
