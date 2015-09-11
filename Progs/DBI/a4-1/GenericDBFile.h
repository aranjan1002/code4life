#ifndef GENERICDBFILE_H
#define GENERICDBFILE_H

#include "TwoWayList.h"
#include "Record.h"
#include "Schema.h"
#include "File.h"

#include <iostream>
#include <fstream>
#include <cstring>

using namespace std;

class GenericDBFile {
 public:
  GenericDBFile();
  virtual int Open(char* fPath);
  virtual int Close();
  virtual int Create(char* fPath);
  void ReplaceReadPage(int pageNo);
  virtual void MoveFirst();
  virtual int GetNext(Record&);
  virtual int GetNext (Record &fetchme, CNF &cnf, Record &literal) = 0;
  int GetPageCnt();

  virtual void Load(Schema&, char*) = 0;
  virtual void Add(Record&) = 0;
  
  virtual ~GenericDBFile();

 protected:
  File *myFile;
  Page *readPage;
  int readPageNo;
  char *fileName;

 private:
  void setFileName(char* fPath);
};

#endif