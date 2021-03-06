#!/bin/ksh

# Run EVDataSetGen.java
# $1 - total time
# $2 - Output Directory Name

for i in 50 100 200 500 800 1000
do 
    for j in 0 20 40 60 80 100
    do
	for k in 1 2 3 4 5
	do
	    echo $i $j $1 ./$2/EV_${i}_${j}_${1}_${k}.csv
	    java edu.strippacking.DataGen.EVDataSetGen $i \
	    $j $1 ./$2/EV_${i}_${j}_${1}_${k}.csv
	done
    done
done