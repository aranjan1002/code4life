#include "TwoWayList.h"
#include "Record.h"
#include "Schema.h"
#include "File.h"
#include "Comparison.h"
#include "ComparisonEngine.h"
#include "DBFile.h"
#include "Defs.h"

#include <stdlib.h>

DBFile::DBFile () {
  readPage = NULL;
  writePage = NULL;
  myFile = NULL;
  readPageNo = 0;
  writePageNo = 0;
}

int DBFile::Create (char *f_path, fType f_type, void *startup) {
  myFile = new File();
  myFile->Open(0, f_path);
  readPage = new Page;
  writePage = new Page;
  return 1;
}

void DBFile::Load (Schema &f_schema, char *loadpath) {
  FILE *tableFile = fopen(loadpath, "r");
  Record temp;
  // Make sure that File is currently empty
  if (readPage != NULL) {
    cerr << "Error: File is not empty" << endl;
    exit(1);
  }
  readPage = new Page;
  writePage = new Page;

  while (temp.SuckNextRecord(&f_schema, tableFile) == 1) {
    Add(temp); 
  }
}

int DBFile::Open (char *f_path) {
  myFile = new File;
  myFile->Open(1, f_path);
  writePageNo = myFile->GetLength() - 2;
  readPageNo = 0;
  // cout << "New file opened of length" << writePageNo + 1 << endl;
  readPage = new Page;
  myFile->GetPage(readPage, 0);
  /*cout << "Records: " << (readPage->GetLeftCount() + 
			  readPage -> GetRightCount()) 
			  << endl; */
  readPage->MoveFirst();
  writePage = new Page;
  myFile->GetPage(writePage, writePageNo);
  return 1;
}

void DBFile::MoveFirst () {
  if (readPageNo == 0) {
    readPage->MoveFirst();
  }
  else if (writePageNo > 0) {
    // get the first page from file
    ReplaceReadPage(0);
  }
}

void DBFile :: ReplaceReadPage(int pageNo) {
  delete readPage;
  readPage = new Page;
  if (pageNo == writePageNo) {
    CopyWritePageToReadPage();
  }
  else {
    myFile->GetPage(readPage, pageNo);
  }
  readPage->MoveFirst();
}

void DBFile :: CopyWritePageToReadPage() {
  char *bits = new (std::nothrow) char[PAGE_SIZE];
  writePage->ToBinary(bits);
  readPage->FromBinary(bits);
}

int DBFile :: Close () {
  /* cout << "Addding page with " << (writePage->GetLeftCount() +
				   writePage->GetRightCount())
				   << " records" << endl; */
  writePage->MoveFirst();
  myFile->AddPage(writePage, writePageNo);
  readPageNo = 0;
  delete readPage;
  delete writePage;
  myFile->Close();
  delete myFile;
}

void DBFile :: Add(Record &rec) {
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

void DBFile :: AddToReadPage(Record &rec) {
  Record recToAdd;
  recToAdd.Copy(&rec);
  readPage->Append(&recToAdd);
}

int DBFile::GetNext (Record &fetchme) {
  int receivedRecord = readPage->GetNext(fetchme);
  if (!receivedRecord) {
    // cout << "reading pageno " << (readPageNo + 1) << endl;
    if (readPageNo < writePageNo) {
      ReplaceReadPage(++readPageNo);
      receivedRecord = readPage->GetNext(fetchme);
    }
  }
  // cout << "Received Record? " << receivedRecord << endl;
  return receivedRecord;
}

int DBFile :: GetNext (Record &fetchme, CNF &cnf, Record &literal) {
  ComparisonEngine comp;
  while (GetNext(fetchme)) {
    if(comp.Compare(&fetchme, &literal, &cnf)) {
      return 1;
    }
  }
  return 0;
}
