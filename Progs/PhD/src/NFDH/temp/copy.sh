#!/bin/bash

for i in `ls DataSet`
do
    cp ./DataSet/$i/*.csv ./
done