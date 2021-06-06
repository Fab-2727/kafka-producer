#!/bin/bash

# Test without env vars
# VARS
mkdir -p logs
counter=0
relative_dir="/logs/"
logs_dir=$PWD$relative_dir

printf "Script location: $PWD\n"
cd ../../..
printf "Maven dir:       $PWD\n"

last_pid=$(netstat -anp | grep -i "8080" | grep -oE "[0-9]+/" | grep -oE "[0-9]+")
echo "$last_pid"
kill "$last_pid"

echo ""

{ mvn spring-boot:run | tee -a "$logs_dir""mvn-logs.txt" & }

tee_process_pid=$!
# 15 seconds until UP
sleep 16

function test-payload () {
    while [[ "$counter" -lt 1 ]]
    do
        curl -X POST \
        -H 'Content-type: text/plain' \
        --data "[$counter] Message send to Kafka Producer using 'Content-type: text/plain'." \
        'localhost:8080/api/v1/kafka/message'
        
        sleep 3
        
        curl -X POST \
        -H "Content-type: text/plain" \
        --data '{"dni":40111222,"phone_number":"1122334455","name":"John","lastname":"Doe","birthDate":"01/08/2009"}' \
        'localhost:8080/api/v1/kafka/message'
        
        counter=$((counter+1))
        sleep 1
    done
}

test-payload
sleep 3
kill "$tee_process_pid"
last_pid=$(netstat -anp | grep -i "8080" | grep -oE "[0-9]+/" | grep -oE "[0-9]+")
kill "$last_pid"
exit 0;

# curl -X POST -H 'Content-type: text/plain' 'localhost:8080/api/v1/kafka/message' --data 'Hola mundo, probando'
# JQ
# A posteriori, deberia aceptar JSON con un serializer correspondiente.
# Por eso, usar JQ para in-line json, como tambien --data '@path-file' para la info
# man test


