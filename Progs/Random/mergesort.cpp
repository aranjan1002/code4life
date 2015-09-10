#include <iostream>

using namespace std;

void merge(int *a, int low, int mid, int up)
{
 int p1 = low, i;
 int p2 = mid+1;
 int b[100];
 cout << low << " " << mid << " " << up << " " << endl;
 for(i=low; i<=up; i++)
	{
	 if ((a[p1] < a[p2] && p1<=mid) || p2>up)
		b[i] = a[p1++];
	 else if(p2<=up)
		b[i] = a[p2++];
	}
 for(i=low; i<=up; i++)
	{//cout << i << " " << b[i] << endl;
	 a[i]=b[i];
	}
 cout << endl << endl;
}
void mergesort(int *a, int low, int up)
{int  	mid = (low+up)/2;
 if(up>low)
	{
	 //mid=(up+low)/2;
	 mergesort(a,low,mid);
	 mergesort(a, mid+1, up);
	 merge(a, low, mid, up);
	}
}
int main()
{
 int a[]={34,54,7,2,5,76,24,34,6,75}, i;
 mergesort(a, 0, 9);
 for(i=0; i<10; i++)
	cout << a[i] << endl;
}
 