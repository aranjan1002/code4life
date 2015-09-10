#ifndef DBFILE_H
#define DBFILE_H

#include "TwoWayList.h"
#include "Record.h"
#include "Schema.h"
#include "File.h"
#include "Comparison.h"
#include "ComparisonEngine.h"
#include "GenericDBFile.h"
#include "Defs.h"

#include <iostream>
#include <fstream>

typedef enum {heap, sorted, tree} fType;


struct SortInfo {
  OrderMaker *myOrder;
  int runLength;
};

// stub DBFile header..replace it with your own DBFile.h 

class DBFile {

 public:
  DBFile (); 
  
  int Create (char *fpath, fType file_type, void *startup);
  int Open (char *fpath);
  int Close ();
  
  void Load (Schema &myschema, char *loadpath);

  void MoveFirst ();
  void Add (Record &addme);
  int GetNext (Record &fetchme);
  int GetNext (Record &fetchme, CNF &cnf, Record &literal);

  ~DBFile();
	
 private:
  void readMetadataFile(string&);
  void readSortOrder(ifstream&);
  void readFileType(ifstream& mdFile);
  void readTypes(int, string);
  void readAtts(int, string);
  void readRunLen(string);
  void writeMetadataFile(string&);
  string ToStr(fType& type);

  string fileName;
  fType fileType;
  GenericDBFile *dbFile;
  SortInfo* sortInfo;
  bool doesMetaExist;
  Type* types;
  int* atts;
  int numAtts;
};

#endif
