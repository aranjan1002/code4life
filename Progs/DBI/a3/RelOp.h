#ifndef REL_OP_H
#define REL_OP_H

#include "Pipe.h"
#include "DBFile.h"
#include "Record.h"
#include "Function.h"

#include <pthread.h>
#include <vector>

class RelationalOp {
 public:
  // blocks the caller until the particular relational operator 
  // has run to completion
  virtual void WaitUntilDone ();

  // tell us how much internal memory the operation can use
  void Use_n_Pages (int n);
  RelationalOp();
 protected:
  pthread_t thread;
  Pipe *in, *out;
  CNF* cnf;
  Record* lit;
  int runLen;
};

class SelectFile : public RelationalOp { 
 public:
  void Run (DBFile &inFile, Pipe &outPipe, CNF &selOp, Record &literal);
  //  void WaitUntilDone ();
  // void Use_n_Pages (int n);
 private:
  DBFile* myFile;
  static void* execute(void*);
};

class SelectPipe : public RelationalOp {
 public:
  void Run (Pipe &inPipe, Pipe &outPipe, CNF &selOp, Record &literal);
 private:
  static void* execute(void*);
  // void WaitUntilDone () { }
  //  void Use_n_Pages (int n) { }
};
class Project : public RelationalOp { 
 public:
  void Run (Pipe &inPipe, 
	    Pipe &outPipe, 
	    int *keepMe, 
	    int numAttsInput, 
	    int numAttsOutput);
 private:
  static void* execute(void*);
  
  int* atts;
  int numAttsIn;
  int numAttsOut;
  // void WaitUntilDone () { }
  // void Use_n_Pages (int n) { }
};
class Join : public RelationalOp { 
 public:
  void Run (Pipe &inPipeL, Pipe &inPipeR, Pipe &outPipe, CNF &selOp, Record &literal, Schema* s1 = NULL, Schema* s2 = NULL);
 private:
  static void* execute(void*);
  void merge (Pipe&, Pipe&);
  void setAttsToKeepAndNumAtts(int* fillItHere,
			       int &numAttsToKeep,
			       Record& rec1,
			       Record& rec2,
			       int &numAtts1,
			       int &numAtts2);
  void joinRec(vector<Record*>&, vector<Record*>&);
  Pipe* in1;
  OrderMaker leftOrder, rightOrder;
  Schema *sch1, *sch2;
};
class DuplicateRemoval : public RelationalOp {
 public:
  void Run (Pipe &inPipe, Pipe &outPipe, Schema &mySchema);
 private:
  static void* execute(void*);
  Schema *mySch;
};
class Sum : public RelationalOp {
 public:
  void Run (Pipe &inPipe, Pipe &outPipe, Function &computeMe);
  // void WaitUntilDone () { }
  // void Use_n_Pages (int n) { }
 private:
  static void* execute(void*);
  Function* func;
};
class GroupBy : public RelationalOp {
 public:
  void Run (Pipe &inPipe, Pipe &outPipe, OrderMaker &groupAtts, Function &computeMe);
 private:
  static void* execute(void*);
  
  OrderMaker *grpAtts;
  Function *myFunc;
  // void WaitUntilDone () { }
  // void Use_n_Pages (int n) { }
};
class WriteOut : public RelationalOp {
 public:
  void Run (Pipe &inPipe, FILE *outFile, Schema &mySchema);
 private: 
  static void* execute(void*);
  FILE *myFile;
  Schema *mySch;
};
#endif
