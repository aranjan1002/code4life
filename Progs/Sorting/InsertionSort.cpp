//Insertion Sort
#include <iostream>

using namespace std;

void InsertionSort(int *a, int len)
{
 int i, temp, j;
 for (i=1 ; i<len ; i++)
	{
	 temp = a[i];
	 j=i;
	 while (a[j-1] > temp && j>0)
		a[j--]=a[j-1];
	 a[j]=temp;
	}
}
void display (int *arr, int len)
{int i;
 for(i=0; i<len; i++)
	cout << arr[i] << " ";
}

int main()
{
 int arr[]={32,56,23,45,7,8,23,7,6,123,21};
 InsertionSort(arr, 11);
 display(arr, 11);
}