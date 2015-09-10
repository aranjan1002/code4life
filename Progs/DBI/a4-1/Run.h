#ifndef RUN_H
#define RUN_H

#include <cstdlib>
#include <iostream>
#include "File.h"
#include "Record.h"

class Run {
 public:
  Run(File& f, int startOffset, int stopOffset);
  int GetNext(Record* putItHere);
  ~Run();

 private:
  Page currPage;
  int currOff;
  int startOff;
  int stopOff;
  File& file;
};
  
#endif
