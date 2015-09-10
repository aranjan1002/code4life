#include <iostream>
using namespace std;


void rec(int i)
{
 while(i<5)
	{cout << i << endl;
	 i++;
	 rec(i);
	}
 cout << i << endl;
}

int main()
{rec(0);}