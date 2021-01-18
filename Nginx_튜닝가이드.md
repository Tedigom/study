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
