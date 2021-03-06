#include "RelOp.h"
#include <sstream>
#include <cstdlib>
#include "BigQ.h"

int pipesize = 100;

RelationalOp :: RelationalOp() {
  runLen = 5; // default value;
}

void RelationalOp :: Use_n_Pages(int n) {
  runLen = n;
}

void RelationalOp :: WaitUntilDone() {
  pthread_join(thread, NULL);
}

void SelectFile::Run (DBFile &inFile, Pipe &outPipe, CNF &selOp, Record &literal) {
  out = &outPipe;
  myFile = &inFile;
  cnf = &selOp;
  lit = &literal;
  pthread_create(&thread, NULL, &SelectFile::execute, (void*) this);
}

void* SelectFile :: execute(void* x) {
  SelectFile* thisObj = (SelectFile*) x;
  Record *fetchMe = new Record();
  int i = 0;
  while (thisObj->myFile->GetNext(*fetchMe, 
				  *(thisObj->cnf), 
				  *(thisObj->lit)) != 0) {
    thisObj->out->Insert(fetchMe);
    fetchMe = new Record();
    i++;
    // cout << i++ << endl;
  }
  cout << "SelectFile: " << i << " rec read" << endl;
  thisObj->out->ShutDown();
}

/*void SelectFile::WaitUntilDone () {
  pthread_join (thread, NULL);
  }*/

/*void SelectFile::Use_n_Pages (int runlen) {
  
}
*/

void SelectPipe :: Run (Pipe &inPipe, 
			Pipe &outPipe, 
			CNF &selOp, 
			Record &literal) {
  in = &inPipe;
  out = &outPipe;
  cnf = &selOp;
  lit = &literal;
  pthread_create(&thread, NULL, &SelectPipe::execute, (void*) this);
}

void* SelectPipe :: execute(void* x) {
  SelectPipe* thisObj = (SelectPipe*) x;
  Record *fetchMe = new Record();
  ComparisonEngine comp;
  while (thisObj->in->Remove(fetchMe) != 0) {
    if (comp.Compare(fetchMe, thisObj->lit, thisObj->cnf)) {
      thisObj->out->Insert(fetchMe);
      fetchMe = new Record();
    }
  }
  thisObj->out->ShutDown();
}

void Project :: Run(Pipe &inPipe, Pipe &outPipe,
		    int *keepMe, int numAttsInput,
		    int numAttsOutput) {
  in = &inPipe;
  out = &outPipe;
  atts = keepMe;
  numAttsIn = numAttsInput;
  numAttsOut = numAttsOutput;
  pthread_create(&thread, NULL, &Project :: execute, (void*) this);
}

void* Project :: execute(void* x) {
  Project* thisObj = (Project*) x;
  Record *fetchMe = new Record();
  ComparisonEngine comp;
  int i = 0;
  while (thisObj->in->Remove(fetchMe) != 0) {
    fetchMe->Project(thisObj->atts, thisObj->numAttsOut, thisObj->numAttsIn);
    // cout << fetchMe->GetNumAtts();
    //cout << " atts" << endl;
    thisObj->out->Insert(fetchMe);
    fetchMe = new Record();    // cout << "in project " << i++ << endl;
  }
  thisObj->out->ShutDown();
}

void Sum :: Run (Pipe &inPipe, Pipe &outPipe, Function &computeMe)  {
  in = &inPipe;
  out = &outPipe;
  func = &computeMe;
  pthread_create(&thread, NULL, &Sum :: execute, (void*) this);
}

void* Sum :: execute(void* x) {
  Sum* thisObj = (Sum*) x;
  Record *fetchMe = new Record();
  int intResult = 0, intSum = 0;
  double dblResult = 0.0;
  double dblSum = 0.0;
  Type t =  thisObj->func->GetReturnType();
  int i = 0;
  cout << "Executing sum" << endl;
  if (t == Int) {
    while (thisObj->in->Remove(fetchMe) != 0) {
      thisObj->func->Apply(*fetchMe, intResult, dblResult);
      intSum += intResult;
      i++;
      // cout << dblResult << endl;
    }
  } else {
    while (thisObj->in->Remove(fetchMe) != 0) {
      thisObj->func->Apply(*fetchMe, intResult, dblResult);
      dblSum += dblResult;
      i++;
      cout << dblSum << endl;
    }
  }
  cout << i << " records read" << endl;
  Attribute DA = {"double", Double};
  Attribute IA = {"int", Int};
  stringstream ss;
  Record resultRec;
  Schema *out_sch;
  if (t == Int) {
    out_sch = new Schema("out_sch", 1, &IA);
    ss << intResult;
  } else if (t == Double) {
    out_sch = new Schema("out_sch", 1, &DA);
    cout << "Result" << dblSum << endl;
    cout << i << endl;
    ss << dblSum;
    ss << "|";
  } else {
    cout << "type of result not valid" << endl;
    exit(1);
  }
  
  resultRec.ComposeRecord(out_sch, ss.str().c_str());
  thisObj->out->Insert(&resultRec);
  thisObj->out->ShutDown();
}

void WriteOut :: Run (Pipe &inPipe, FILE *outFile, Schema &mySchema) {
  in = &inPipe;
  myFile = outFile;
  mySch = &mySchema;
  pthread_create(&thread, NULL, &WriteOut :: execute, (void*) this);
}

void* WriteOut :: execute(void* x) {
  WriteOut* thisObj = (WriteOut*) x;
  stringstream ss;
  Record *fetchMe = new Record();
  int cnt = 0;
  while (thisObj->in->Remove(fetchMe) != 0) {
    fetchMe->Print(thisObj->mySch, ss);
    fputs(ss.str().c_str(), thisObj->myFile);
    cnt++;
  }
  cout << "Total records count: " << cnt << endl;
}

void DuplicateRemoval :: Run (Pipe &inPipe, Pipe &outPipe, Schema &mySchema) {
  mySch = &mySchema;
  in = &inPipe;
  out = &outPipe;
  //cout << "rem dep" << endl;
  pthread_create(&thread, NULL, &DuplicateRemoval :: execute, (void*) this);
}

void* DuplicateRemoval :: execute(void* x) {
  DuplicateRemoval* thisObj = (DuplicateRemoval*) x;
  stringstream ss;
  // cout << "removing dup" << endl;
  Record *fetchMe = new Record();
  OrderMaker om(thisObj->mySch);
  Pipe p(pipesize);
  BigQ bigQ(*thisObj->in, p, om, thisObj->runLen);
  Record *prevRecord = new Record();
  while (p.Remove(fetchMe) != 0) {
    if (fetchMe->IsEqual(prevRecord) == 0) {
      prevRecord->Copy(fetchMe);
      thisObj->out->Insert(fetchMe);
    }
  }
  thisObj->out->ShutDown();
}

void Join :: Run (Pipe &inPipeL, 
		  Pipe &inPipeR, 
		  Pipe &outPipe, 
		  CNF &selOp, 
		  Record &literal, Schema* sc1, Schema* sc2) {
  in = &inPipeL;
  in1 = &inPipeR;
  out = &outPipe;
  cnf = &selOp;
  lit = &literal;
  sch1 = sc1;
  sch2 = sc2;
  pthread_create(&thread, NULL, &Join :: execute, (void*) this);
}

void* Join :: execute(void* x) {
  Join* thisObj = (Join*) x;
  if (thisObj->cnf->GetSortOrders(thisObj->leftOrder, 
				  thisObj->rightOrder) == 1) {
    // an equi-join
    Pipe leftOut(pipesize);
    Pipe rightOut(pipesize);
    BigQ bigQL(*thisObj->in, leftOut, thisObj->leftOrder, thisObj->runLen);
    BigQ bigQR(*thisObj->in1, rightOut, thisObj->rightOrder, thisObj->runLen);
    thisObj->merge(leftOut, rightOut);
  }
  else {
    // not an equi join
  }
  thisObj->out->ShutDown();
}

void Join :: merge(Pipe& leftP, Pipe& rightP) {
  Record r1, r2;
  leftP.Remove(&r1);
  rightP.Remove(&r2);
  int cnt = 1;
  ComparisonEngine comp;
  int c;
  vector<Record*> lVec, rVec;
  bool flag1 = true, flag2 = true;
  while (r1.isNull() == false && r2.isNull() == false && flag1 && flag2) {
    c = comp.Compare(&r1, &leftOrder, &r2, &rightOrder);
    // cout << c << " " << r1.isNull() << " " << r2.isNull() << endl;
    while (c != 0 && r1.isNull() == false && r2.isNull() == false) {
      while (c < 0 && r1.isNull() == false) {
	if (leftP.Remove(&r1) == 0) { break; }
        c = comp.Compare(&r1, &leftOrder, &r2, &rightOrder);
      }
      while (c > 0 && r2.isNull() == false) {
	if (rightP.Remove(&r2) == 0) {break; }
	cnt++;
	c = comp.Compare(&r1, &leftOrder, &r2, &rightOrder);
      }
    }
    Record *toInsert;
    while (c == 0 && r2.isNull() == false) {
      //r2.Print(sch2);
      toInsert = new Record();
      toInsert->Copy(&r2);
      rVec.push_back(toInsert);
      if (rightP.Remove(&r2) == 0) {
	flag2 = false;
	break;
      }
      cnt++;
      if (r2.isNull() == false) {
	c = comp.Compare(&r1, &leftOrder, &r2, &rightOrder);
      }
    }
    
    if (r1.isNull() == false) {
      Record rec;
      rec.Copy(&r1);
      while (r1.isNull() == false &&
	     comp.Compare(&r1, &rec, &leftOrder) == 0) {
	//r1.Print(sch1);
	toInsert = new Record();
	toInsert->Copy(&r1);
	lVec.push_back(toInsert);
	if (leftP.Remove(&r1) == 0) {
	  flag1 = false;
	  break;
	}
      }
    }
    joinRec(lVec, rVec);
    lVec.clear();
    rVec.clear();
  }
  cout << "Done!" << cnt << endl;
}

void Join :: joinRec(vector<Record*>& lVec, vector<Record*>& rVec) {
  int lsize = lVec.size();
  int rsize = rVec.size();
  cout << lsize << " " << rsize << endl;
  if (lsize == 0 || rsize == 0) {
    return;
  }
  int attsToKeep[100];
  int numAttsToKeep, numAtts1, numAtts2;
  setAttsToKeepAndNumAtts(attsToKeep, 
			  numAttsToKeep, 
			  *lVec[0],
			  *rVec[0],
			  numAtts1, 
			  numAtts2);
  Record mergedRec;
  for (int i = 0; i < lsize; i++) {
    for (int j = 0; j < rsize; j++) {
      mergedRec.MergeRecords(lVec[i],
			     rVec[j],
			     numAtts1,
			     numAtts2,
			     attsToKeep,
			     numAttsToKeep,
			     numAtts1);
      out->Insert(&mergedRec);
    }
  }
}

void Join :: setAttsToKeepAndNumAtts(int* fillItHere,
				     int &numAttsToKeep,
				     Record& rec1,
				     Record& rec2,
				     int &numAtts1,
				     int &numAtts2) {
  numAtts1 = rec1.GetNumAtts();
  numAtts2 = rec2.GetNumAtts();
  numAttsToKeep = numAtts1 + numAtts2;
  //fillItHere = new int[numAttsToKeep];
  for (int i = 0; i < numAtts1; i++) {
    fillItHere[i] = i;
  }
  for (int i = numAtts1; i < numAttsToKeep; i++) {
    fillItHere[i] = i - numAtts1;
  }
}

void GroupBy :: Run(Pipe &inPipe, 
		    Pipe &outPipe, 
		    OrderMaker &groupAtts, 
		    Function &computeMe) {
  in = &inPipe;
  out = &outPipe;
  grpAtts = &groupAtts;
  myFunc = &computeMe;
  
  pthread_create(&thread, NULL, &GroupBy :: execute, (void*) this);
}

void *GroupBy :: execute(void* x) {
  GroupBy* thisObj = (GroupBy*) x;
  Pipe out(pipesize);
  BigQ bigQ(*thisObj->in, out, *thisObj->grpAtts, thisObj->runLen);
  Record *currRec = new Record();
  if (out.Remove(currRec) == 0) {
    return x;
  }
  Record *prevRec = new Record();
  prevRec->Copy(currRec);
  ComparisonEngine comp;
  int intRes = 0;
  double dblRes = 0.0;
  Attribute DA = {"double", Double};
  Attribute IA = {"int", Int};
  Record grpRec;
  Type tGrp = thisObj->grpAtts->GetType(0);
  Attribute GA;
  int flag;
  if (tGrp == Int) {
    GA = {"int", Int};
  } else {
    GA = {"double", Double};
  }
  Schema *out_sch;
  Type t = thisObj->myFunc->GetReturnType();
  if (t == Int) {
    int sum = 0;
    Attribute atts[] = {IA, GA};
    out_sch = new Schema("out_sch", 1, atts);
    while (out.Remove(currRec) || flag) {
      if (comp.Compare(currRec, prevRec, thisObj->grpAtts) == 0) {
	thisObj->myFunc->Apply(*currRec, intRes, dblRes);
	sum += intRes;
	flag = 1;
      }
      else {
	stringstream ss;
	cout << "group result: " << sum << endl;
	ss << sum;
	ss << "|";
	int v;
	double v2;
	prevRec->GetAtt(*thisObj->grpAtts, v, v2);
	ss << v;
	flag = 0;
	grpRec.ComposeRecord(out_sch, ss.str().c_str());
	thisObj->out->Insert(&grpRec);
	sum = 0;
      }
      prevRec->Copy(currRec);
    }
  }
  else {
    double sum = 0;
    Attribute atts[] = {DA, GA};
    out_sch = new Schema("out_sch", 1, atts);
    while (out.Remove(currRec) || flag) {
      if (comp.Compare(currRec, prevRec, thisObj->grpAtts) == 0) {
        thisObj->myFunc->Apply(*currRec, intRes, dblRes);
        sum += dblRes;
	flag = 1;
      }
      else {
        stringstream ss;
        cout << "group result: " << sum << endl;
        ss << sum;
        ss << "|";
	int v;
        double v2;
        prevRec->GetAtt(*thisObj->grpAtts, v, v2);
        ss << v2;
        grpRec.ComposeRecord(out_sch, ss.str().c_str());
        thisObj->out->Insert(&grpRec);
	sum = 0;
	flag = 0;
      }
      prevRec->Copy(currRec);
    }
  }
  thisObj->out->ShutDown();
}


