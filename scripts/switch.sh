#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

function switch_proxy(){
  IDLE_PORT=$(find_idle_port)

  echo "-----------------------------"
  echo " 1.switch.sh 파일 switch_proxy함수 진입 health.sh 파일에서 엔진엑스 포트변경을 위해 "
  echo "------------------------------"
  echo " 2.switch.sh 파일 switch_proxy함수 진입 전환할 포트 -> $IDLE_PORT"
  echo "------------------------------"
  echo " 3.switch.sh 파일 switch_proxy함수 진입 $IDLE_PORT 로 service-url.inc 파일 포트전환"
  echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc
  echo "------------------------------"
  echo " 4.switch.sh 파일 switch_proxy함수 진입 엔진엑스 리로드"
  sudo service nginx reload
  echo "------------------------------"
}