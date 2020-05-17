
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




