[꿀팁] 고성능 Nginx를 위한 튜닝 게시물 참조하여 정리

# 1. 디스크의 I/O 병목 줄이기
### 1.a. file system 마운트 옵션 수정 (noatime과 nodirtime 추가)
약 시스템 성능의 10~30% 개선 효과가 있음.  
시스템 코어에 필요한 I/O 입출력을 줄여서 속도를 향상시키는 것 ( 디스크 입출력 메타데이터 기록 X )  

~~~
#@ 파일시스템 마운트 옵션을 수정 합니다.
# vi /etc/fstab
UUID=b373f8f4-c3f0-4bed-b582-3e354d14548f / xfs defaults,noatime 0 0 
/dev/sda1 /dbdata           ext4    defaults,noatime,nodirtime 1 2
/dev/sdb1 /var/lib/docker  ext4    defaults,noatime,nodirtime 1 2

#@ 이미 마운트된 상태에서 /data 디스크의 마운트 옵션을 바꿉니다.
# mount -o remount /data

#@ 변경된 마운트 상태를 확인 합니다.
# cat /proc/mounts
/dev/sda1 /data ext4 rw,noatime 0 
~~~

# 2. 프로세스 처리량 늘리기
### 2.a. 리눅스 커널 파라미터 튜닝하여 동시처리량 늘리기
#### fs.file-max 모든 프로세스에 대해 열린 파일의 최대수를 지정한다.
파일을 open 뿐만 아니라 대량의 네트워크 호출시 TCP 커넥션의 소켓 수도 파일 핸들로 취급됨.  

~~~
[fs.file-max 수정]
# echo 65536 > /proc/sys/fs/file-max 
# sysctl -w fs.file-max=65536 

[재부팅시에도 계속 값이 적용 하는 방법] : /etc/sysctl.con에 수정 / 추가를 합니다
# echo "fs.file-max=65536" >> /etc/sysctl.conf
# sysctl -a 
~~~

#### /etc/security/limits.conf 계정 환경에 대한 보안 제한 설정을 변경한다.
~~~
[/etc/security/limits.conf 수정]
# nofile : max number of open file descriptors
# nproc : max number of processes
# soft: enforcing the soft limits
# hard : enforcing the hard limits


[ nginx 계정에 대한 제한 설정]
nginx hard  nproc 10240
nginx soft   nproc 10240
nginx hard  nofile 204800
nginx soft   nofile 204800

~~~

### 2.b. Nginx 워크프로세스와 백로그를 이용한 대량 트래픽 처리
~~~
user  nginx [nobody];   # user [group] 으로 default nobody [nobody] 입니다


[Nginx config의 공통영역]
worker_processes 6;  # [auto | cpu core 수] : 10~20%의 코어는 운영체제를 위해 남겨둠
worker_rlimit_nofile 204800;


[Nginx config의 event 처리 영역]
worker_connections 8192;     # 동시에 8192개의 요청을 받을수 있음
multi_accept        on;          # 순차적으로 요청을 받지 않고 동시에 요청을 접수.
use                 epoll;          # Linux 2.6+ 이상에서 사용하는 효율적인 이벤트 처리 방식


[Nginx config의 http 영역]
sendfile on;  # 커널 내에서 하나의 FD와 다른 FD간에 데이터를 복사함. # read () + write ()
tcp_nopush on;  # 응답 헤더를 TCP packet 한 조각으로 보냅니다.
tcp_nodelay on ;  # 전송된 데이터를 버퍼링하지 않음, 실시간으로 느린 네트워크에서 작은 패킷 문제를 해결
reset_timedout_connection on ;  # 서버가 응답하지 않는 클라이언트에서 연결을 닫을 수 있도록 허용 합니다.
client_body_timeout 10 ;  # 요청 시간 초과-기본값 60 입니다.
send_timeout 2 ;  # 클라이언트가 응답을 중지하여 메모리를 확보합니다. 기본값 60
keepalive_timeout 30 ;  #이 시간이 지나면 서버가 연결을 종료합니다. 기본값 75
keepalive_requests 100000 ;  # 테스트 환경의 경우 - 클라이언트가 연결 유지를 통해 수행 할 수있는 요청 수입니다.
reset_timedout_connection on;  # 닫힌 소켓이 FIN_WAIT1 상태로 오랫동안 유지되는 것을 방지 할 수 있습니다.
client_body_timeout 10;   [default 60초]
send_timeout 2;  [default 60초]   #클라이언트에 응답을 전송하기위한 시간 제한시간이며, 응답없거나 느린 클라이언트를 제한


[Nginx config의 server 영역]
user  nginx;              # default nobody

worker_processes 6;  # [auto | cpu core 수]
worker_rlimit_nofile 204800;

pid /var/run/nginx.pid; 
error_log /var/log/nginx.error_log debug; 
                  # [ debug | info | notice | warn | error | crit ] 
# 

events {
    worker_connections 8192;      [4096 ~ 8192 정도]
    multi_accept     on;
    use                 epoll;
                     # use [ kqueue | epoll | /dev/poll | select | poll ]; 
}

http {
    include conf/mime.types;
    default_type application/octet-stream;
    sendfile on;
    tcp_nopush on;
    tcp_nodelay on;
    keepalive_timeout 15;
    keepalive_requests 100000;

    reset_timedout_connection on;
    client_body_timeout 10;
    send_timeout 2;


     ..... 생략 ....
  server {
          listen one.example.com backlog=8192; 
          server_name one.example.com www.one.example.com;
          .... 생략 ....

         # serve static files
         location  ~ ^/(images|javascript|js|css|flash|media|static)/ {
              root   /data/example/static;
              expires 30d;
         }

         location / {
          .... 생략 ....
         }
         location /app { 
          .... 생략 ....
        }
   }
}
~~~

### 2.C. Thread Pool 기술을 통한 일정한 처리 속도 향상
Nginx 스레드풀은 풀링을 이용하여 대량의 처리를 보다 안정적으로 처리할 수 있다. 그리고 None Block 입출력으로 async I/O인 aio(Nginx Plus) 방식 또한 쓰레드와 함께 활용시 프로세스간 경합을 줄이고 성능을 극대화할 수 있다.

~~~
# in the 'main' context
thread_pool default threads=32 max_queue=65536;

# in the 'http', 'server', or 'location' context
aio threads=default;

# 쓰레드 풀을 두가지로 만들어 특정 URI 별로 별도의 풀이 작동되도록 구성할 수 있다.
# in the 'main' context
thread_pool  pool_one threads=128 max_queue=0;
thread_pool  pool_two threads=32 max_queue=65536; 
http {
    server {
        location /one {
            aio threads=pool_one;
        }
        location /two {
            aio threads=pool_two;
        }
    }
    #...
}

~~~

# 3. TCP 관련 처리량 늘리기
### 3.a. 수신 연결 수 최대화 하기(리눅스 커널 파라미터)
**TCP 수신 대기열 netdev_max_backlog** : 높은 트래픽을 처리하는 서버에서 처리 대기중인 패킷의 수를 최대화 하도록 증가하여야 함.  
**TCP 백로그 대기열 tcp_max_syn_backlog** : TCP 요청으로 유입되는 대량의 SYN 패킷 요청을 수용하도록 백로그 큐를 늘려줘야함. Accept ACK가 수신 및 처리 될 때까지 수락 큐로 이동되지 않고 큐에서 대기. (synflood 방지)  
**TCP SYN 쿠키 tcp_syncookies, tcp_syn_retries** : SYN 쿠키가 활성화 된 경우 백로그 큐가 가득 찼을 경우에도 정상적인 접속 요구를 계속 받아들일 수 있도록 해주므로, SYN_Flooding 공격에 대비한 가장 효과적인 방법이다.  
**TCP 수락 대기열 somaxconn** : somaxconn는 애플리케이션이 TCP listen()으로 열리는 수락 큐 크기이다. 리스너의 수락 큐에 있을 수 있는 최대 연결 제한을 충분히 늘려준다.  
**네트워크 메모리 설정 net.core.Xmem_max, net.ipv4.tcp_Xme** : 네트워크 드라이버의 버퍼 메모리 확보를 위해 설정하며, 최근의 OS에서는 기본설정도 충분하므로, 별도 튜닝이 필요없을 수 있다.

~~~
# TCP 수신 대기열
net.core.netdev_max_backlog = 20480 

# TCP 백로그 대기열
net.ipv4.tcp_max_syn_backlog =20480 [ default 8096 ]
net.ipv4.tcp_syncookies = 1  [ default 0 ] 

# TCP SYN 쿠키
net.ipv4.tcp_syncookies = 1   [ default 0 ]
net.ipv4.tcp_syn_retries =  2   [ default 5 ]
net.ipv4.tcp_retries = 2        [ default 3 ]

# TCP 수락 대기열 
net.core.somaxconn = 8192   [ default 1024 ]

# 네트웍 메모리 설정
net.ipv4.tcp_window_scaling=1
net.core.rmem_max = 212992
net.core.wmem_max = 212992  
net.ipv4.tcp_rmem = 4096        87380   6291456
net.ipv4.tcp_wmem = 4096        87380   629145

# Time-wait 관련 설정
net.ipv4.tcp_tw_reuse = 1 
net.ipv4.tcp_timestamps = 1
net.ipv4.tcp_max_tw_buckets = 360000   [default 16384]
net.netfilter.nf_conntrack_buckets = 16384  [default 16384

# FIN_WAIT2 관련 설정
net.ipv4.tcp_fin_timeout = 5
net.ipv4.tcp_keepalive_time = 1200   [default 7200] # 첫 번째 연결 후 유지 프로브를 보내기까지 대기시간
net.ipv4.tcp_keepalive_intvl = 75   # default 75초 마다 응답 SYN에 대한 상태 확인을 하는 probe 주기 
net.ipv4.tcp_keepalive_probes = 9 # default 연속 9 회 동안 ACK 응답이 수신되지 않으면 연결이 끊어진 것으로 판단
~~~

### 3.b. 파일 연결수 늘리기(리눅스 환경 변수)
#### 오픈 가능한 파일 수 설정
* fs.file-max = 65536
* kernel.threads-max = 3261780 ( 시스템이 자동으로 설정되므로 수정하지 않는다. )

#### 사용자 제한 해제
~~~
# ulimit -n -u
open files (-n) 1024
max user processes (-u) 22474
~~~
고성능 서버의 경우 어플리케이션이 스레드가 동시에 처리할 수 있을 만큼 충분히 높게 설정한다.  
~~~
# cat /etc/security/limits.d/nginx.conf 
nginx soft  nofile  204800
nginx hard nofile  204800
nginx soft  nproc   64000
nginx hard nproc   64000
~~~

#### systemd (LimitNPROC) 를 이용한 프로세스 별 제한 해제하기
/usr/lib/systemd/system/<servicename>.service 파일을 수정하여 해당 서비스를 재기동한다.

~~~
[Unit]
Description=Some Daemon
After=syslog.target network.target

[Service]
Type=notify
LimitNOFILE=65536
ExecStart=/usr/sbin/somedaemon

[Install]
WantedBy=multi-user.target

데몬을 재기동 후 설정 상태를 확인한다.
해당 데몬으 ㅣ구동되는 PID로 limits 설정을 확인하면 적용된 상태를 알 수 있다.

# cat /proc/service_pid/limits | grep open    (PID가 2614인 데몬에 대한 설정 확인)

# cat /proc/2614/limits
Limit                     Soft Limit           Hard Limit           Units
Max processes             22474                22474                processes
Max open files            65536                65536                files
~~~

### 3.c. 리눅스 커널 튜닝 적용하기

리눅스 커널 파라미터는 /etc/sysctl.conf 나  /etc/sysctl.d/sys_tune.conf 파일로 관리된다. 주로 이 두개의 파일을 아래와 같이 수정하여 시스템 재기동 시 자동으로 로딩되도록 관리한다.

~~~
# cat /etc/sysctl.d/sys_tune.conf

# FS Tuneups
fs.file-max = 65536

# Socket Tuneups
net.core.netdev_max_backlog = 20480
net.core.somaxconn = 20480 

# NET Tuneups
net.ipv4.tcp_window_scaling=1
net.core.rmem_max = 16000000
net.core.wmem_max = 16000000
net.ipv4.tcp_rmem = 4096 250000 16000000
net.ipv4.tcp_wmem = 4096 250000 16000000

# TCP Tuneups
net.ipv4.tcp_max_syn_backlog = 204800
net.ipv4.tcp_syncookies = 1
net.ipv4.tcp_syn_retries = 2
net.ipv4.tcp_retries1 = 2

# TIME_WAIT
net.ipv4.tcp_tw_reuse = 1
# net.ipv4.tcp_tw_recycle =0  # CentOs8 deprecated
net.ipv4.tcp_max_tw_buckets = 360000  # [default 16384]
net.netfilter.nf_conntrack_buckets = 16384 # [default 16384]

# FIN_WAIT2
net.ipv4.tcp_fin_timeout = 15
net.ipv4.tcp_keepalive_time = 1200
~~~
위와 같이 파일을 수정 후 , "sysctl -p"라는 명령을 통해 sysctl.conf 관련 환경 파일을 런타임에서 리눅스 커널에 적용할 수 있다. (다만, 적용 후 해당 프로세스는 재기동을 해야한다.)


# 메모리 및 CPU 튜닝하기
CPU와 메모리의 튜닝은 대량의 처리를 위한 리소스 할당과 context 처리를 위한 방식, 그리고 사용중인 리소스의 빠른 반환과 재할당에 있다. 기존 Apache나 Multi-Thread 방식의 프로그램과는 달리, nginx는 이벤트 기반으 ㅣ처리기술을 도입하여 CPU와 Memory를 효과적으로 사용할 수 있다.

#### [이벤트 기반 처리 방식]
Nginx의 아키텍처의 개념은 모듈식, 이벤트 중심, 비동기, 단일스레드, 비 차단 개념으로 구성된다.  
이벤트 중심 아키텍처를 사용하여 요청을 비동기적으로 처리하며, 단일 스레드를 통해 새로운 요청에 대해 새 프로세스를 생성하지 않는다. 또 연결은 worker라고 하는 제한된 수의 단일 스레드 프로세스에서 매우 효율적인 실행 루프에서 이벤트 처리된다. 

상세내용 : [https://couplewith.tistory.com/entry/%EA%BF%80%ED%8C%81%EA%B3%A0%EC%84%B1%EB%8A%A5-Nginx%EB%A5%BC%EC%9C%84%ED%95%9C-%ED%8A%9C%EB%8B%9D4-%EB%A9%94%EB%AA%A8%EB%A6%AC-%EB%B0%8F-CPU-%ED%8A%9C%EB%8B%9D%ED%95%98%EA%B8%B0-Processor?category=212810](https://couplewith.tistory.com/entry/%EA%BF%80%ED%8C%81%EA%B3%A0%EC%84%B1%EB%8A%A5-Nginx%EB%A5%BC%EC%9C%84%ED%95%9C-%ED%8A%9C%EB%8B%9D4-%EB%A9%94%EB%AA%A8%EB%A6%AC-%EB%B0%8F-CPU-%ED%8A%9C%EB%8B%9D%ED%95%98%EA%B8%B0-Processor?category=212810)
