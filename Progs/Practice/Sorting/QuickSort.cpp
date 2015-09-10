#include <iostream>

using namespace std;

void display(int *arr)
{int i;
 for(i=0; i<10; i++)
	cout << arr[i] << "\t";
 cout << endl;
}

void swap(int *a, int *b)
{
 int temp=*a;
 *a=*b;
 *b=temp;
}

void Quick_Sort(int *arr, int start, int end)
{
 int istart = start;
 int iend = end;
 int pivot = start++;
 
 display(arr);
 
 while(start<end)
	{
	 while(arr[start] < arr[pivot] && start < iend)
		start++;
	 while(arr[end] > arr[pivot] && end>(pivot+1))
		end--;
	 if(start<end)
		swap(&arr[start], &arr[end]);
	}
 if(arr[pivot] > arr[end])
 	swap(&arr[pivot], &arr[end]);
 cout << pivot << " " << end << " " << start << " " << istart << " " << iend << endl;
 if(end-1-istart>0)
	Quick_Sort(arr, istart, end-1);
 if(iend-end-1>0)
	Quick_Sort(arr, end+1, iend);
}

int main()
{
 int arr[]={23,54,2,45,65,23,5,68,43,34}, i;
 Quick_Sort(arr, 0, 9);
 display(arr);
}

