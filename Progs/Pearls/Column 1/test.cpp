#include <iostream>
#include <cstring>
#include <cstdlib>

using namespace std;

int main()
{
 unsigned long int a,cnt=0;
 memset (&a, 255, sizeof(a));
 	 cout << a <<endl;
      
 cout << a << endl;
 while (cnt<32)
	{cnt++;
	 cout << (a&1);
	 a = a >> 1;
	}
}

