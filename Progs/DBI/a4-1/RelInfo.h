#ifndef RELINFO_H
#define RELINFO_H

#include <map>
#include <string>

using namespace std;

class RelInfo {
 public:
  // RelInfo();
  // RelInfo(RelInfo&);

  double numTuples;
  map<string, double> attsToNumDistincts;
  int setId;
};

#endif
