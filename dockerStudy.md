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


