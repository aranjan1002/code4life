#include "BigQ.h"
#include "MSSecPhase.h"
#include <algorithm>
#include <string>
#include <sstream>

// string runfile = "runFile.temp";

BigQ :: BigQ (Pipe &in, 
	      Pipe &out, 
	      OrderMaker &sortorder, 
	      int runlen) { //sch = sc;
  // read data from in pipe sort them into runlen pages
  Params *params = new Params();
  params->in = &in;
  params->out = &out;
  params->sortorder = &sortorder;
  params->runlen = runlen;
  params->runfile = getRunFileName(in);
  pthread_t thread3;
  pthread_create(&thread3, NULL, &BigQ::start, (void*) params);
  // runfile = runfile + &runlen;
  // pthread_join(thread3, NULL);
}

string BigQ :: getRunFileName(Pipe &in) {
  stringstream ss;
  ss << "runfile";
  ss << (&in);
  ss << ".temp";
  string runfile = ss.str().c_str();
  return runfile;
}

void *BigQ :: start(void* par) {
  Params *params = (Params*) par;
  Pipe &in = *(params->in);
  Pipe &out = *(params->out);
  OrderMaker &sortorder = *(params->sortorder);
  int runlen = params->runlen;
  string runfile = params->runfile;
  RecComp comp(&sortorder);
  vector<int> runOffsets;
  File file;
  file.Open(0, (char *) runfile.c_str());
  // cout << runfile << endl;

  MSFirstPhase ms1(in, comp, runlen, file);
  ms1.createRunsAndGetOffsets(runOffsets);

  MSSecPhase ms2(file, runOffsets, comp);

  Record *r = new Record();
  while(ms2.GetNext(*r)) {
    out.Insert(r);
    r = new Record();
  }
  file.Close();
  delete params;
  out.ShutDown ();
  remove(runfile.c_str());
}

BigQ :: ~BigQ() {
}