#include <iostream>

using namespace std;

int func()
{
 int i;
 for (i=0 ; i<10 ; i++)
	return(i);
 cout << i;
}

int main()
{
 cout << func();
}

