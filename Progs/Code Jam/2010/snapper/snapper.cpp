#include <iostream>

using namespace std;

int main()
{long int n, k, test, nooftests;
 cin >> test;
 nooftests = test;
 while (test--)
	{
 	 cin >> n >> k;
	 while(1)
	 {
	  if(!(k&1))
		{
		 cout << "Case #" << nooftests - test << ": "<< ((k&1)?"ON":"OFF") << endl;
		 break;
		}
	  else if(n == 1) 
		{
		 cout << "Case #" << nooftests - test << ": "<< ((k&1)?"ON":"OFF") << endl;
		 break;
		}
 	  k = k >> 1;
	  n--;
	}
	}
}