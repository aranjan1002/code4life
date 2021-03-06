#ifndef BIGQ_H
#define BIGQ_H
#include <pthread.h>
#include <iostream>
#include <vector>
#include "Pipe.h"
#include "File.h"
#include "Record.h"
#include "MSFirstPhase.h"

using namespace std;

struct Params {
  Pipe* in;
  Pipe* out;
  OrderMaker* sortorder;
  int runlen;
};
  
struct RecComp {
  OrderMaker* so;

  RecComp(OrderMaker* sortorder) {
    so = sortorder;
  }

  // return true if x < y, otherwise false
  bool operator() (Record* x, Record* y) {
    ComparisonEngine ceng;
    // x->Print(sch);
    // cout  << " and " << endl;
    // y->Print(sch);
    // cout << "= " << ceng.Compare(x,y,so) << endl;
    if (ceng.Compare(x, y, so) >= 0) {
      return false;
    }
    else {
      return true;
    }
  }
};


class BigQ {

public:

  BigQ (Pipe &in, Pipe &out, OrderMaker &sortorder, int runlen);
  ~BigQ ();

 private:
  static void *start(void*);
};

#endif
