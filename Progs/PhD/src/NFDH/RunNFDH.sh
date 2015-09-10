for i in $1/*
do
    echo $i `java edu.strippacking.NFDH.RunNFDH $i`
done
