
#!/bin/bash

IS_GREEN=$(docker ps | grep green)

echo "현재dogservice_deploy.sh 파일 IS_GREEN 변수값 -> $IS_GREEN"

if [ -z "$IS_GREEN"  ]; then # blue라면
        echo "현재 blue컨테이너 동작중 -> green컨테이너에 새로운 버전 배포필요"
        echo "1.도커허브에서 이미지내려받기"
        docker-compose -f /home/ubuntu/docker-compose.yml pull green #green으로 이미지를 내려받습니다.
        echo "2.새버전green 컨테이너 실행"
        docker-compose up -d green #green컨테이너 실행

        while [ 1 = 1 ]; do

        echo "3.새버전green 컨테이너 상태체크중"
        sleep 5

        REQUEST=$(curl http://127.0.0.1:8082) # green으로 request

        echo "새버전green컨테이너실행 후8082 포트 확인 -> $REQUEST "

        if [ -n "$REQUEST" ]; then # 서비스 가능하면 health check 중지
                echo "새버 green 컨테이너 동작확인 완료"

                echo "4.엔진엑스 리로드위한 포트설정8082 green 향하도록 수정"
                sudo docker exec -it ngnix /bin/bash
                sudo sed -i "s/8081/8082/g" /etc/nginx/conf.d/service-url.inc
                sudo nginx -s reload

                echo "5.이전 버전 blue컨테이너 종료"
                docker-compose stop blue

                break;
        else
                echo "새버전 green컨테이너 응답없음 구버전blue컨테이너로 운영"
                break;
        fi
        done;


else

        echo "현재 green컨테이너 동작중 -> blue컨테이너에 새로운 버전 배포필요"
        echo "1.도커컴포즈에서 컨테이너 blue만 가져오기"
        docker-compose -f /home/ubuntu/docker-compose.yml pull blue

        echo "2.새버전blue 컨테이너 실행"
        docker-compose up -d blue

        while [ 1 = 1 ]; do

        echo "3.새버전blue 컨테이너 상태체크중"
        sleep 5

        REQUEST=$(curl http://127.0.0.1:8081) # blue로 request

        echo "새버전blue컨테이너 실행후 8081포트확인 -> $REQUEST "

        if [ -n "$REQUEST" ]; then # 서비스 가능하면 health check 중지
                echo "새버전blue 컨테이너 동작확인 완료"
                break;

                echo "4.엔진엑스 리로드위한 포트설정 8081 blue 향하도록 수정"
                sudo docker exec -it ngnix /bin/bash
                sudo sed -i "s/8082/8081/g" /etc/nginx/conf.d/service-url.inc

                echo "5.엔진엑스 리로드"
                sudo nginx -s reload

                echo "5.이전버전green 컨테이너 종료"
                docker-compose stop green

        else
                echo "새버전blue컨테이너 응답 없음 이전버전 green컨테이너운영"
                break;
        fi
        done;

fi

