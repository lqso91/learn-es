#!/bin/bash
if [ $UID -eq 0 ]; then
   echo "Can not be run as root!"
   exit 1
fi

CFILE=kh_ydkh_add.conf
LFILE=kh_ydkh_add.log

LS_PID=$(ps -ef | grep logstash | grep $CFILE | awk '{printf $2}')

if [ "$1" = "stop" ]; then
    if [ -z "$LS_PID" ]; then 
        echo 'Logstash does not start.'
        exit 1
    fi
    
    echo -e "Stopping logstash ...\c"
    kill -TERM $LS_PID
    F=1
    cnt=0
    tput sc
    while [ $F -gt 0 ]; do
        sleep 1
        PID_EXIST=$(ps -f -p $LS_PID | grep $CFILE)
        if [ -z "$PID_EXIST" ]; then 
            F=0
        fi
        let cnt++
        tput rc
        tput ed
        echo -n "$cnt..."
        if [ $cnt -eq 30 ]; then
            kill -9 $LS_PID
        fi
    done
    echo -e "\nLogstash has stopped."
elif [ "$1" = "status" ]; then
    PID_EXIST=$(ps -ef | grep logstash | grep $CFILE | awk '{printf $2}')
    if [ -n "$PID_EXIST" ]; then
        echo "Logstash is running"
    else
        echo "Logstash is not running"
    fi
elif [ "$1" = "count" ]; then
    curl -X GET http://localhost:9200/_cat/indices/kh_ydkh_gx?v
elif [ "$1" = "log" ]; then
    tail -n 20 /home/es/es_file/$LFILE
elif [ "$1" = "delete" ]; then
    curl -X DELETE http://localhost:9200/kh_ydkh_gx?pretty
elif [ "$1" = "create" ]; then
    curl -X PUT http://localhost:9200/kh_ydkh_gx?pretty -H 'Content-Type: application/json' -d'
{
  "mappings": {
    "doc": {
      "properties": {
        "yddz": {
          "type": "text", 
          "analyzer": "ik_max_word",
          "search_analyzer": "ik_max_word"
        }, 
        "yhmc": {
          "type": "text", 
          "analyzer": "ik_max_word",
          "search_analyzer": "ik_max_word"
        }
      }
    }
  }
}
'
elif [ "$1" = "test" ]; then
    curl -XPOST http://localhost:9200/kh_ydkh_gx/doc/_search?pretty  -H 'Content-Type:application/json' -d'
{
    "query" : { "match" : { "yhmc" : "安桂华" }},
    "_source": ["yhmc", "yddz", "yhbh"]
}
'
else
    if [ -n "$LS_PID" ]; then
        echo "Logstash is already running."
        exit 0
    fi
    nohup /home/es/app/logstash-6.5.1/bin/logstash -f /home/es/es_file/$CFILE > /home/es/es_file/$LFILE 2>&1 &
    echo "Logstash running..."
fi
