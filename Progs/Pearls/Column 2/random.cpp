#include <iostream>
#include <cstdlib>
#include <cstring>

using namespace std;

long int randint(long int l, long int u)
{
 return l+rand()%(u-l+1);
}

int main()
{

 long int i, a[65536], temp, j;
 for (i=0; i<65536; i++)
	a[i]=i;
 for (i=0; i<65535; i++)
	{
	 temp = randint(i, 65535);
	 swap(a[i], a[temp]);
	 cout << a[i] <<  endl;
	}
}