#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh
source ${ABSDIR}/switch.sh

IDLE_PORT=$(find_idle_port)

echo "-------------------------"
echo " 1.이벤트훅 ValidateService health.sh 파일이 실행"
echo "--------------------------------------"
echo " 2.이벤트훅 ValidateService health.sh 파일이 실행 realink -f $0 로 출력한 변수값 확인 -> $ABSPATH"
echo "-------------------------------------"
echo " 3.이벤트훅 ValidateService health.sh 파일이 실행 dirname 으로 변수값 확인 -> $ABSDIR"
echo "-------------------------------------"
echo " 4.이벤트훅 ValidateService health.sh 파일이 실행 find_idle_port함수 실행 결과 -> $IDLE_PORT"
echo "-------------------------------------"
echo " 5.이벤트훅 ValidateService health.sh 파일이 실행 해당 포트로 /profile 요청하기 http://localhost:$IDLE_PORT/profile"
sleep 10

for RETRY_COUNT in {1..10}
do
  RESPONSE=$(curl -s http://localhost:${IDLE_PORT}/profile)
  UP_COUNT=$(echo ${RESPONSE} | grep 'real' | wc -l)
  echo "------------------------------------"
  echo " 6.이벤트훅 ValidateService health.sh 파일이 실행 $IDLE_PORT /profile 요청한 결과값 -> $RESPONSE "
  echo "------------------------------------"
  echo " 7.이벤트훅 ValidateService health.sh 파일이 실행 real 설정파일 존재여부 -> $UP_COUNT "

  if [ ${UP_COUNT} -ge 1 ]
  then
    echo "----------------------------------"
    echo " 8.이벤트훅 ValidateService health.sh 파일이 실행 값의길이가 1보다 크기에 $IDLE_PORT 포트 상태체크 완료 리버스프록시수정 switch.sh의 함수호출"
    switch_proxy
    break
  else
    echo "----------------------------------"
    echo " 8.이벤트훅 ValidateService health.sh 파일이 실행 $IDLE_PORT 상태체크 확인결과 응답을 알수없거나 실행상태가 아님"
  fi

  if [ ${RETRY_COUNT} -eq 10 ]
  then
    echo "----------------------------------"
    echo " 9.이벤트훅 ValidateService health.sh 파일이 실행 1~10까지 종료 후 $IDLE_PORT 로 연결실패"
    echo " 10.이벤트훅 ValidateService health.sh 파일이 실행 엔진엑스에 연결하지 않고 배포를 종료합니다."
    exit 1
  fi

  echo "--------------------------------------"
  echo " 11.이벤트훅 ValidateService health.sh 파일이 실행 $IDLE_PORT 연결 재시도 중"
  sleep 10
done


