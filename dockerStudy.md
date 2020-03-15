도커는 컨테이너에 어플리케이션 실행환경이 함께 배포되는 방식
-> 실행 환경 째로 배포하는 방식으로 의존성 문제를 근본적으로 해결함

"코드로 관리하는 인프라(IaC)와 불변 인프라"
 불변 인프라는 어떤 시점의 서버 상태를 저장해 복제할 수 있게 하자는 개념이다. 제대로 설정된 상태의 서버를 항상 사용할수 있다는 점이 가장 큰 장점

## Docker file
FROM : 컨테이너의 원형(틀) 역할을 할 도커 이미지(운영체제)를 정의
COPY : 특정 파일을 도커 컨테이너 안의 /usr/local/bin에 복사를 정의
RUN : 도커 컨테이너 안에서 어떤 명령을 수행하기 위한 것.
CMD : 완성된 이미지를 도커 컨테이너로 실행하기 전에 먼저 실행할 명령을 정의함.  
EntryPoint : Entrypoint를 지정하면, CMD의 인자가 EntryPoint에서 실행하는 파일에 인자로 주어진다. 즉 EntryPoint에 지정된 갑이 기본 프로세스를 지정하는 것이다.(bash, go, python.... )  
ex)  
~~~
FROM ubuntu:16.04

COPY helloworld /usr/local/bin      # 셸 스크립트 파일(helloworld)을 도커 컨테이너 안의 /usr/local/bin에 복사
RUN chmod +x /usr/local/bin/helloworld        # helloworld 스크립트 파일의 파일 실행권한 부여

CMD ["helloworld"]    # 어플리케이션 
~~~

## 도커 이미지 빌드하기

### 도커 이미지란? 
도커 이미지는 우분투 같은 운영체제로 구성된 파일 시스템은 물론, 컨테이너 위에서 실행하기 위한 어플리케이션이나, 그 의존 라이브러리, 도구에 어떤 프로세스를 실행할지 등의 실행환경의 설정정보까지 포함하는 아카이브다. 컨테이너의 템플릿 역할을 하는 이미지를 만드는 과정을 일반적으로 '도커이미지를 빌드한다.' 라고 한다. 

### docker search -이미지 검색
docker search 명령을 사용하여 도커 허브에 등록된 리포지토리를 검색할 수 있다.  
~~~
docker search [options] 검색_키워드

docker search --limit 5 mysql
~~~

### docker image tag - 이미지에 태그 붙이기
~~~
docker image tag 기반이미지명[:태그] 새이미지명[:태그]
ex)
docker image tag example/echo:latest example/echo:0.1.0
~~~
### docker image build - 이미지 
~~~
docker image build -t 이미지명[:태그명] Dockerfile의 경로

docker image build -t example/echo:latest

docker image ls 
로 이미지 목록을 볼 수 있다.
~~~
-t 옵션은 이미지명과 태그명을 붙이는 것으로, 실제 사용에서 거의 필수적으로 쓰인다.  
docker image bulid명령은 기본으로 Dockerfile이라는 이름으로 된 Dockerfile을 찾으며, 그 이외의 파일명으로 된 Docker file을 사용하려면 -f 옵션을 사용해야한다.  
~~~ 
docker image build -f Dockerfile-test -t example/echo:latest
~~~
--pull 옵션은 매번 베이스 이미지를 새로 받아오도록 한다.( 기본적으로 이미지를 빌드할 때, 매번 베이스 이미지를 받아오지 않는다.)

## 도커 컨테이너 다루기
겉에서 본 도커 컨테이너는 가상환경이다. 파일시스템과 어플리케이션이 함께 담겨있는 박스라고 보면된다.

### docker container run - 도커 컨테이너 생성 및 실행
~~~
docker container run [options] 이미지명[:태그] [명령] [명령인자...]
docker container run [options] 이미지ID [명령] [명령인자...]

ex) 

docker container run example/echo: latest

백그라운드에서 실행하고 싶으면,
docker container run -d example/echo:latest

docker container ls
로 실행중인 컨테이너 목록을 볼 수 있다.
~~~

--name 옵션을 사용하면 컨테이너에 원하는 이름을 붙일 수 있다.  
docker container run -t -d --name sehwan-echo example/echo:latest  


### docker container ls - 도커 컨테이너 목록 보기
~~~ 
docker container ls [options]

ID만 추출하기
docker container ls -q

컨테이너 목록 필터링 하기
docker container ls --filter "name=echo1"   (컨테이너를 생성하는 이미지를 기준으로 하려면 ancestor filter를 사용한다.)

종료된 컨테이너까지 포함된 목록 보기
docker container ls -a
 
~~~

### docker container stop - 컨테이너 정지하기
~~~
docker container stop 컨테이너ID_또는_컨테이너명

docker container stop -d -p 9000:8080 example/echo:latest
~~~

### docker container restart - 컨테이너 재시작하기
~~~
docker container restart 컨테이너ID_또는_컨테이너명

docker container restart echo
~~~

### docker container rm - 컨테이너 파기하기
~~~
docker container rm 컨테이너ID_또는_컨테이너명

docker container rm aaaaaaaaa
~~~

### docker container exec - 실행중인 컨테이너에서 명령 실행하기
docker container exec명령을 사용하면 마치 컨테이너에 ssh로 로그인한 것처럼 컨테이너 내부를 조작할 수 있다. 컨테이너 안에서 실행할 셸(sh나 bash)을 실행하면 마찬가지 결과를 얻을 수 있기 때문이다. 표준 입력 연결을 유지하는 -i 옵션과 유사 터미널을 할당하는 -t 옵션을 조합하면 컨테이너를 셸을 통해 다룰 수 있다. 이런 용도를 위해서는 무조건 -it 옵션을 붙인다.  

~~~
docker container exec [options] 컨테이너ID_또는_컨테이너명 컨테이너에서_실행할 명령

docker container exec -it echo sh
~~~


### 포트 포워딩
포트 포워딩이란, 호스트 머신의 포트를 컨테이너 포트와 연결해 컨테이너 밖에서 온 통신을 컨테이너 포트로 전달한다. 이 기능 덕분에 컨테이너 포트를 컨테이너 외부에서도 이용할 수 있다.  
포트 포워딩을 사용하기 전에 앞서 실행한 컨테이너를 다음과 같이 정지시킨다.  
~~~
docker container stop $(docker container ls 00filter "ancestor=example/echo" -q)
~~~
이후, 호스트 포트 9000을 컨테이너 포트 8080에 연결하도록 포트포워딩을 적용한다.  
~~~
docker container run -d -p 9000:8080 example./echo:latest

이후, curl을 이용하여 실행
curl http://localhost:9000/


호스트 포트를 아래와 같이 생략할 수 있다. 이경우에는 비포트가 ephemeral포트로 자동 할당된다.
docker container run -d -p 8080 example/echo:latest
~~~

## Docker 운영과 관리를 위한 명령
### prune - 컨테이너 및 이미지 파기
도커를 오랜기간 사용하다 보면, 디스크에 저장된 컨테이너와 이미지가 점점 늘어나게 마련이다. 이경우 prune 명령을 사용해 필요없는 이미지나 컨테이너를 일괄 삭제할 수 있다. prune 명령은 실행중이 아닌 모든 컨테이너를 삭제하는 명령이다.  
~~~
docker container prune

docker image prune

docker system prune
~~~

### docker container stats - 사용현황 확인하기
시스템 리소스의 사용 현황을 컨테이너 단위로 확인하려면 docker container stats 명령을 사용한다. (linux의 top과 비슷한 역할)
~~~
docker container stats
~~~

## 도커 컴포즈로 여러 컨테이너 실행하기
Docker는 어플리케이션 배포에 특화된 컨테이너다. 도커 컨테이너 = 단일 어플리케이션이라 봐도 무방하다. 가상 서버와는 대상 단위의 크기가 다르다.  
어플리케이션 간의 연동 없이는 실용적 수준의 시스템을 구축할 수 없다. 시스템을 수축하다보면 단일 컨테이너를 다룰 때는 문제가 되지 않았던 부분에도 주의가 필요하고, 컨테이너의 동작을 제어하기 위한 설정 파일이나 환경 변수를 어떻게 전달할지, 컨테이너 간의 의존관계를 고려할 때, 포트 포워딩을 어떻게 설정해야하는지 등의 요소를 적절히 관리해야 한다.  

* 이전 docker 명령어는 container 한개에 대한 명령어로, 여러 컨테이너를 다루는데에는 적합하지 않다. 여러 컨테이너에 대한 관리를 하려면, docker-compose를 사용한는 것이 수월하다.  

### Docker-compose 명령으로 컨테이너 실행하기
Docker compose는 yaml 포맷으로 기술된 설정 파일로, 여러 컨테이너의 실행을 한번에 관리할 수 있게 해준다.  

docker container run -d -p 9000:8080 example/echo:latest를 docker-compose에서 수행시키려면,  
docker-compose.yml 파일을 만들어 docker-compose up 명령을 실행시켜야 한다.  
  
<docker-compose.yml>
~~~
version: "3"
services:
 echo:
  image: example/echo:latest
  ports:
   - 9000:8080
~~~

이후 docker-compose up -d를 실행시킨다.  
컨테이너를 정지시키기 위해서는 docker-compose down을 사용한다.  

컴포즈를 사용하면 이미 존재하는 도커 이미지 뿐만 아니라, docker-compose up 명령을 실행하면서 이미지를 함께 빌드해 새로 생성한 이미지를 실행할 수도 있다. 이번에는 docker-compose.yml 파일에서 image속성을 지정하는 대신 build 속성에 Dockerfile이 위치한 상대 경로를 지정했다. 

~~~ 
version: "3"
services:
 echo:
  build: .
  ports:
   - 9000:8080
~~~

이후 docker-compose up -d --build를 사용한다.(--build 옵션은 도커 이미지를 강제로 다시 빌드하는 것으로, 이미지가 자주 수정하는 경우 사용하는 것이 좋다.)  
## docker compose 실습
컴포즈를 사용해 여러 컨테이너를 실행하기 위해 필요한 기본 요소를 파악하기 위해 젠킨스를 예제 삼아 컴포즈로 실행해 보겠다.

### 젠킨스 컨테이너 실행하기
다음과 같이 docker-compose.yml 파일을 작성한다.
~~~
version: "3"
services:
 master:
  container_name: master
  image: jenkinsci/jenkins:2.142-slim
  ports:
   -8080:8080
  volumes:
   - ./jenkins_home:/var/jenkins_home
~~~

젠킨스 이미지는 도커 허브에 올라와 있는 것을 이용하며,  
volumes 항목은 호스트와 컨테이너 사이에 파일을 복사하는 것이 아니라, 파일을 공유할 수 있는 메커니즘이다. 젠킨스 컨테이너를 호스트 쪽에서 편리하게 다룰 수 있도록 docker-compose.yml 파일에 volumes를 정의해 호스트 쪽 현재 작업 디렉터리 바로 아래에 jenkins_home 디렉터리를 젠킨스 컨테이너의 /var/jenkins_home에 마운트한다.  

이후 -d 옵션을 사용하지 않고 포어그라운드로 컨테이너를 실행하면, 초기 설정에서 패스워드가 생성되는데, 이 패스워드를 잘 복사해놓는다.  
여기서는 젠킨스를 호스트 쪽 포트 8080과 포트포워딩으로 연결했다. 브라우저에서 http://localhost:8080/에 접근한다. 앞에서 적어둔 초기 패스워드를 입력받는 페이지가 나타난다.  

젠킨스 셋업완료 후, 젠킨스 공식 이미지에서는 /var/jenkins_home 아래에 데이터가 저장된다. 그러므로 컴포즈로 실행한 젠킨스 컨테이너를 종료했다가 재시작해도 초기 설정이 유지된다.  

### 마스터 젠킨스용 SSH키 생성 
좀 더 실용적인 예제로, 슬레이브 젠킨스 컨테이너를 추가해보겠다. 보통, 관리기능이나 작업 실행 지시 등은 마스터 인스턴스가 맡고, 작업을 실제로 진행하는 것은 슬레이브 인스턴스가 담당한다. 이러한 구성으로 컴포즈를 만든다.  

먼저 준비작업으로, 마스터가 슬레이브에 접속할 수 있도록 마스터 컨테이너에서 SSH 키를 생성한다. 나중에 마스터가 슬레이브와 소통할 수 있으려면 이 키가 반드시 필요하므로 꼭 만들어야 한다. 현재 실행중인 첫번째 컨테이너가 마스터 젠킨스 역할을 한다.

~~~
# 마스터 컨테이너에 접속한 다음 SSH키 생성
$ docker container exec -it master ssh-keygen -t rsa -C ""

~~~

### 슬레이브 젠킨스 컨테이너 생성
슬레이브 인스턴스 역할을 할 젠킨스 컨테이너를 추가한다. 마스터 컨테이너는 master, 슬레이브 컨테이너는 slave01로 각각 이름을 붙였다. SSH_PUBKEY는 호스트 파일 시스템의 ./jenkins_home/.ssh/id_rsa.pub에서 찾아올 수 있다. 슬레이브 컨테이너 안에서 키를 받아오거나 설정해서는 안되며, 외부 환경 변수로 받아오게 해야한다. 
~~~
version: "3"
services:
 master:
  container_name: master
  image: jenkinsci/jenkins:2.142-slim
  ports:
   - 8080:8080
  volumes:
   - ./jenkins_home:/var/jenkins_home
  links:
   - slave01

  slave01:
   container_name: slave01
   image: jenkinsci/ssh-slave
   environment:
    - JENKINS_SLAVE_SSH_PUBKEY=ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDkEiAV7/+7F0TAWOTfA9SrlkmJKOzrikAjhssL+mBvjz9ehBGjX4FnlI99iy/zQ3F6yDcKEKcX/qlccio5dDoudiUgS868bnzbURi4ys+5drukI7EV+OoQ3BIcZ2XOWGCCSmClxm02E9DEF0kLbdzfZ0Zx1MkE2le1tHygQtESXxLBmHVhQn7cRedC/Lthi53xCitctoXDyeXVwXqrHrrhWlfsRl3F+3mihOJGIVkDBxHLh4cn9GIa0JLlOyC8oPERgNOQJnOs9XFfqNMthslLm5smcRk0IAktbNq0sr+2kZKGAA5UOqHegW1ICgDMDYPTyIcAWFYuCTyvGTGkE+Cp

~~~

마스터 컨테이너가 어떻게 슬레이브 컨테이너를 찾아 추가할까? IP 주소를 찾아 설정하는 방법도 있지만, 컴포즈를 사용하면 좀 더 깔끔하게 이 문제를 해결할 수 있다. links 요소를 사용해 다른 services 그룹에 해당하는 다른 컨테이너와 통신하면 된다.  

### 컨테이너 간의 관계정리 및 준비완료
-- 지금까지 진행한 과정을 한번 더 되짚어본다.  
* 마스터 컨테이너를 먼저 생성한 다음, 마스터의 SSH 공개키 생성
* docker-compose.yml 파일에 슬레이브 컨테이너를 추가하고, 앞에서 만든 마스터의 SSH 공개키를 환경변수 JENKINS_SLAVE_SSH_PUBKEY에 설정
* links 요소를 사용해 마스터 컨테이너가 슬레이브 컨테이너로 통신할 수 있게 설정  


docker-compose.yml 파일 작성후, docker-compose up -d 를 하여 컨테이너를 실행시킨다.  

### 마지막 설정
두컨테이너를 실행했다고 해서, 마스터 젠킨스가 슬레이브 젠킨스를 인식하지는 못한다. 이제, Jenkins 관리 페이지에서 '노드 관리'를 선택한 다음, 왼쪽 사이드 메뉴의 '신규 노드 생성' 항목에서 slave01을 추가한다. 
