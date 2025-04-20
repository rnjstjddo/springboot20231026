#!/bin/bash

REPOSITORY=/home/ubuntu/app/step2
PROJECT_NAME=springboot231026
echo "---------------"
echo "1.build 파일복사"

cp /home/ubuntu/zip/*.jar $REPOSITORY/

echo "-------------------"
echo "2.현재구동중인 애플리케이션 PID확인"

CURRENT_PID=$(pgrep -fl springboot231026 | grep jar | awk '{print $1}')

echo "-----------------------------------"
echo "3.현재 구동중인 애플리케이션PID -> $CURRENT_PID "

echo "-----------------------------------"
if [ -z "$CURRENT_PID" ]; then
        echo "4.현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
        echo "---------------------------------------------------------"
else
        echo "4.현재 구동중인 애플리케이션이 존재하기에 종료합니다."

        echo "---------------------------------------------------------"
        echo "kill -15 $CURRENT_PID"
        kill -15 $CURRENT_PID
        sleep 5
fi

echo "------------------------------"
echo "6.새버전 애플리케이션 배포시작"
echo "-------------------------------"

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "7.실행파일 .jar 퐉인 -> $JAR_NAME"
echo "-------------------------"

echo "8.실행권한 .jar파일에 주기"
chmod +x $JAR_NAME

echo "-------------------------"
echo "9.$JAR_NAME 실행"

nohup java -jar \
        -Dspring.config.location=classpath:/application.properties,classpath:/application-real.properties,/home/ubuntu/application-oauth.properties,/home/ubuntu/application-real-db.properties \
        -Dspring.profiles.active=real \
        $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &



