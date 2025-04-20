#!/usr/bin/env bash
#쉬고있는 profile찾기

function find_idle_profile(){
  #echo "------------------------------------------"
  #echo "현재 profile.sh 파일진입 여기서 쉬고있는 profile을 찾는다. "
  RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://3.39.29.255/profile)
  #echo "------------------------------------------"
  #echo "RESPONSE_CODE의 값 -> ${RESPONSE_CODE}"

  if [ ${RESPONSE_CODE} -ge 400 ] #400보다 클경우
  then
    #echo "------------------------------------------"
    #echo "상태코드가 400이상일경우 real2 설정파일을 선택한다. "
    CURRENT_PROFILE=real2
  else
    #echo "------------------------------------------"
    #echo "상태코드가 400미만이라면 /profile 로 다시 접속한다."
    CURRENT_PROFILE=$(curl -s http://3.39.29.255/profile)
  fi

  if [ ${CURRENT_PROFILE} == real1 ]
    #echo "------------------------------------------"
    #echo "CURRENT_PROFILE 값 -> ${CURRENT_PROFILE} "
  then
    #echo "------------------------------------------"
    #echo "다시 접속한 뒤 값이 real1 과 같다면 real2 로 변경한다. "
    IDLE_PROFILE=real2
  else
    #echo "------------------------------------------"
    #echo "다시 접속한뒤 값이 real1 과 다를경우 real1 로 설정한다. "
    IDLE_PROFILE=real1
  fi

  echo "${IDLE_PROFILE}"
  #echo "현재 엔진엑스와 연결되지 않는 profile 이다 -> ${IDLE_PROFILE}"
}

function find_idle_port(){

  #echo "------------------------------------------"
  #echo "profile.sh파일의 find_idle_port() 함수진입 "
  IDLE_PROFILE=$(find_idle_profile)
  #echo "------------------------------------------"
  #echo "find_idle_profile()함수의 결과로 가져온 값 -> ${IDLE_PROFILE} "

  if [ ${IDLE_PROFILE} == real1 ]
  then
    #echo "------------------------------------------"
    #echo "해당 값이 real1 과 같다면 8081을 반환한다."
    echo "8081"
  else
    #echo "------------------------------------------"
    #echo "해당 값이 real 과 다르다면 8082를 반환한다."
    echo "8082"
  fi
}