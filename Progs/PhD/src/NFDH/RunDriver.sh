cat DirPaths | while read LINE
do
    echo $LINE
    ./Run.sh $LINE >> Readings
done
