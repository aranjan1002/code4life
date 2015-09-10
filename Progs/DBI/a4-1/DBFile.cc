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
    SortInfo* temp = (SortInfo*) startup;
    sortInfo = new SortInfo();
    sortInfo->runLength = temp->runLength;
    sortInfo->myOrder = new OrderMaker(*(temp->myOrder));
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
}

void DBFile::Load (Schema &f_schema, char *loadpath) {
  dbFile->Load(f_schema, loadpath);
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
}

int DBFile :: Close () {
  if (doesMetaExist == false) {
    string mdFilePath = fileName;
    mdFilePath += ".meta";
    writeMetadataFile(mdFilePath);
  }
  
  return dbFile->Close();
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
}

int DBFile::GetNext (Record &fetchme) {
  return dbFile->GetNext(fetchme);
}

int DBFile :: GetNext (Record &fetchme, CNF &cnf, Record &literal) {
  return dbFile->GetNext(fetchme, cnf, literal);
}

DBFile :: ~DBFile() {
  delete dbFile;
  if (sortInfo != NULL) {
    delete sortInfo;
    delete[] types;
    delete[] atts;
  }
}

