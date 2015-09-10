#include <iostream>
#include <cstring>

using namespace std;

int main()
{
 long int a[1000000], temp, count=0;
 memset (a, 0, sizeof(a));
 while(cin >> temp)
 	{//cout << count++ << " " << temp << endl;
	 if (a[temp] == 1)
		cout << "Numbers not unique" << temp << endl;
	 a[temp] = 1; 
	 
	}
 cout << "Numbers are unique";
}
