#include <iostream>

using namespace std;

int main()
{
 long test, i, j, testnum, cnt, temp, low, up, fact, ans;
 cin >> test;
 testnum = test;
 
 while (test--)
	{
	 cnt = 0;
	 //cout << "hello";
	 cin >> low >> up >> fact;
	 //cout << low << "\t" << up << "\t" << fact << "\t";
	 temp = low;
	 while (temp*fact < up)
		{temp = temp * fact;
		 //cout << temp << " ";
		 cnt++;
		}
	 //cout << cnt << " ";
	 temp = cnt;
	 ans = 0;
	 if(low*fact!=up)
		while(cnt>0)
			{
			 if(cnt == 1)
				{ans++;
				 break;
				}
			 cnt = cnt/2;
			 ans++;
			}
	 /*if(cnt%2==0 && low*fact!=up)
		ans++;*/
 	 cout << "Case #" << testnum-test << ": " << ans << endl;
	}
}
		
		 