#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

echo " 0.이벤트훅 AfterInstall stop.sh 파일이 실행-------------------------------"
echo " 0.이벤트훅 AfterInstall stop.sh 파일이 실행 profile.sh에서 확인못해서 여기서 먼저확인"
RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://3.39.29.255/profile)
CURRENT_PROFILE=$(curl -s http://3.39.29.255/profile)
echo "-----------------------------------------------------------------------------"
echo " 0.이벤트훅 AfterInstall stop.sh 파일이 실행 RESPONSE_CODE값 확인 -> $RESPONSE_CODE "
echo "-----------------------------------------------------------------------------"

echo " 0.이벤트훅 AfterInstall stop.sh 파일이 실행 CURRENT_PROFILE값 확인 -> $CURRENT_PROFILE "
echo "-----------------------------------------------------------------------------"

echo " 0.이벤트훅 AfterInstall stop.sh 파일이 실행 위의 값이 real1 이라면 아래의 IDLE_PORT 값이 real2가 된다. 아니면 real1이 된다."
echo "-----------------------------------------------------------------------------"

IDLE_PORT=$(find_idle_port)

echo " 0.이벤트훅 AfterInstall stop.sh 파일이 실행-------------------------------"
echo " 1.이벤트훅 AfterInstall stop.sh 파일이 실행 $IDLE_PORT 에서 구동중인 애플리케이션 PID확인"
IDLE_PID=$(lsof -ti tcp:${IDLE_PORT})

echo "-----------------------------------------"
echo " 2.이벤트훅 AfterInstall stop.sh 파일이 실행 IDLE_PID값 -> $IDLE_PID"

if [ -z ${IDLE_PID} ]
then
  echo "--------------------------------------"
  echo " 3.이벤트훅 AfterInstall stop.sh 파일이 실행 현재 구동중인 애플리케이션이 없기에 종료하지 않는다."
else
  echo "--------------------------------------"
  echo " 3.이벤트훅 AfterInstall stop.sh 파일이 실행 현재 구동중인 애플리케이션 존재하기에 해당 프로세스 종료"
  kill -15 ${IDLE_PID}
  sleep 5
fi
