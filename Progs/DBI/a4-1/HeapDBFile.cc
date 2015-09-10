#include "TwoWayList.h"
#include "Record.h"
#include "Schema.h"
#include "File.h"
#include "Comparison.h"
#include "ComparisonEngine.h"
#include "DBFile.h"
#include "Defs.h"
#include "HeapDBFile.h"

#include <stdlib.h>
#include <sstream>

HeapDBFile :: HeapDBFile() {
  writePage = NULL;
  writePageNo = 0;
}

int HeapDBFile :: Create(char* fPath) {
  writePage = new Page;
  writePageNo = 0;
  return GenericDBFile :: Create(fPath);
}

int HeapDBFile :: Open(char* fPath) {
  writePage = new Page;
  writePageNo = 0;
  int currLen = myFile->GetLength();
  if (currLen > 0) {
    writePageNo = currLen - 2;
    myFile->GetPage(writePage, writePageNo);
  }
  return GenericDBFile :: Open(fPath);
}

int HeapDBFile :: GetNext(Record& fetchme) {
  if (GenericDBFile :: GetNext(fetchme) == 0) {
    if (readPageNo == writePageNo - 1) {
      CopyWritePageToReadPage();
      readPageNo++;
      return GenericDBFile :: GetNext(fetchme);
    }
    else if (readPageNo < writePageNo) {
      ReplaceReadPage(++readPageNo);
      return GenericDBFile :: GetNext(fetchme);
    }
    else {
      return 0;
    }
  }
  else {
    return 1;
  }
}

int HeapDBFile :: GetNext (Record &fetchme, CNF &cnf, Record &literal) {
  ComparisonEngine comp;
  while (GetNext(fetchme)) {
    if(comp.Compare(&fetchme, &literal, &cnf)) {
      return 1;
    }
  }
  return 0;
}


void HeapDBFile :: Add(Record &rec) {
  if (writePageNo == readPageNo) {
    AddToReadPage(rec);
  }
  int isRecordAdded = writePage->Append(&rec);
  // cout << "new record added" << endl;
  // In case page is full
  if (!isRecordAdded) {
    writePage->MoveFirst();
    myFile->AddPage(writePage, writePageNo++);
    delete writePage;
    writePage = new Page;
    // cout << "new page created, pageno " << writePageNo << endl;
    if (!(writePage->Append(&rec))) {
      cerr << "unable to insert record in a newly created page";
      exit(1);
    }
  }
}

void HeapDBFile :: Load(Schema& fSchema, char *loadpath) {
  FILE *tableFile = fopen(loadpath, "r");
  Record temp;
  Create (fileName);
  // Make sure that File is currently empty
  while (temp.SuckNextRecord(&fSchema, tableFile) == 1) {
    Add(temp);
  }
}

void HeapDBFile :: MoveFirst() {
  if (writePageNo == 0) {
    CopyWritePageToReadPage();
  }
  GenericDBFile :: MoveFirst();
}

void HeapDBFile :: CopyWritePageToReadPage() {
  char *bits = new (std::nothrow) char[PAGE_SIZE];
  writePage->ToBinary(bits);
  readPage->FromBinary(bits);
  readPage->MoveFirst();
}

void HeapDBFile :: AddToReadPage(Record &rec) {
  Record recToAdd;
  recToAdd.Copy(&rec);
  readPage->Append(&recToAdd);
}

int HeapDBFile :: Close() {
  writePage->MoveFirst();
  if (writePage->IsEmpty() == false) {
    myFile->AddPage(writePage, writePageNo);
  }
  writePageNo = 0;
  //delete writePage;
  return GenericDBFile :: Close();
}

HeapDBFile :: ~HeapDBFile() {
  //delete writePage;
}
