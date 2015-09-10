#include <iostream>
using namespace std;

long int maxim(long int a, long int b)
{
 if (a>b)
	return a;
 else return b;
}

int main()
{
 long int num[3], max, gcd, m;
 int no, test, i,test1;
 cin >> test;
 test1 = test;

 while (test--)
	{max = 0;
	 cin >> no;
	 for (i=0 ; i<no ; i++)
		cin >> num[i];
	 if (no == 2)
		num[2]=num[0];
	 m = maxim(maxim(num[0],num[1]),num[2]);
	 for (i=2 ; i<=m ; i++)
		if(num[0]%i == num[1]%i && num[1]%i == num[2]%i)
				{max = i - num[0]%i;
				 //cout << i << endl;
				 //break;
				}
	 cout << "Case #" << test1-test << ": " << max << endl;
	}
}