#include "TwoWayList.h"
#include "Record.h"
#include "Schema.h"
#include "File.h"
#include "Comparison.h"
#include "ComparisonEngine.h"
#include "DBFile.h"
#include "Defs.h"
#include "SortedDBFile.h"

#include <stdlib.h>
#include <sstream>
#include <string>

DBFile::DBFile () {
  /*readPage = NULL;
  writePage = NULL;
  myFile = NULL;
  readPageNo = 0;
  writePageNo = 0;*/
  dbFile = NULL;
  doesMetaExist = false;
  atts = NULL;
  types = NULL;
  sortInfo = NULL;
}

int DBFile::Create (char *f_path, fType f_type, void *startup) {
  fileName = f_path;
  doesMetaExist = false;
  fileType = f_type;
  if (fileType == sorted) {
    sortInfo = (SortInfo*) startup;
    dbFile = new SortedDBFile(*sortInfo);
    OrderMaker *om = sortInfo->myOrder;
    numAtts = om->GetNumAtts();
    string a = om->GetAtts();
    string t = om->GetTypes(); 
    readAtts(numAtts, a);
    readTypes(numAtts, t);
  }
  else {
    dbFile = new HeapDBFile();
  }
  return dbFile->Create(f_path);
  /*myFile = new File();
  myFile->Open(0, f_path);
  readPage = new Page;
  writePage = new Page;
  return 1;*/
}

void DBFile::Load (Schema &f_schema, char *loadpath) {
  dbFile->Load(f_schema, loadpath);
  /*FILE *tableFile = fopen(loadpath, "r");
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
    }*/
}

int DBFile :: Open (char *f_path) {
  doesMetaExist = true;
  fileName = f_path;
  string mdFilePath = f_path;
  mdFilePath += ".meta";
  readMetadataFile(mdFilePath);
  if (fileType == sorted) {
    dbFile = new SortedDBFile(*sortInfo);
  } 
  else {
    dbFile = new HeapDBFile();
    /*myFile = new File;
    myFile->Open(1, f_path);
    writePageNo = myFile->GetLength() - 2;
    readPageNo = 0;
    // cout << "New file opened of length" << writePageNo + 1 << endl;
    readPage = new Page;
    myFile->GetPage(readPage, 0);
    /*cout << "Records: " << (readPage->GetLeftCount() + 
      readPage -> GetRightCount()) 
      << endl; */
    /*readPage->MoveFirst();
    writePage = new Page;
    myFile->GetPage(writePage, writePageNo);*/
  }
  return dbFile->Open(f_path);
}

void DBFile :: readMetadataFile(string& filePath) {
  ifstream mdFile(filePath.c_str(), ifstream::in);
  if (mdFile.is_open()) {
    readFileType(mdFile);
    if (fileType == sorted) {
      readSortOrder(mdFile);
    }
    mdFile.close();
  }
  else {
    cout << "Unable to read metadatafile" << endl;
    exit(1);
  }
}

void DBFile :: readFileType(ifstream& mdFile) {
  string line;
  getline (mdFile, line);
  if (line == "heap") {
    fileType = heap;
  }
  else if (line == "sorted") {
    fileType = sorted;
  }
  else if (line == "tree") {
    fileType = tree;
  }
  else {
    cerr << "Invalid line metadata file: " << line << endl;
    exit(1);
  }
}

/*void DBFile :: readRunLength(ifstream& mdFile) {
  string line;
  getline (mdFile, line);
  sortInfo->runLength = atoi(line.c_str());
  }*/

/*
  Format of sort order:
  n - integer reprsenting count of attributes
  a1 b1 .... n1 - n integers seperated by spaces representing attribute number
  a b .... n - n Types seperated by spaces
 */
void DBFile :: readSortOrder(ifstream& mdFile) {
  string line;
  getline(mdFile, line);
  numAtts = atoi(line.c_str());
  getline(mdFile, line);
  readAtts(numAtts, line);
  getline(mdFile, line);
  readTypes(numAtts, line);
  getline(mdFile, line);
  delete sortInfo;
  sortInfo = new SortInfo();
  sortInfo->myOrder = new OrderMaker();
  sortInfo->myOrder->Init(atts, types, numAtts);
  readRunLen(line);
}

void DBFile :: readTypes(int cnt, string line) {
  delete types;
  types = new Type[cnt];
  stringstream iss(line);
  for (int i = 0; i < cnt; i++) {
    string type;
    iss >> type;
    if (type == "Int") {
      types[i] = Int;
    } else if (type == "Double") {
      types[i] = Double;
    } else if (type == "String") {
      types[i] = String;
    } else {
      cerr << "Invalid entry in metadata: " << line << endl;
      exit(1);
    }
  }
}

void DBFile :: readAtts(int cnt, string line) {
  delete atts;
  stringstream iss(line);
  atts = new int[cnt];
  for (int i = 0; i < cnt; i++) {
    string att;
    iss >> att;
    atts[i] = atoi(att.c_str());
  }
}

void DBFile :: readRunLen(string line) {
  sortInfo->runLength = atoi(line.c_str());
}
 
void DBFile::MoveFirst () {
  dbFile->MoveFirst();
  /*if (readPageNo == 0) {
    readPage->MoveFirst();
  }
  else if (writePageNo > 0) {
    // get the first page from file
    ReplaceReadPage(0);
    }*/
}

/*void DBFile :: ReplaceReadPage(int pageNo) {
  delete readPage;
  readPage = new Page;
  if (pageNo == writePageNo) {
    CopyWritePageToReadPage();
  }
  else {
    myFile->GetPage(readPage, pageNo);
  }
  readPage->MoveFirst();
  readPageNo = pageNo;
  }*/

/*void DBFile :: CopyWritePageToReadPage() {
  char *bits = new (std::nothrow) char[PAGE_SIZE];
  writePage->ToBinary(bits);
  readPage->FromBinary(bits);
  }*/

int DBFile :: Close () {
  /* cout << "Addding page with " << (writePage->GetLeftCount() +
				   writePage->GetRightCount())
				   << " records" << endl; */
  if (doesMetaExist == false) {
    string mdFilePath = fileName;
    mdFilePath += ".meta";
    writeMetadataFile(mdFilePath);
  }
  
  return dbFile->Close();
  /*writePage->MoveFirst();
  myFile->AddPage(writePage, writePageNo);
  readPageNo = 0;
  delete readPage;
  delete writePage;
  myFile->Close();
  delete myFile;*/
}

void DBFile :: writeMetadataFile(string& mdFilePath) {
  ofstream mdFile(mdFilePath.c_str(), ofstream::out);
  if (mdFile.is_open()) {
    mdFile << ToStr(fileType) << endl;
    if (fileType == sorted) {
      mdFile << numAtts << endl;
      for (int i = 0; i < numAtts - 1; i++) {
	mdFile << atts[i] << " ";
      }
      mdFile << atts[numAtts - 1] << endl;
      for (int i = 0; i < numAtts - 1; i++) {
	mdFile << ToString(types[i]) << " ";
      }
      mdFile << ToString(types[numAtts - 1]) << endl;
      mdFile << sortInfo->runLength << endl;
    }
    mdFile.close();
  } 
  else {
    cerr << "Unable to create new meta file" << endl;
    exit(1);
  }
}

string DBFile :: ToStr(fType& type) {
  string result;
  if (type == sorted) {
    result = "sorted";
  }
  else if (type = heap) {
    result = "heap";
  }
  else {
    cerr << "Unrecognized type";
    exit(1);
  }
  return result;
}

void DBFile :: Add(Record &rec) {
  dbFile->Add(rec);
  /*if (writePageNo == readPageNo) {
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
    }*/
}

/*void DBFile :: AddToReadPage(Record &rec) {
  Record recToAdd;
  recToAdd.Copy(&rec);
  readPage->Append(&recToAdd);
  }*/

int DBFile::GetNext (Record &fetchme) {
  /*int receivedRecord = readPage->GetNext(fetchme);
  if (!receivedRecord) {
    // cout << "reading pageno " << (readPageNo + 1) << endl;
    if (readPageNo < writePageNo) {
      ReplaceReadPage(++readPageNo);
      receivedRecord = readPage->GetNext(fetchme);
    }
  }
  // cout << "Received Record? " << receivedRecord << endl;
  */
  return dbFile->GetNext(fetchme);
}

int DBFile :: GetNext (Record &fetchme, CNF &cnf, Record &literal) {
  /*ComparisonEngine comp;
  while (GetNext(fetchme)) {
    if(comp.Compare(&fetchme, &literal, &cnf)) {
      return 1;
    }
    }*/
  return dbFile->GetNext(fetchme, cnf, literal);
  
}

DBFile :: ~DBFile() {
  delete dbFile;
  if (sortInfo != NULL) {
    // delete sortInfo;
    delete types;
    delete atts;
  }
}

int DBFile :: GetNext (Record &fetchme, CNF &cnf, Record &literal,
		       Schema* sc) {
  return ((SortedDBFile*) dbFile)->GetNext(fetchme, cnf, literal, sc);
}
