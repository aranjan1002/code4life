#include <iostream>
#include <cstring>
#include "bignum.h"

using namespace std;

int main()
{
 int num[70], i, j, tottest, test, count, a[300], dig;
 bignum ans, base, temp2;
 char temp[1000];
 cin >> test;
 tottest = test; 
 while (test--)
 {
  cout << "Case #" << tottest - test << ": ";
  memset (a, 0, sizeof(a));
  cin >> temp;
  if (temp[1])
  	{num[0] = 2;
	 dig = 1;
	 i = 1;
	 a[temp[0]]=2;
	 //cout << a[temp[i]]<<" " << i<<" "<<a[num[0]];
	 while (temp[i])
		{
		 if (a[temp[i]] == 0)
			{
			 num[i]=dig++;
			 a[temp[i]]=dig-1;
			 if(dig==2)
				dig=3;
			}
		 else
			{
			 num[i]=a[temp[i]];
			}
		 //cout << num[i];
		 i++;
		}
 	 base = dig-1;
	 if (base < 2) base =2;
	 ans =0;
	 count = 0;
	 for (j=i-1; j>=0; j--)
		{
		 temp2 = num[j]-1;
		 ans = ans + (temp2 * (base.power(count++)));
		}
	 cout << ans.str() << endl;
	}
  else cout << "1" << endl;
 }
}