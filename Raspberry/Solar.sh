#!/bin/bash
cd /nfs/Python/SolarLogger

while :
do
  HOUR=$(date +%H)
  
  if [ $HOUR -gt 4 ] && [ $HOUR -lt 22 ] ; then
	python Serial.py	
    sleep 300
  else
    exit
  fi
done