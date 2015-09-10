#ifndef MSSEC_H
#define MSSEC_H

#include "BigQ.h"
#include "Run.h"
#include <queue>
#include <iostream>

using namespace std;

struct RecordInfo {
RecordInfo(RecComp* c) : comp(c) {
  rec = new Record();
}

  ~RecordInfo() {
    //cout << "Destructor called";
    delete rec;
  }

  Run* run;
  Record* rec;
  RecComp *comp;
};

class RecInfoComp {
  bool reverse;
 public:
  RecInfoComp(const bool& revParam=false) {
    reverse = revParam;
  }

  bool operator() (const RecordInfo* x, const RecordInfo* y) {
    RecComp *c = x->comp;
    /*x->rec->Print(c->sch);
    cout << "and" << endl;
    y->rec->Print(c->sch);
    cout << "returns " << (*c)(x->rec, y->rec) << endl;*/
    if (!reverse) {
      return ((*c)(y->rec, x->rec));
    }
    else {
      return ((*c)(x->rec, y->rec));
    }
  }
};

class MSSecPhase {
 public :
  MSSecPhase(File& f, vector<int>& run_off, RecComp& c);
  int GetNext(Record& putItHere);
  ~MSSecPhase();

 private:
  void initRuns(vector<int>& runOffsets);
  
  File& file;
  vector<RecordInfo*> recVec;
  priority_queue<RecordInfo*, vector<RecordInfo*>, RecInfoComp> pq;
  vector<Run*> runs; //stores the vector of runs
  //of different runs
  RecComp& comp;
  RecInfoComp* recInfoComp;
};

#endif
