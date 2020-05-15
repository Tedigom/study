###  어플리케이션 배포
<단일 인스턴스 시작>  
kubectl create deployment kubernetes-bootcamp --image=gcr.io/google-samples/kubernetes-bootcamp:v1  

<매니페스트로부터 리소스 생성>  
kubectl apply -f ./my-manifest.yaml  

### 어플리케이션 조회 
#### Name으로 정렬된 서비스의 목록 조회
kubectl get services --sort-by=.metadata.name  

#### 재시작 횟수로 정렬된 파드의 목록 조회
kubectl get pods --sort-by='.status.containerStatuses[0].restartCount'  

#### PersistentVolumes을 용량별로 정렬해서 조회
kubectl get pv --sort-by=.spec.capacity.storage  

#### app=casandra 레이블을 가진 모든 파드의 레이블 버전 조회
kubectl get pods --selector=app=casandra -o \
  jasonpath='{.items[* ].metadata.labels.version}'

