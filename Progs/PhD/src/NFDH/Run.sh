#!/bin/bash
#cat NiceFiles | while read LINE
#iterate through all the files in directory $1/
for i in $1/*
do
    echo $i $2 `./RunAll.sh $i`
done
echo