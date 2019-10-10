#!/bin/bash
latestbuildFile=$(ls -tr ../build/libs/lite-bank-*-all.jar | tail -n 1)

cp $latestbuildFile ./lite-bank.jar

docker build -t lite-bank .
