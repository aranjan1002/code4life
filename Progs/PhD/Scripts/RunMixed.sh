#!/bin/bash
#cat NiceFiles | while read LINE
#iterate through all the files in directory $1/
for i in $1/*
do
    echo $i `java -cp ../src/ edu.strippacking.JobScheduling 0 $i`
done
