
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
