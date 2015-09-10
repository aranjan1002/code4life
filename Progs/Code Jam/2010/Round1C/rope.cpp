#include <iostream>
#include <cstring>
using  namespace std;

int main()
{
 int point[1200][2], test, testnum, n, i, j,ans;
 cin >> test;
 testnum = test;
 while (test--)
	{
	 cin >> n; 
	 ans = 0;
	 for( i=0 ; i<n ; i++)
		{
		 cin >> point[i][0] >> point[i][1];
		 for( j=i-1 ; j>=0 ; j--)
			{
			 if ((point[i][0] < point[j][0]	&& point[i][1] > point[j][1]) || 
			      (point[i][0] > point[j][0] && point[i][1] < point[j][1]))
				ans++;
			}
		}
	 cout << "Case #" << testnum-test << ": " << ans << endl;
	}
}