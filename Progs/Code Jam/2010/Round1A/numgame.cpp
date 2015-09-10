#include <iostream>

using namespace std;

int main()
{
 int test, testno, ans, a1,a2,b1,b2, i , j;
 cin >> test;
 testno=test;
 while (test--)
	{ans = 0;
	 cin >> a1 >> a2 >> b1 >> b2;
	 for (i=a1; i<=a2; i++)
		for (j=b1; j<=b2; j++)
			{
			 if(i>j)
				{
				 if(i%j==0 || j%(i%j)==0)
					ans++;
				}
			 else if(i<j)
				{
				 if(j%i==0 || i%(j%i)==0)
					ans++;
				}
			 cout << i << j << ans << endl;
			}
	 cout << "Case #" << testno-test << ": " << ans << endl;
	}
}
				