#!/bin/bash
#cat NiceFiles | while read LINE
#iterate through all the files in directory $1/
for i in $1/*
do
    echo $i $2 `java -cp ../src edu.strippacking.JobScheduling 0 $i 25`
    echo $i $2 `java -cp ../src edu.strippacking.JobScheduling 0 $i 50`
    echo $i $2 `java -cp ../src edu.strippacking.JobScheduling 0 $i 75`
done
#echo