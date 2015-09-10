//Selection Sort
#include <iostream>
using namespace std;

void selectionSort(int *a, int len)
{int i, min, j;
 for(i=0; i<len; i++)
	{min = i;
	 for(j=i+1; j<len; j++)
		if (a[j] < a[min])
			min=j;
	 a[i]=a[min]+a[i]-(a[min]=a[i]);
	}
}

void display(int *a, int len)
{int i;
 for (i=0; i<len; i++)
	cout << a[i] << " ";
}

int main()
{
 int a[10], i;
 for (i=0; i<10  ;  cin >> a[i],i++);
 selectionSort(a, 10);
 display(a, 10);
}