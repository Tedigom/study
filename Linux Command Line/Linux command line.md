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


**grep** - 텍스트 검색 기능을 가진 명령어이다. global | regular expression | print에서 따온 말이다.
` $ grep [options] [pattern] file`  


#    

## 프로세스

**at** - 단발적인 예약작업을 사용하기 위해  at를 사용하여 편리하게 예약작업을 만들어준다.  
`$ 명령어 | at 00:00 0000-00-00
$ 명령어 | at 00:00
$ at 0000-00-00 00:00 스크립트.sh`  



> http://throughkim.kr/2017/01/09/linux-14/
