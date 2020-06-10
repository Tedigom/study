##  어플리케이션 배포
#### 단일 인스턴스(deployment) 시작  
kubectl create deployment kubernetes-bootcamp --image=gcr.io/google-samples/kubernetes-bootcamp:v1  

#### 단일 pod 시작(port=80)
kubectl run nginx --image=nginx:1.17.4 --generator=run-pod/v1 --restart=Never --port=80

**단일 파드를 만들기 위해서는 --generator=run-pod/v1을 붙여주어야 한다.**

#### 매니페스트로부터 리소스 생성
kubectl apply -f ./my-manifest.yaml  




## 어플리케이션 조회 
#### Name으로 정렬된 서비스의 목록 조회
kubectl get services --sort-by=.metadata.name  

#### timestamp로 정렬된 파드의 목록 조회
kubectl get pods --sort-by=.metadata.creationTimestamp

#### 재시작 횟수로 정렬된 파드의 목록 조회
kubectl get pods --sort-by='.status.containerStatuses[0].restartCount'  

#### PersistentVolumes을 용량별로 정렬해서 조회
kubectl get pv --sort-by=.spec.capacity.storage  

#### app=casandra 레이블을 가진 모든 파드의 레이블 버전 조회
kubectl get pods --selector=app=casandra -o \
  jasonpath='{.items[* ].metadata.labels.version}'

#### 네임스페이스의 모든 실행 중인 파드를 조회
kubectl get pods --field0selecotr=status.phase=Running  

#### 모든 노드의 외부IP를 조회
kubectl get nodes - o jsonpath='{.items[* ].status.addresses[?(@.type=="ExternalIP")].address}'

#### 모든 파드의 레이블 조회
kubectl get pods --show-labels

#### pod에 들어있는 컨테이너의 bash session을 실행
kubectl exec -ti $POD_NAME bash




## 리소스 업데이트
#### 앱 버전 업데이트
kubectl set image deployments/frontend www=image:v2 ("frontend" 디플로이먼트의 "www" 컨테이너의 이미지를 업데이트(rolling))    

kubectl edit deployment nginx ( vi editor이 열리고 버전을 바꿈)


#### 업데이트 롤백
kubectl rollout undo deployments/kubernetes-bootcamp  

#### 라벨 붙이기
kubectl label pod $POD_NAME app=v1

## 앱 외부로 노출하기 (service)
#### service 만들고 노출시키기
kubectl expose deployment/kubernetes-bootcamp --type="NodePort" --port 8080



## 앱 스케일링하기
#### Deployment에서 replica를 4로 scaleout 시키기
kubectl scale deployments/kubernetes-bootcamp --replicas=4

#### mysql이라는 디플로이먼트의 현재크기가 2인경우, mysql을 3으로 스케일
kubectl scale --current-replicas=2 --replicas=3 deployment/mysql



## 리소스 삭제
#### "baz", "foo"와 동일한 이름을 가진 파드와 서비스 삭제
kubectl delete pod,service baz foo

#### name=myLabel 라벨을 가진 파드와 서비스 삭제
kubectl delete pods,services -l name=myLabel

## 스케쥴링
#### taint 적용 ( create a taint node01 with key of 'spray', value of 'mortein' and effect of 'NoSchedule')  
kubectl taint nodes node01 spray=mortein:NoSchedule

#### toleration 적용 ( create another pod named 'bee' with the NGINX image, which has a toleration set to the taint Mortein)
~~~
yaml file (bee.yaml)  

apiVersion: v1
kind: Pod
metadata:
  name: bee
spec:
  containers:
  - image: nginx
    name: bee
  tolerations:
  - key: spray
    value: mortein
    effect: NoSchedule
 
kubectl run bee --generator=run-pod/v1 --image=nginx --dry-run -o yaml 로 yaml파일 형식 복사 후 toleration 적용
~~~

#### Node Affinity 적용 ( Set Node Affinity to the deployment to place the PODs on node01 only)
~~~
deployment yaml 파일에서 NodeAffinity를 적용함.

yaml file (blue-deployment.yaml)

apiVersion: apps/v1
kind: Deployment
metadata:
  name: blue
spec:
  replicas: 6
  selector:
    matchLabels:
      run: nginx
  template:
    metadata:
      labels:
        run: nginx
    spec:
      containers:
      - image: nginx
        imagePullPolicy: Always
        name: nginx
      affinity:
        nodeAffinity:
          requiredDuringSchedulingIgnoredDuringExecution:
            nodeSelectorTerms:
            - matchExpressions:
              - key: color
                operator: In
                values:
                - blue
~~~

#### static pod 만들기 ( create a static pod named static-busybox that uses the busybox image and the command sleep 1000 )
~~~
static pod는 /etc/kubrernetes/manifests 디렉토리 안에 manifest 파일이 저장되어 있어야 한다. 

kubectl run --restart=Never --image=busybox static-busybox --dry-run -o yaml --command -- sleep 1000 > /etc/kubernetes/manifests/static-busybox.yaml

kubectl apply -f static-busybox.yaml
~~~

#### static Pod의 삭제 ( we just created a new static pod named static-greenbox. find it and delete it)
~~~
Static pod의 삭제는 해당 파드가 있는 Node의 ssh 접속을 통해, staticpod yaml 파일 경로를 찾아내고, 해당 yaml파일을 삭제하여야 최종적으로 지워질 수 있다.

1. kubectl get nodes -o wide 를 통해 노드 IP확인 후 지우려고 하는 static pod가 있는 노드에 ssh 접속
2. static pod manifest 파일에서 static pod yaml 파일 경로 확인 (cat /var/lib/kubelet/config.yaml | grep static -i)
--> staticpodpath: /etc/just-to-mess-with-you
3. 해당 디렉토리에서 greenbox.yaml 삭제
~~~

