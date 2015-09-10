#include <iostream>
#include <cstring>

using namespace std;

#define BITSPERWORD 32
#define SHIFT 5
#define MASK 0x1f

int a[10000000/BITSPERWORD];

void set(long int i)
{
 a[i>>SHIFT] |= (1<<(i&MASK));
}

void clr(long int i)
{
 a[i>>SHIFT] &= ~(1<<(i&MASK));
}

int check(long int i)
{
 return a[i>>SHIFT] & (1<<(i&MASK));
}

int main()
{
 long int temp, i;
 for (i=0 ; i<10000000 ; i++)
	{
	 clr(i);}
 while (cin>>temp)
	set(temp);
 for (i=0 ; i<10000000 ; i++)
	if(check(i))
		cout << i << endl;
}