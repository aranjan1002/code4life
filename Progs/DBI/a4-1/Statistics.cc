#include "Statistics.h"

#include <cstdlib>
#include <cstdio>

using namespace std;

Statistics::Statistics() {
  lastRelId = -1;
  n = 1.0;
  relSetsLength.assign(100, 0);
}

Statistics::Statistics(Statistics &copyMe) {
  map<string, RelInfo*> :: iterator it1;
  map<string, double> :: iterator it2;
  for (it1 = copyMe.relToRelInfo.begin(); 
       it1 != copyMe.relToRelInfo.end(); it1++) {
    char* relName = const_cast<char*> (it1->first.c_str());
    // char* relNameChr = relName.c_str();
    AddRel(relName, 
	   it1->second->numTuples);
    map<string, double> attsToNumDistincts = it1->second->attsToNumDistincts;
      for (it2 = attsToNumDistincts.begin();
           it2 != attsToNumDistincts.end();
           it2++) {
	AddAtt(relName, 
	       const_cast<char*> (it2->first.c_str()), 
	       it2->second);
      }
  }
}

Statistics::~Statistics() {
  // map<string, RelInfo*> :: iterator it1;
  for (int i = 0; i < relInfoVector.size(); i++) {
    delete relInfoVector[i];
  }
}

void Statistics::AddRel(char *relName, int numTuples) {
  // sanity check
  if (numTuples > 4000000000) {
    cerr << "Too many tuples" << endl;
    exit(1);
  }
  if (exists(relName)) {
    delete FindInRelToRelInfo(relName);
  }
  RelInfo *relInfo = new RelInfo();
  relInfo->numTuples = (double) numTuples;
  relInfo->setId = ++lastRelId;
  relSetsLength[lastRelId] = 1;
  //relInfoVector.push_back(relInfo);
  relToRelInfo.insert(pair<string, RelInfo*>(relName, relInfo));
}

void Statistics::AddAtt(char *relName, char *attName, int numDistincts) {
  map<string, RelInfo*> :: iterator it;
  // cout << relName << attName << numDistincts << endl;
  it = relToRelInfo.find(relName);
  if (it != relToRelInfo.end()) {
    (it->second->attsToNumDistincts)
      .insert(pair<string, int> (attName, (double) numDistincts));
  }
  else {
    cerr << "could not find relation " << relName << endl;
    exit(1);
  }
}

void Statistics::CopyRel(char *oldName, char *newName) {
  RelInfo* oldRelInfo = FindInRelToRelInfo(oldName);
  //relToRelInfo.insert(pair<string, RelInfo*>(newName, relInfo));
  AddRel(newName, oldRelInfo->numTuples);
  map<string, double> :: iterator it2;
  map<string, double> attsToNumDistincts = oldRelInfo->attsToNumDistincts;
  for (it2 = attsToNumDistincts.begin();
       it2 != attsToNumDistincts.end();
       it2++) {
    AddAtt(newName, 
	   const_cast<char*> (it2->first.c_str()), 
	   (int) it2->second);
  }
}
	
void Statistics :: Read(char *fromWhere) {
  ifstream readFile(fromWhere, ifstream::in);
  lastRelId = 0;
  n = 1.0;
  relSetsLength.assign(100, 0);
  relToRelInfo.clear();
  if (readFile.is_open()) {
    ReadFileStream(readFile);
  }
  /*else {
    cerr << "Unable to open file" << endl;
    exit(1);
    }*/
}

void Statistics :: ReadFileStream(ifstream& inputStream) {
  string line;
  getline(inputStream, line);
  int relCnt = atoi(line.c_str());
  // cout << "Rel cnt " << relCnt << endl;
  for (int i = 0; i < relCnt; i++) {
    ReadRel(inputStream);
  }
}

void Statistics :: ReadRel(ifstream& inputStream) {
  string line = "", numDistincts, relName, setId;
  //char *relName;
  getline(inputStream, relName);
  //relName = const_cast<char*> (line.c_str());
  // cout << "found rel " << relName << endl;
  getline(inputStream, numDistincts);
  AddRel(const_cast<char*> (relName.c_str()), atoi(numDistincts.c_str()));
  getline(inputStream, setId);
  int setIdInt = atoi(setId.c_str());
  SetSetId(relName, setIdInt);
  //relSetsLength[setIdInt]++;
  //cout << "set length " << setIdInt << " " << relSetsLength[setIdInt] << endl;
  if (lastRelId < setIdInt) {
    lastRelId = setIdInt;
  }
  while (line != "-1") {
    getline(inputStream, line);
    if (line != "-1") {
      getline(inputStream, numDistincts);
      // cout << line << " " << numDistincts << endl;
      AddAtt(const_cast<char*> (relName.c_str()),
	     const_cast<char*> (line.c_str()),
	     atoi(numDistincts.c_str()));
    } 
  }
}

void Statistics :: Write(char *fromWhere) {
  ofstream outputStream(fromWhere, ofstream :: out);
  if (outputStream.is_open()) {
    Print(outputStream);
  }
  else {
    cerr << "Unable to open file" << endl;
    exit(1);
  }
}

void Statistics :: Print(ostream &outputStream) {
  // if (outputStream.is_open()) {
    outputStream << relToRelInfo.size() << endl;
    map<string, RelInfo*> :: iterator it1;
    map<string, double> :: iterator it2;
    for (it1 = relToRelInfo.begin(); it1 != relToRelInfo.end(); it1++) {
      outputStream << it1->first << endl;
      outputStream << it1->second->numTuples << endl;
      outputStream << it1->second->setId << endl;
      map<string, double> attsToNumDistincts = it1->second->attsToNumDistincts;
      for (it2 = attsToNumDistincts.begin(); 
	   it2 != attsToNumDistincts.end();
	   it2++) {
	outputStream << it2->first << endl;
	outputStream << it2->second << endl;
      }
      outputStream << "-1" << endl;
    }
}

void  Statistics::Apply(struct AndList *parseTree, 
			char *relNames[], 
			int numToJoin) {
  // cout << "In Apply" << endl;
  // Print(cout);
  apply = true;
  setJoinRelsCnt(relNames, numToJoin);
  double result = ComputeAnd(parseTree);
  int setId = GetSetId(relNames[0]);
  // cout << "Set ID " << setId << " num " << n << " " <<  result << endl;
  SetNumTuples(setId, (n / result));
  // Print(cout);
}

double Statistics :: Estimate(struct AndList *parseTree, 
			      char **relNames, 
			      int numToJoin) {
  apply = false;
  // cout << "In Estimate" << endl;
  // Print(cout);
  setJoinRelsCnt(relNames, numToJoin);
  double result = ComputeAnd(parseTree);
  // cout << " num " << n << " " <<  result << endl;
  return (n / result);
}

void Statistics :: setJoinRelsCnt(char **relNames, int numToJoin) {
  n = 1.0;
  // cout << relNames[0] << endl;
  int relSetCnt[lastRelId];
  for (int i = 0; i <= lastRelId; i++) {
    relSetCnt[i] = 0;
  }
  for (int i = 0; i < numToJoin; i++) {
    // cout << (char *) relNames[1] << endl;
    int setId = GetSetId(relNames[i]);
    // cout << "increasing setid" << setId 
    //  << "previously: " << relSetCnt[setId] << endl;
    relSetCnt[setId]++;
    if (relSetCnt[setId] == 1) {
      n *= GetNumTuples(relNames[i]);
    }
  }
  for (int i = 0; i <= lastRelId; i++) {
    if (relSetCnt[i] != 0 && relSetCnt[i] != relSetsLength[i]) {
      cout << "SetID: " << i << " Expected: " << relSetsLength[i]
	   << " Got: " << relSetCnt[i] << endl;
      cerr << "Invalid set of relations" << endl;
      exit(1);
    }
  }
  // cout << "n = " << n << endl;
}

double Statistics :: ComputeAnd(AndList *andList) {
  double rightVal = 0;
  if (andList->rightAnd != NULL) {
    rightVal = ComputeAnd(andList->rightAnd);
  }
  double leftVal = ComputeOr(andList->left);
  // cout << leftVal << " " << rightVal << " " << n << endl;
  if (rightVal != 0) {
    return (rightVal * leftVal);
  }
  else {
    return leftVal;
  }
}

double Statistics :: ComputeOr(OrList *orList) {
  double rightVal = 0;
  if (orList->rightOr != NULL) {
    rightVal = ComputeOr(orList->rightOr);
  }
  double leftVal = GetTuplesCount(orList->left);
  // cout << leftVal << " " << rightVal << " " << n << endl;
  if (rightVal != 0) {
    if (IsDependent(orList)) {
      return (1/((1/leftVal) + (1/rightVal)));
    }
    leftVal = n / leftVal;
    rightVal = n / rightVal;
    return (1 /(1 - ((1 - (leftVal / n)) * (1 - (rightVal / n)))));
  }
  else {
    return leftVal;
  }
}

bool Statistics :: IsDependent(OrList *orList) {
  if (orList->rightOr == NULL) {
    return true;
  }
  string leftRel = GetAtt(orList->left);
  string rightRel = GetAtt(orList->rightOr->left);
  if (IsDependent(orList->rightOr) && leftRel == rightRel) {
    return true;
  }
  return false;
}

string Statistics :: GetAtt(ComparisonOp *compOp) {
  return compOp->left->value;
}

double Statistics :: GetTuplesCount(ComparisonOp *compOp) {
  if (compOp->code == EQUALS) {
    if (IsJoin(compOp) == true) {
      return Join(compOp);
    }
    else {
      double numDistinct = GetNumDistincts(compOp->left);
      return (numDistinct);
    }
  }
  else if (compOp->code == LESS_THAN || compOp->code == GREATER_THAN) {
    return (3.0);
  }
  else {
    cerr << "Invalid comparison operator" << endl;
    exit(1);
  }
}

void Statistics :: PrintOperand(Operand *op) {
  cout << "Operand value: " << op->value << endl;
  cout << "Operand type: " << op->code << endl;
}

double Statistics :: GetNumDistincts(Operand* op) {
  // PrintOperand(op);
  if (op->code != NAME) {
    cerr << "expected attribute name" << endl;
    exit(1);
  }
  else {
    char *attName = op->value;
    char *attNameCopy = new char[strlen(attName)];
    strcpy(attNameCopy, attName);
    char *s1 = strtok(attNameCopy, ".");
    char *s2 = strtok(NULL, ".");
    // delete attNameCopy;
    double result;
    if (s2 == NULL) {
      result = GetNumDistincts(s1);
    }
    else {
      result = GetNumDistincts(s1, s2);
    }
    delete attNameCopy;
    return result;
  }
}

double Statistics :: GetNumDistincts(char* attName) {
  map<string, RelInfo*> :: iterator it1;
  map<string, double> :: iterator it2;
  for (it1 = relToRelInfo.begin();
       it1 != relToRelInfo.end(); 
       it1++) {
    map<string, double> attsToNumDistincts = it1->second->attsToNumDistincts;
    it2 = attsToNumDistincts.find(attName);
    if (it2 != attsToNumDistincts.end()) {
      return (double) it2->second;
    }
  }
  cerr << "Unable to find attribute " << attName << endl;
  exit(1);
}

string Statistics :: GetRelName(char* attName) {
  char *attNameCopy = new char[strlen(attName)];
  strcpy(attNameCopy, attName);
  char *s1 = strtok(attNameCopy, ".");
  char *s2 = strtok(NULL, ".");
  if (s2 != NULL) {
    return s1;
  }
  delete attNameCopy;
  map<string, RelInfo*> :: iterator it1;
  map<string, double> :: iterator it2;
  for (it1 = relToRelInfo.begin();
       it1 != relToRelInfo.end();
       it1++) {
    map<string, double> attsToNumDistincts = it1->second->attsToNumDistincts;
    it2 = attsToNumDistincts.find(attName);
    if (it2 != attsToNumDistincts.end()) {
      return it1->first;
    }
  }
  cerr << "Unable to find attribute " << attName << endl;
  exit(1);
}

double Statistics :: GetNumDistincts(char* relName, char* attName) {
  map<string, RelInfo*> :: iterator it1;
  map<string, double> :: iterator it2;
  it1 = relToRelInfo.find(relName);
  if (it1 != relToRelInfo.end()) {
    map<string, double> attsToNumDistincts = it1->second->attsToNumDistincts;
    it2 = attsToNumDistincts.find(attName);
    if (it2 !=  attsToNumDistincts.end()) {
      return (double) it2->second;
    }
  }
  cerr << "Unable to find attribute " << attName << endl;
  exit(1);
}

bool Statistics :: IsJoin(ComparisonOp* compOp) {
  if (compOp->left->code != NAME || 
      compOp->right->code != NAME ||
      compOp->code != EQUALS) {
    return false;
  }
  char *leftAtt = compOp->left->value;
  char *rightAtt = compOp->right->value;
  // cout << "left att " << leftAtt << endl;
  if (GetRelName(leftAtt) != GetRelName(rightAtt)) {
    // cout << "left att afterwards " << leftAtt << endl;
    return true;
  }
  return false;
}

void Statistics :: PrintCompOp(ComparisonOp* compOp) {
  cout << "Comparison code: " << compOp->code << endl;
  cout << "left operand: " << endl;
  PrintOperand(compOp->left);
  cout << "right operand: " << endl;
  PrintOperand(compOp->right);
}

double Statistics :: Join(ComparisonOp* compOp) {
  // PrintCompOp(compOp);
  double maxDistinct = max(GetNumDistincts(compOp->left),
			   GetNumDistincts(compOp->right));
  string leftRel = GetRelName(compOp->left->value);
  string rightRel = GetRelName(compOp->right->value);
  int leftSetId = GetSetId(leftRel);
  int rightSetId = GetSetId(rightRel);
  double leftNumTuples = GetNumTuples(leftRel.c_str());
  double rightNumTuples = GetNumTuples(rightRel.c_str());
  // cout << "Combining " << leftRel << " and " << rightRel 
  //  << " which corresponds to sets " << leftSetId << " and " 
  //  << rightSetId << endl;
  if (leftSetId != rightSetId && apply == true) {
    SetSetId(rightSetId, leftSetId); 
    /*cout << leftNumTuples 
      << " " << rightNumTuples << " " << maxDistinct << endl;*/
    double numTuples =  (leftNumTuples * rightNumTuples) / maxDistinct;
    // cout << "Join num att " << numTuples << endl;
    // SetNumTuples(leftSetId, numTuples);
    //relSetsLength[leftSetId]++;
  }
  return (double) maxDistinct;
}

int Statistics :: GetSetId(string relName) {
  RelInfo *relInfo = FindInRelToRelInfo(relName);
  return relInfo->setId;
}

void Statistics :: SetSetId(string relName, 
			    int setId) {
  // cout << "Setting id for " << relName << endl;
  // Print(cout);
  RelInfo *relInfo = FindInRelToRelInfo(relName);
  if (relInfo->setId != setId) {
    relInfo->setId = setId;
    relSetsLength[setId]++;
    // cout << "Set length " << setId << " " << relSetsLength[setId] << endl;
  }
}

RelInfo* Statistics ::  FindInRelToRelInfo(string relName) {
  map<string, RelInfo*> :: iterator it;
  it = relToRelInfo.find(relName);
  if (it != relToRelInfo.end()) {
    return it->second;
  }
  else {
    cerr << "Unable to find relation " << relName << endl;
    exit(1);
  }
}

bool Statistics :: exists(string relName) {
  map<string, RelInfo*> :: iterator it;
  it = relToRelInfo.find(relName);
  return it != relToRelInfo.end();
}

void Statistics :: SetNumTuples(int setId,
				double numTuples) {
  map<string, RelInfo*> :: iterator it1;
  for (it1 = relToRelInfo.begin();
       it1 != relToRelInfo.end();
       it1++) {
    if (it1->second->setId == setId) {
      it1->second->numTuples = numTuples;
    }
  }
}

void Statistics :: SetSetId(int oldSetId,
			    int newSetId) {
  if (oldSetId == newSetId) {
    return;
  }
  map<string, RelInfo*> :: iterator it1;
  for (it1 = relToRelInfo.begin();
       it1 != relToRelInfo.end();
       it1++) {
    if (it1->second->setId == oldSetId) {
      it1->second->setId = newSetId;
      relSetsLength[newSetId]++;
      // cout << "Set Length " << newSetId 
      //  << " " << relSetsLength[newSetId] << endl;
    }
  }
}

double Statistics :: GetNumTuples(const char* relName) {
  RelInfo *relInfo = FindInRelToRelInfo(relName);
  return relInfo->numTuples;
}

