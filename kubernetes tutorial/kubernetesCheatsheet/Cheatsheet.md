###  어플리케이션 배포
<단일 인스턴스 시작>
kubectl create deployment kubernetes-bootcamp --image=gcr.io/google-samples/kubernetes-bootcamp:v1

<매니페스트로부터 리소스 생성>
kubectl apply -f ./my-manifest.yaml
