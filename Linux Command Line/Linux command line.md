# Linux Command Line
## Basic  
**cd , ls, mkdir, touch는 생략 **  
**pwd(print working directory)** - 현재 작업중인 디렉토리 정보 출력  
`$ pwd
/home/student-00-497f1d0b47c1`  
**cp(copy)** - 파일 혹은 디렉토리를 복사, 디렉토리를 복사할때는 -r 옵션을 주어야한다.  
`$ cp testfile testfile_cp1   -  같은 디렉토리에서 파일 (testfile)을 복사한다.`  
`$ cp testfile ~/test2  - 파일(testfile)을 ~/test2 directory에 복사한다.  `  
**rm(remove)** - 파일이나 디렉토리를 삭제하며, 디렉토리를 삭제할때는 -r 옵션을 주어야한다.  
`rm -f testfile1  # -f 옵션은 사용자에게 삭제 여부를 물어보지 않고 삭제한다.  
rm -r testdir # 디렉토리를 삭제할 때는 -r옵션을 주어야한다. `  

**cat(concatenate)** - 파일의 내용을 출력하거나, 여러 파일을 합쳐 하나의 파일로 만들 수도 있다.  
`$ cat testfile  
11111`  

**head** - 파일의 앞부분을 보고싶은 줄 수만큼 보여준다. 옵션을 지정하지 않으면 파일 상위 10줄을 보여준다.  
`$ head -3 testfile  
1  
2  
3`    

**tail** - 파일의 뒷부분을 보고싶은 줄 수만큼 보여준다. 옵션을 지정하지 않으면 파일 하위 10줄을 보여준다.  
`$ tail -4 testfile  
7  
8  
9  
10  `  

`$ tail -F testfile  # -F 적용시, 명령어가 종료되지 않고 계속 해당 화면을 출력하며, 파일 내용 변경시 자동으로 갱신해준다.  
7  
8  
9  
10  
11  
12  `  



