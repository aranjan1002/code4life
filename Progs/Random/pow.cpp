#include <iostream>
using namespace std;

int pow(int a, int b)
{int ans, i, j, temp;
ans = a;
temp = a;
 if(b==0)
	return(1);
for(i=0; i<b-1; i++)
	{
	 for(j=0; j<a-1; j++)
		ans=temp+ans;
	 temp=ans;
	}
 return(ans);
}


int main()
{int a, b;
 cin>> a >> b;
 cout << pow(a,b);
}