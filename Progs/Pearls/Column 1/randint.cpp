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
 long int i, a[1000000], temp, j;
 for(j=0; j<10000000; j=j+1000000)
 {
 for (i=j; i<j+1000000; i++)
	a[i-j]=i;
 for (i=0; i<100000; i++)
	{
	 temp = randint(i, 999999);
	 swap(a[i], a[temp]);
	 cout << a[i] <<  endl;
	}
 }
}