
CC = g++ -O2 -Wno-deprecated

tag = -i

ifdef linux
tag = -n
endif

test.out: Record.o Comparison.o ComparisonEngine.o Schema.o File.o BigQ.o Run.o MSSecPhase.o MSFirstPhase.o GenericDBFile.o SortedDBFile.o HeapDBFile.o DBFile.o Pipe.o y.tab.o lex.yy.o test.o Defs.o 
	$(CC) -o test.out Record.o Comparison.o ComparisonEngine.o Schema.o File.o BigQ.o Run.o MSFirstPhase.o MSSecPhase.o GenericDBFile.o SortedDBFile.o HeapDBFile.o DBFile.o Defs.o Pipe.o y.tab.o lex.yy.o test.o -lfl -lpthread

a2test.out:Record.o Comparison.o ComparisonEngine.o Schema.o File.o BigQ.o DBFile.o Pipe.o y.tab.o lex.yy.o a2-test.o
	$(CC) -o a2test.out Record.o Comparison.o ComparisonEngine.o Schema.o File.o BigQ.o DBFile.o Pipe.o y.tab.o lex.yy.o a2-test.o -lfl -lpthread
	
a1test.out: Record.o Comparison.o ComparisonEngine.o Schema.o File.o DBFile.o Pipe.o y.tab.o lex.yy.o a1-test.o
	$(CC) -o a1test.out Record.o Comparison.o ComparisonEngine.o Schema.o File.o DBFile.o Pipe.o y.tab.o lex.yy.o a1-test.o -lfl
	
test.o: test.cc
	$(CC) -g -c test.cc

a2-test.o: a2-test.cc
	$(CC) -g -c a2-test.cc

a1-test.o: a1-test.cc
	$(CC) -g -c a1-test.cc

SortedDBFile.o: SortedDBFile.cc SortedDBFile.h GenericDBFile.h GenericDBFile.cc test.h
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

Run.i:
	$(CC) -g -c Run.cc

Comparison.o: Comparison.cc Defs.h
	$(CC) -g -c Comparison.cc
	
ComparisonEngine.o: ComparisonEngine.cc
	$(CC) -g -c ComparisonEngine.cc
	
Pipe.o: Pipe.cc
	$(CC) -g -c Pipe.cc

BigQ.o: BigQ.cc
	$(CC) -g -c BigQ.cc

DBFile.o: DBFile.cc DBFile.h Defs.h
	$(CC) -g -c DBFile.cc

File.o: File.cc
	$(CC) -g -c File.cc

Record.o: Record.cc
	$(CC) -g -c Record.cc

Schema.o: Schema.cc
	$(CC) -g -c Schema.cc
	
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
	rm -f y.tab.c
	rm -f lex.yy.c
	rm -f y.tab.h
