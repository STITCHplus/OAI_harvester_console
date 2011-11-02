#!/bin/bash

cd /usr/local/log/harvester/ # Working dir must be writeable for user Tomcat

date=`date`
echo $date" : Start " $1 >> /usr/local/log/harvester/harvest_log

/usr/bin/java -Dlog4j.configuration=file:///usr/local/etc/log4j.properties -jar /usr/local/bin/OAI_harvester.jar $@ >> /usr/local/log/harvester/$1_log  &

rm /usr/local/log/harvester/$1_status
echo "Running,," $1 >> /usr/local/log/harvester/$1_status


