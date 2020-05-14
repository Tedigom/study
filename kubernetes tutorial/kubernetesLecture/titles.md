# 쿠버네티스 기초 강의 title

### 기초다지기
* VM vs Container
* Kubernetes Overview ( why kubernetes?)

### 쿠버네티스 설치
* 서버전용
* Virtual box
* GCP

### 기본 오브젝트
* Pod
  * 초급 
    * Container
    * Label
    * Node Schedule
  * 중급
    * LifeCycle
    * ReadinenssProbe
    * LivenessProbe
    * QoS Classes
    * Node Scheduling
* Service
  * 초급
    * ClusterIP
    * NodePort
    * LoadBalancer
  * 중급
    * Headless
    * Endpoint
    * ExternalName
* Volume
  * 초급
    * emptyDir
    * hostPath
    * PV/PVC
  * 중급
    * Dynamic Provisioning
    * StorageClass
    * status
    * ReclaimPolicy
* ConfigMap/Secret
  * Env
  * Mount
* Namespace , ResourceQuota  , LimitRange
* AccessingAPI , Authentication, Authorization, Dashboard

### 컨트롤러
* Replication Controller / ReplicaSet
  * Template
  * Replicas
  * Selector
* Deployment
  * Recreate
  * RollingUpdate
  * CanaryUpdate
* DaemonSet / Job / CronJob
* StatefulSet
* Ingress
* Autoscaler
  * HPA
  * VPA
  * CA
  
