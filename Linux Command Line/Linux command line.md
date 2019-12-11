# Linux Command Line
## 파일시스템  
**cd , ls, mkdir, touch는 생략 **  
**pwd(print working directory)** - 현재 작업중인 디렉토리 정보를 출력합니다.
`$ pwd
/home/student-00-497f1d0b47c1`  
**cp(copy)** - 파일 혹은 디렉토리를 복사, 디렉토리를 복사할때는 -r 옵션을 주어야합니다.  
`$ cp testfile testfile_cp1   -  같은 디렉토리에서 파일 (testfile)을 복사합니다.`  
`$ cp testfile ~/test2  - 파일(testfile)을 ~/test2 directory에 복사합니다.  `  
**rm(remove)** - 파일이나 디렉토리를 삭제하며, 디렉토리를 삭제할때는 -r 옵션을 주어야합니다.  
`rm -f testfile1  # -f 옵션은 사용자에게 삭제 여부를 물어보지 않고 삭제합니다.  
rm -r testdir # 디렉토리를 삭제할 때는 -r옵션을 주어야합니다. `  

**cat(concatenate)** - 파일의 내용을 출력하거나, 여러 파일을 합쳐 하나의 파일로 만들 수도 있습니다.  
`$ cat testfile  
11111`  

**head** - 파일의 앞부분을 보고싶은 줄 수만큼 보여준다. 옵션을 지정하지 않으면 파일 상위 10줄을 보여줍니다.  
`$ head -3 testfile`     

**tail** - 파일의 뒷부분을 보고싶은 줄 수만큼 보여준다. 옵션을 지정하지 않으면 파일 하위 10줄을 보여줍니다.  
`$ tail -4 testfile`   

`$ tail -F testfile  # -F 적용시, 명령어가 종료되지 않고 계속 해당 화면을 출력하며, 파일 내용 변경시 자동으로 갱신해줍니다.`   

**df** - 파일 시스템 구조와 용량등을 보여줍니다.  

**du** - 파일 size를 kbyte단위로 보여줍니다.  

**mount** - 운영체제에게 파일 시스템이 사용준비가 되었음을 지시하고, 이를 파일 시스템 계층 구조에 있는 특정 지점에 연결시킨 다음, 접근에 관련한 옵션을 설정한다. 마운트를 통해 파일시스템, 파일, 디렉토리, 장치, 특수파일을 사용할 수 있게 한다.  



#     

## 검색

**find** - 특정 파일이나 디렉토리를 검색합니다.  
` $ find [검색경로] -name [파일명]`  

exec 옵션을 사용하여 바로 실행시킬 수도 있습니다.  
`$ find ./ -name "*.jpg" -exec rm {} \;`  

wc -l 옵션을 함꼐 사용하여 숫자로 간편하게 알아볼 수도 있습니다.  
`$ find ./ -type f | wc -l`  


**grep** - 텍스트 검색 기능을 가진 명령어입니다. global | regular expression | print에서 따온 말입니다.
` $ grep [options] [pattern] file`  


#    

## 프로세스

**at** - 단발적인 예약작업을 사용하기 위해 at를 사용하여 편리하게 예약작업을 만들어줍니다.  
`$ 명령어 | at 00:00 0000-00-00 `  
`$ 명령어 | at 00:00 `  
`$ at 0000-00-00 00:00 스크립트.sh`  

atq를 통해 at 예약 목록을 확인합니다.  
atrm를 통해 at 예약을 지웁니다.  

**cron** - 반복적인 예약작업을 수행해주는 데몬입니다.  
**kill** - 프로세스에 시그널을 보내어 프로세스를 종료시키는 명령어입니다.  
`$ kill [SIGNAL] PID`  
`$ kill -9 PID   # 응답없어도 강제종료`  
`$ kill -15 PID  # 일반적 종료`  

**nice** - 특정 프로세스가 실행될 때 우선순위를 지정하여서 실행속도를 높여주는 도구입니다.기본 nice값은 부모 프로세스의 것을 따르며 보통 0으로 시작합니다. 일반적으로 프로세스가 가질 수 있는 nice값 범위는 -20~19이며, -20이 가장 우선순위가 빠르고 +19는 가낭 낮은 우선순위를 의미합니다.  

`$ nice -n [nice값] 프로세스`  
`$ renice -n [nice값] -p [PID]   #renice는 이미 실행중인 프로세스의 nice값을 변경해줄 수 있습니다.`  

**ps(process status)** - 시스템에서 현재 수행되고 있는 프로세를 확인하는 기초 명령어입니다.  

**top** - 시스템의 운용사항을 실시간으로 모니터링할 수 있는 유틸리티입니다. CPU, 메모리, 프로세스등을 확인할 수 있습니다. 하지만, 네트워크 부하율은 표시되지 않습니다.  


## 네트워크
**netstat** - 네트워크 접속, 라우팅 테이블, 네트워크 인터페이스의 통계 정보를 보여주는 도구입니다.
netstat의 옵션은 아래와 같습니다.  
#   
`-a(all): 모든 소켓 보기, 이 옵션이 없으면 Established 상태만 출력됩니다.`  
`-c(continuous): 실행명령을 매 초마다 실행합니다`  
`-e(extend): 확장 정보 추가, User부분과 Inode열이 추가됩니다.`  
`-l(listening): Listening상태인 소켓 리스트만 보여줍니다.`  
`-n(numeric): 도메인 주소를 읽지않고 숫자로 출력합니다`  
`-p(program): PID와 사용중인 프로그램명이 출력합니다.`  
`-r(route): 라우팅 테이블 보기`  
`-t(tcp)`  
`-u(udp)`  

**nslookup(nameserver lookup)** -네임 서버 관련된 조회를 할 수 있는 명령어입니다. 사용법은 아래와 같습니다  
`$ nslookup google.com           # 구글의 ip확인`  
`$ nslookup -query=mx google.com # 구글의 MX확인`  
`$ nslookup -type=ns google.com  # NS레코드로 DNS목록 확인`  
`$ nslookup 209.132.183.181      # Reverse DNS lookup`  

**ssh(secured shell)** - 네트워크를 통해 다른 컴퓨터로 접근하거나 그 컴퓨터에서 명령 실행등을 할 수 있도록 해줍니다.  
기본적으로 22번 포트로 접속을 시도하며, 다른 포트를 이용하려면 -p옵션을 사용합니다. ssh명령어 뒤에 명령어를 추가하여 원격으로 접속하여 특정 작업을 수행할 수 있습니다.  


## 패키지 관리자

**yum** - yum의 명령어는 다음과 같습니다.  
`$ yum check-update      # 업데이트가 필요한 패키지 체크`  
`$ yum install package   # 패키지를 인스톨`  
`$ yum list              # 설치된 패키지의 리스트`  
`$ yum update package    # 패키지 업데이트`  
`$ yum upgrade package   # 패키지 업그레이드`  
`$ yum remove package    # 패키지 삭제`  

**apt-get** - apt-get의 명령어는 다음과 같습니다.  
`$ apt-get update           # 패키지 인덱스 정보 업데이트(/etc/apt/sources.list)`  
`$ apt-get upgrade          # 설치된 패키지 업그레이드`  
`$ apt-get install package  # 패키지 설치`  
`$ apt-get remove package   # 패키지 삭제`  

**rpm**  

## 일반적 지식
**리다이렉션** - 표준입출력 기능은 입/출력을 다루는 기본적인 방법을 제공합니다.  
* 표준출력(stdout): 정상적인 출력
* 표준에러(stderr): 에러메세지
* 표준입력(stdin): 특별한 지정이 없으면 키보드로부터 입력을 읽어들임  

`$ cat afile bfile`  
`this is afile`  
`this is bfile`  
`$ cat afile bifle >cfile # 표준 출력을 cfile로 리다이렉트`  
`$ cat cfile`  
`this is afile`  
`this is bfile`  

**히스토리** - 히스토리를 이용해 이전에 입력했던 명령어를 반복하거나, 변경하여 다시 사용할 수 있습니다.  
`$ history 4`  
`2117 ls`  
`2118 grep HISTSIZE ~/.bashrc`  
`2119 ll`  
`2120 cd ..`  

**작업제어** 
`command &`  - 백그라운드에서 command를 실행합니다. 포그라운드에서 다른 작업을 실행할 수 있습니다.  
`CTRL + C` - INTR 시그널을 보내 포그라운드 작업을 죽입니다.  
`CTRL + Z` - TSTP 시그널을 보내 포그라운드 작업을 중지시킵니다.  
`suspend` - 쉘을 중지시킵니다.  
`stop` - 명령어 또는 같은 기능의 alias를 이용해 백그라운드 작업을 중지시킵니다.  
`bg %num` - 중지된 작업을 백그라운드에서 계속 실행되도록 합니다.  
`fg %num` - 백그라운드 작업이나 중지된 작업을 포그라운드로 가져옵니다.  
`kill &num` - 특정 백그라운드 작업을 중지시킵니다.  
`kill pid` - 프로세스 ID를 이용해 특정 백그라운드 작업을 종료시킵니다.  
`jobs` - 백그라운드 작업과 중지된 작업을 작업 번호와 함께 나열합니다.  
`set notify` - 작업의 상태가 바뀌면 즉시 통보합니다.  
`stty tostop` - 백그라운드의 작업들이 화면에 메세지를 출력하지 못하도록 합니다.  
`nohup command &` - 세션이 끊기더라도 백그라운드에서 작업을 계속합니다.  




> http://throughkim.kr/2017/01/09/linux-14/
