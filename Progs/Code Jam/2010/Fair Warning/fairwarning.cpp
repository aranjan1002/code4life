#include <iostream>

using namespace std;

long int gcd(long int a, long int b)
{
 if (b == 0)
	return a;
 else return gcd(b, a%b);
}

int main()
{
 long int num[3], max, m, result, i, ogcd;
 int no, test,test1;
 cin >> test;
 test1 = test;

 while (test--)
	{max = 0;
	 cin >> no;
	 cin >> num[0];
	 ogcd = num[0];
	 //cout << num[0] << " ";
	 for (i=1 ; i<no ; i++)
		{cin >> num[i];
		 if(ogcd > num[i])
			ogcd = gcd(ogcd, num[i]);
		 else 
			ogcd = gcd(num[i], ogcd);
		 //cout << num[i] << " ";
		 num[i] = (num[0] - num[i]);
		 if (num[i] < 0)
			num[i] = (-1) * num[i];
		}
	 result = num[1];
	 for (i=1; i<no; i++)
		{//cout << gcd(result, result) << endl;
		 if(result > num[i])
			result = gcd(result, num[i]);
		 else 
			result = gcd(num[i], result);
		 //cout << num[i] << " " << result << endl;
		}
	 if (result == 1 || result <= ogcd)
		cout << "Case #" << test1-test << ": 0" << endl;
	 else
	 cout << "Case #" << test1-test << ": " << result-(num[0]%result) << endl;
	}
}