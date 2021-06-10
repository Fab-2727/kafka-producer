#!/bin/bash

mkdir -p logs
# Test without env vars
cd logs
logs_dir=$PWD
cd ../
COUNTER=0
SERVER_PORT=8080

printf "Script location: $PWD\n"
cd ../../..
printf "Maven dir:       $PWD\n"

last_pid=$(netstat -anp | grep -i "$SERVER_PORT" | grep -oE "[0-9]+/" | grep -oE "[0-9]+")
printf "$last_pid\n"
[[ ! -z "$last_pid" ]] && kill "$last_pid"

echo ""

{ mvn spring-boot:run | tee -a "$logs_dir""/mvn-log.txt" & }

tee_process_pid=$!
# 15 seconds until UP
sleep 16

function test-payload () {
    while [[ "$COUNTER" -lt 1 ]]
    do
        curl -X POST \
        -H 'Content-type: text/plain' \
        --data "[$COUNTER] Message send to Kafka Producer using 'Content-type: text/plain'." \
        'localhost:8080/api/v1/kafka/message'
        
        sleep 3
        
        curl -X POST \
        -H "Content-type: text/plain" \
        --data '{"dni":40111222,"phone_number":"1122334455","name":"John","lastname":"Doe","birthDate":"01/08/2009"}' \
        'localhost:8080/api/v1/kafka/message'
        
        COUNTER=$((COUNTER+1))
        sleep 3
    done
}

test-payload
sleep 4
kill "$tee_process_pid"
last_pid=$(netstat -anp | grep -i "$SERVER_PORT" | grep -oE "[0-9]+/" | grep -oE "[0-9]+")
kill "$last_pid"
exit 0;

# Implement JQ for JSON file.
