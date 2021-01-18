[꿀팁] 고성능 Nginx를 위한 튜닝 게시물 참조하여 정리

# 1. 디스크의 I/O 병목 줄이기
## file system 마운트 옵션 수정 (noatime과 nodirtime 추가)
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
