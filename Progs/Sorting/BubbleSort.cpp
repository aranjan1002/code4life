//Bubble Sort
#include <iostream>
using namespace std;

void display(int *a, int len)
{int i;
 for (i=0; i<len; i++)
	cout << a[i] << " ";
 cout << endl;
}

void bubbleSort(int *a, int len)
{
 int i, j;
 for (i=0; i<len; i++)
	{for (j=0; j<len-i-1; j++)
		if(a[j]>a[j+1])
			a[j]=a[j]+a[j+1]-(a[j+1]=a[j]);
	// display(a, len);
	}
}

int main()
{
 int a[10], i;
 for (i=0; i<10; cin >> a[i], i++); 
 bubbleSort(a, 10);
 display(a,10);
}

	