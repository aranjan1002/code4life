#Runs ILP program
#Needs one parameter: the location of input files

java -d64 -Djava.library.path=/opt/ibm/ILOG/CPLEX_Studio1251/cplex/bin/x86-64_sles10_4.1 -classpath /opt/ibm/ILOG/CPLEX_Studio1251/cplex/lib/cplex.jar: StripPacking2 $1
