#include <iostream>
#include <cstring>
using namespace std;

int main()
{
 char *a = new char();
 char temp;
 int r, index, tempindex;
 int cnt;
 cin >> a;
 cin >> r;
 cnt = strlen(a);
 index = 0;
 while(cnt)
	{temp=a[index];
 	 tempindex = index;
	 while(((index+r)%strlen(a))!= tempindex)
		{a[index]=a[(index+r)%strlen(a)];
		 index = (index+r)%strlen(a);
		 cnt--;
	 	 //cout << a << endl;
		}
	 a[index]=temp;
	 index = ++tempindex;
	 cnt--;
	}
 cout << a << endl;
}
