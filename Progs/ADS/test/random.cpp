#include <iostream>
#include <cstdlib>
#include <fstream>
#include "Queue.h"

using namespace std;

int main()
{
 Queue<int> q;
 int ch, val;
 while(1)
 {cin >> ch;
  if(ch==1)
	{cin>>val;
	 q.push(val);
	}
  else q.pop();
  q.display();
 }
}