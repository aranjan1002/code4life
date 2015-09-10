#include <iostream>

using namespace std;

void removedups(char *a)
{
 int i,j,k;
 int p1, p2;
 for(i=0; a[i]; i++);
 for(j=0; j<i; j++)
	if(a[j])
		for(k=j+1; k<i; k++)
			if(a[k]==a[j]) a[k]=0;
 p1=0;
 p2=0;
 while(p2<i)
 	{
 	 a[p1++]=a[p2++];
	 while(p2<i && a[p2]=='\0')p2++;
	}
 a[p1]=0;
 cout << a;
}
		
int main()
{char str[133];
 
 while(cin >> str)
 removedups(str);
}