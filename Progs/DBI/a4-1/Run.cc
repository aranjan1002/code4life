#include "Run.h"

Run :: Run(File& f, int startOffset, int stopOffset) : file(f),
						       startOff(startOffset),
						       stopOff(stopOffset) 
{
  if (startOff > stopOff) {
    cout << "Start offset greater than stop offset" << endl;
    exit(1);
  }
  currOff = startOff;
  file.GetPage(&currPage, currOff);
  currPage.MoveFirst();
}  

int Run :: GetNext(Record* putItHere) {
  if (currPage.GetNext(*putItHere) == 0) {
    if (currOff < stopOff) {
      file.GetPage(&currPage, ++currOff);
      currPage.MoveFirst();
      if (currPage.GetNext(*putItHere) != 1) {
	cout << "Unable to read from new page:" << endl;
	cout << "page no " << currOff << endl;
	exit(1);
	return 0;
      }
    }
    else {
      return 0;
    }
  }
  return 1;
}

Run :: ~Run() {}
