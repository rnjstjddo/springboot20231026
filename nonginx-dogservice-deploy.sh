
#!/bin/bash

IS_GREEN=$(docker ps | grep green)
echo "----------------------------------------------------------"
echo "0.현재 nonginx_dogservice_deploy.sh 파일 IS_GREEN 변수값 -> $IS_GREEN"

if [ -z "$IS_GREEN"  ]; then # blue라면
        echo "1.현재 blue컨테이너 동작중 -> green컨테이너에 새로운 버전 배포필요"
        echo "2.도커허브에서 이미지내려받기"
        sudo docker-compose pull green #green으로 이미지를 내려받습니다.
        echo "3.새버전 green 컨테이너 실행"
        sudo docker-compose up -d --build green #green컨테이너 실행

        while [ 1 = 1 ]; do

        echo "4.새버전 green 컨테이너 상태체크중"
        sleep 5

        REQUEST=$(curl http://localhost:8082) # green으로 request

        echo "5.새버전 green컨테이너실행 후8082 포트 확인 -> $REQUEST "

        if [ -n "$REQUEST" ]; then # 서비스 가능하면 health check 중지
                echo "6.새버전 green 컨테이너 동작확인 완료"

                echo "7.엔진엑스 리로드위한 포트설정8082 green 향하도록 수정"
                #sudo docker exec -it ngnix /bin/bash
                sudo sed -i "s/8081/8082/g" /etc/nginx/conf.d/service-url.inc
                sudo service nginx reload

                echo "8.이전 버전인 blue컨테이너 종료"
                docker-compose stop blue

                break;
        else
                echo "6.새버전 green컨테이너 응답없기에 구 버전인 blue컨테이너로 운영"
                break;
        fi
        done;


else

        echo "1.현재 green컨테이너 동작중 -> blue컨테이너에 새로운 버전 배포필요"
        echo "2.도커컴포즈에서 컨테이너 blue만 가져오기"
        sudo docker-compose pull blue

        echo "3.새버전 blue 컨테이너 실행"
        sudo docker-compose up -d --build blue #서비스명이 끝에와야한다.

        while [ 1 = 1 ]; do

        echo "4.새버전 blue 컨테이너 상태체크중"
        sleep 5

        REQUEST=$(curl http://localhost:8081) # blue로 request

        echo "5.새버전 blue컨테이너 실행후 8081포트확인 -> $REQUEST "

        if [ -n "$REQUEST" ]; then # 서비스 가능하면 health check 중지
                echo "6.새버전 blue 컨테이너 동작확인 완료"
                break;

                echo "7.엔진엑스 리로드위한 포트설정 8081 blue 향하도록 수정"
                #sudo docker exec -it ngnix /bin/bash
                sudo sed -i "s/8082/8081/g" /etc/nginx/conf.d/service-url.inc

                echo "8.엔진엑스 리로드"
                sudo service nginx reload

                echo "9.이전 버전인 green 컨테이너 종료"
                docker-compose stop green

        else
                echo "6.새 버전인 blue컨테이너 응답 없기에 이전 버전인 green컨테이너로 운영"
                break;
        fi
        done;

fi

