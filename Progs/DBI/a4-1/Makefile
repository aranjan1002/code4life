CC = g++ -O2 -Wno-deprecated 

tag = -i

ifdef linux
tag = -n
endif

a4-1.out: Record.o Comparison.o ComparisonEngine.o Schema.o File.o DBFile.o Statistics.o Pipe.o BigQ.o RelOp.o Function.o Run.o MSSecPhase.o MSFirstPhase.o GenericDBFile.o SortedDBFile.o HeapDBFile.o Defs.o y.tab.o lex.yy.o test.o
	$(CC) -o a4-1.out Record.o Comparison.o ComparisonEngine.o Schema.o File.o DBFile.o Statistics.o Pipe.o BigQ.o RelOp.o Function.o Run.o MSSecPhase.o MSFirstPhase.o GenericDBFile.o SortedDBFile.o HeapDBFile.o Defs.o y.tab.o lex.yy.o test.o -lfl -lpthread

test.o: test.cc
	$(CC) -g -c test.cc

Statistics.o: Statistics.cc
	$(CC) -g -c Statistics.cc

Comparison.o: Comparison.cc
	$(CC) -g -c Comparison.cc

RelOp.o: RelOp.cc RelOp.h
	$(CC) -g -c RelOp.cc

SortedDBFile.o: SortedDBFile.cc SortedDBFile.h GenericDBFile.h GenericDBFile.cc 
	$(CC) -g -c SortedDBFile.cc

HeapDBFile.o: HeapDBFile.cc SortedDBFile.h GenericDBFile.h GenericDBFile.cc
	$(CC) -g -c HeapDBFile.cc

GenericDBFile.o: GenericDBFile.h GenericDBFile.cc
	$(CC) -g -c GenericDBFile.cc

MSFirstPhase.o: MSFirstPhase.cc MSFirstPhase.h
	$(CC) -g -c MSFirstPhase.cc

MSSecPhase.o: MSSecPhase.cc MSSecPhase.h
	$(CC) -g -c MSSecPhase.cc

Defs.o: Defs.cc Defs.h
	$(CC) -g -c Defs.cc

Run.o:
	$(CC) -g -c Run.cc

	
ComparisonEngine.o: ComparisonEngine.cc
	$(CC) -g -c ComparisonEngine.cc
	
DBFile.o: DBFile.cc
	$(CC) -g -c DBFile.cc

Pipe.o: Pipe.cc
	$(CC) -g -c Pipe.cc

BigQ.o: BigQ.cc
	$(CC) -g -c BigQ.cc

Function.o: Function.cc
	$(CC) -g -c Function.cc

File.o: File.cc
	$(CC) -g -c File.cc

Record.o: Record.cc
	$(CC) -g -c Record.cc

Schema.o: Schema.cc
	$(CC) -g -c Schema.cc
	
y.tab.o: Parser.y
	yacc -d Parser.y
	#sed $(tag) y.tab.c -e "s/  __attribute__ ((__unused__))$$/# ifndef __cplusplus\n  __attribute__ ((__unused__));\n# endif/" 
	g++ -c y.tab.c
		
y.tab.o: Parser.y
	yacc -d Parser.y
	sed $(tag) y.tab.c -e "s/  __attribute__ ((__unused__))$$/# ifndef __cplusplus\n  __attribute__ ((__unused__));\n# endif/" 
	g++ -c y.tab.c

lex.yy.o: Lexer.l
	lex  Lexer.l
	gcc  -c lex.yy.c

clean: 
	rm -f *.o
	rm -f *.out
	rm -f y.tab.*
	rm -f yyfunc.tab.*
	rm -f lex.yy.*
	rm -f lex.yyfunc*