#!/bin/bash

# Test without env vars
mkdir -p logs
counter=0
relative_dir="/logs/"
logs_dir=$PWD$relative_dir
printf "Script location: $PWD\n"
cd ../../..
printf "Maven dir:       $PWD\n"

# Start execution

printf "Starting execution\n"

echo ""

{ mvn spring-boot:run | tee -a "$logs_dir""mvn-logs.txt" & }

tee_process_pid=$!
# 15 seconds until UP
sleep 15
# We kill the process that writes to std_out
kill "$tee_process_pid"

function test-payload () {
    while [[ "$counter" -lt 3 ]]
    do
        curl -X POST \
        -H 'Content-type: text/plain' \
        --data "[$counter] Hola mundo, probando" \
        'localhost:8080/api/v1/kafka/message'
        counter=$counter+1
        sleep 4
    done
}

# curl -X POST -H 'Content-type: text/plain' 'localhost:8080/api/v1/kafka/message' --data 'Hola mundo, probando'
# JQ
# A posteriori, deberia aceptar JSON con un serializer correspondiente.
# Por eso, usar JQ para in-line json, como tambien --data '@path-file' para la info
# man test

exit 0;
