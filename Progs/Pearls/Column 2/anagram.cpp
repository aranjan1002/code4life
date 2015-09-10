#include <iostream>
#include <cstdlib>
#include <cstring>

using namespace std;

int tolower(char a)
	{
	 int i=a;
	 if(i>=65 && i<=90)
		return(i+32);
	 else return(i);
	}

char* signature(char *a)
{char *b=new char();
 int index=0, tmp[26], cnt=0, i, j;
 memset(tmp, 0, sizeof(tmp));
	 while(a[index++])
		tmp[tolower(a[index-1])-97]++;
	 for(j=0; j<26; j++)
		if(tmp[j])
			while(tmp[j]--)
				(b[cnt++]=(char)(j+97));
 b[cnt]=0;
	 //cout <<endl;
 return(b);
}

/*void merge(char a[][30], int low, int mid, int up)
{char b[250000][30];
 int p1 = low, i;
 int p2 = mid+1;
  b[100];
 cout << low << " " << mid << " " << up << " " << endl;
 for(i=low; i<=up; i++)
	{
	 if ((strcmp(signature(a[p1]), signature(a[p2])) < 0 && p1<=mid) || p2>up)
		strcpy(b[i], a[p1++]);
	 else if(p2<=up)
		strcpy(b[i], a[p2++]);
	}
 for(i=low; i<=up; i++)
	{//cout << i << " " << b[i] << endl;
	 strcpy(a[i],b[i]);
	}
 cout << endl << endl;
}
void mergesort(char a[][30], int low, int up)
{int  	mid = (low+up)/2;
 if(up>low)
	{
	 //mid=(up+low)/2;
	 mergesort(a,low,mid);
	 mergesort(a, mid+1, up);
	 //cout << low <<  " " << up << endl;
	 merge(a, low, mid, up);
	}
}*/

int compare(const void *a, const void *b)
{
 return(strcmp(signature((char *)(a)), signature((char *)(b))));
}



int main()
{
 char a[250000][30], tmp[26],j, index, *b;
 int i, maxi=1;
 i=0;
 while(cin>>a[i++]);
 cout << signature(a[i-2]);
 qsort(a, i-2, 30, compare);
 while(maxi++ < i)
	{if(compare(a[maxi],a[maxi-1]) == 0)
		{cout << a[maxi-1] << " ";
		 while(compare(a[maxi], a[maxi-1]) == 0)
			cout << a[maxi++] << " ";
	 	 cout << endl;
		}
	}
}