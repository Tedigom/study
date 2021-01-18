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
