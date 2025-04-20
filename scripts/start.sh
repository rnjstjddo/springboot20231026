#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

REPOSITORY=/home/ubuntu/app/step3
PROJECT_NAME=springboot231026

echo "-----------------------"
echo " 0.이벤트훅 ApplicationStart start.sh 파일이 실행"
echo "-----------------------"
echo " 0.현재위치확인"
pwd
echo "-----------------------"
echo " 1.이벤트훅 ApplicationStart start.sh 파일이 실행 .jar 파일을 복사한다."
echo "-----------------------"
echo " 2.이벤트훅 ApplicationStart start.sh 파일이 실행 .jar 파일을 /home/ubuntu/app/step3의 경로로 복사한다."
cp $REPOSITORY/zip/*.jar $REPOSITORY/
echo "-----------------------"
echo " 3.이벤트훅 ApplicationStart start.sh 파일이 실행 새버전 배포한다."
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)
echo "-----------------------"
echo " 4.이벤트훅 ApplicationStart start.sh 파일이 실행 .jar파일명 -> $JAR_NAME"
echo "-----------------------"
echo " 5.이벤트훅 ApplicationStart start.sh 파일이 실행 .jar에 실행권한 추가"
chmod +x $JAR_NAME
echo "-----------------------"
echo " 6.이벤트훅 ApplicationStart start.sh 파일이 실행 .jar 실행전 profile 지정"
IDLE_PROFILE=$(find_idle_profile)
echo " 7.이벤트훅 ApplicationStart start.sh 파일이 실행 profile 의 값을 알기위해 find_idle_profile함수를 실행 -> $IDLE_PROFILE"
nohup java -jar \
              -Dspring.config.location=classpath:/application.properties,classpath:/application-$IDLE_PROFILE.properties,/home/ubuntu/application-oauth.properties,/home/ubuntu/application-real-db.properties \
              -Dspring.profiles.active=$IDLE_PROFILE \
              $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
