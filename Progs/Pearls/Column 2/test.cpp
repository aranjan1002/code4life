#include <iostream>
#include <cstring>
using namespace std;

int main()
{ 
 int a[65536], i, temp;
 memset(a, 0, sizeof(a));
 for (i=0; i<65536; i++)
	{cin >> temp;
	a[temp]=1;}
 for(i=0; i<65535; i++)
	if(a[i]!=1)
		cout << i << endl;
}