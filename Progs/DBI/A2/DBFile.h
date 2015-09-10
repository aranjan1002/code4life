#ifndef DBFILE_H
#define DBFILE_H

#include "TwoWayList.h"
#include "Record.h"
#include "Schema.h"
#include "File.h"
#include "Comparison.h"
#include "ComparisonEngine.h"

#include <iostream>

typedef enum {heap, sorted, tree} fType;

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
	
 private:
  void ReplaceReadPage(int);
  void CopyWritePageToReadPage();
  void AddToReadPage(Record&);

  Page* writePage;
  Page* readPage;
  File* myFile;
  unsigned int writePageNo;
  unsigned int readPageNo;
};
#endif
