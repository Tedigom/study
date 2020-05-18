
## Core Concepts

1. 특정 namespace의 자원의 List
~~~
kubectl get pod -n <namespace name>
kubectl get svc -n <namespace name>
~~~

2. JSON 경로 표현식으로 이름과 네임 스페이스를 표시하는 모든 pod List
~~~
kubectl get pods -o=jsonpath="{.items[*]['metadata.name','metadata.namespace']}"
~~~

3. 파드의 내용을 yaml file로 저장하고 싶을때
~~~
kubectl get pod nginx -o yaml --export
~~~

4. 파드의 force delete
~~~
kubectl delete po nginx --grace-period=0 --force
~~~

5. describe command 없이 Image version 체크
~~~
kubectl get pod nginx -o jsonpath='{.spec.containers[].image}{"\n"}'
~~~

## 멀티컨테이너 파드
ambassador, adaptor, sidecar 패턴에 대해 공부  

6. pod를 yaml파일로 받은 다음 수정하는 전략 (initial yaml file을 받은 후 수정)
~~~
kubectl run busybox --image=busybox --restart=Never --dry-run -o yaml -- bin/sh -c "sleep 3600; ls" > multi-container.yaml

// edit the pod to following yaml and create it
kubectl create -f multi-container.yaml
kubectl get po busybox
~~~

## 파드 디자인
Label, Selector, Annotation에 대해서 안다.  
Deployment와 rolling update, roll back에 대해서 안다.  
Job과 Cronjob에 대해 안다.  

7. 파드의 라벨 정보 확인
~~~
kubectl get pods --show-labels
kubectl get pods -L env
kubectl get pods -l 'env in (dev,prod)' --show-labels
~~~

8. 파드 중 하나의 레이블을 env = uat로 변경
~~~
kubectl label pod/nginx-dev3 env=uat --overwrite

# label 삭제
kubectl label pod nginx-dev{1..3} env-
~~~

9. rollout
~~~
# get the deployment rollout status
kubectl rollout status deploy webapp

# check the rollout history and make sure everything is ok after the update
kubectl rollout history deploy webapp

# rollback
kubectl rollout undo deploy webapp
~~~

## Configuration
configMap, SecurityContext, application resource requirement, secret, serviceAccount  

10. Configmap 만들기(literal value : appname=myapp)
~~~
kubectl create cm myconfigmap --from-literal=appname=myapp

# config file(txt)를 만들어 적용시키기
#1. config.txt를 만듦
cat >> config.txt << EOF
key1=value1
key2=value2
EOF

#2. configmap을 만듦
kubectl create cm keyvalconfigmap --from-file=config.txt

# 이후 application yaml 파일에 적용
~~~


11. Secret 만들기 ( user=myuser , password=mypassword)
~~~
kubectl create secret generic my-secret --from-literal=username=user --from-literal=password=mypassword

# 이후 application yaml 파일에 적용
~~~

12. Service Account 만들기
~~~
kubectl create sa admin

# 이후 application yaml 파일에 적용
~~~

## Observability
* ReadinessProbes, LivenessProbes
* container Logging
* monitor applications in Kubernetes
* Debugging in Kubernetes

13. Readiness Probe / Liveness Probe
~~~
# 1. Readiness Probe / Liveness Probe의 추가: 

apiVersion: v1
kind: Pod
metadata:
  creationTimestamp: null
  labels:
    run: nginx
  name: nginx
spec:
  containers:
  - image: nginx
    name: nginx
    ports:
    - containerPort: 80
    readinessProbe:
      httpGet:
        path: /
        port: 80
    livenessProbe:
      httpGet:
        path: /healthz
        port: 80
    resources: {}
  dnsPolicy: ClusterFirst
  restartPolicy: Never
status: {}

2. pod create
kubectl create -f nginx-pod.yaml

3. verify
kubectl describe pod nginx | grep -i readiness
kubectl describe pod nginx | grep -i liveness

~~~

14. List all the events sorted by timestamp and put them into file.log and verify
~~~
kubectl get events --sort-by=.metadata.creationTimestamp

// putting them into file.log
kubectl get events --sort-by=.metadata.creationTimestamp > file.log
~~~

15. log following
~~~
1. 5초마다 이벤트 발생하는 pod 생성

kubectl run hello --image=alpine --restart=Never -- /bin/sh -c "while true; do echo 'Hi I am from Alpine'; sleep 5; done"

2. hello라는 파드에 대한 로그 following

kubectl logs --follow hello
~~~

16. 디버깅
~~~
1. 디버깅 대상 파드 생성
kubectl create -f 
https://gist.githubusercontent.com/bbachi/212168375b39e36e2e2984c097167b00/raw/1fd63509c3ae3a3d3da844640fb4cca744543c1c/not-running.yml

2. pod 상태 확인
kubectl get pod not-running
kubectl describe pod not-running

3. pod가 not running인 이유 : ImagPullBackOff 이므로 조치를 취한다.
kubectl edit pod not-running   // vim editor가 열림
kubectl set image pod/not-running not-running=nginx
~~~

17. memory, cpu usage 가 높은 top3 파드 찾기
~~~
// get the top 3 hungry pods
kubectl top pod --all-namespaces | sort --reverse --key 3 --numeric | head -3

//putting into file
kubectl top pod --all-namespaces | sort --reverse --key 3 --numeric | head -3 > cpu-usage.txt
~~~





