#ifndef MSFIRST_H
#define MSFIRST_H

#include "BigQ.h"

struct RecComp;

using namespace std;

class MSFirstPhase {
 public:
  MSFirstPhase(Pipe &in, RecComp &comp, int runLen, File&);
  void createRunsAndGetOffsets(vector<int>& runOffsets);

 private:
  int sortInsertInFileClearAndGetOffset(vector<Record*>& recVec,
					File &file);
  int insertInFileAndGetOffset(vector<Record*> &recVec,
			       File &file);

  Pipe& in;
  RecComp& comp;
  int runLen;
  File& file;
};

#endif
