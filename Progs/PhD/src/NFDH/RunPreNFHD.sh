for i in $1/*
do
    echo $i `java edu.strippacking.NFDH.RunPreemptiveNFDH $i 0`
done