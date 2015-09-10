#include <iostream>

using namespace std;

void ins_sort(int *a, int len);

int main()
{
 int a[]={3,4,5,87,3,2,5,4,345,467,45};
 ins_sort(a, 11);
 for(int i=0; i<11; i++)
	cout <<a[i] << endl;
}

void ins_sort(int *a, int len)
{
 int i, j, temp;
 for(i=1; i<len; i++)
	if(a[i]<a[i-1])
		{temp=a[i];
		 j=i-1;
		 while(j>=0 && temp<a[j])
			a[j+1]=a[j--];
		 a[j+1]=temp;
		}
}
