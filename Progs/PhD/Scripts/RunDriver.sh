cat $1 | while read LINE
do
    echo $LINE
    ./Run.sh $LINE 
done
