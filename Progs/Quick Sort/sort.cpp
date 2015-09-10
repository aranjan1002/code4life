#include <iostream>

using namespace std;

void display(int array[])
{
 int count = 20;
 while (count--)
	cout << array[19-count] << "  ";
 cout << endl;
}

void quicksort(int a[], int x, int j)
{
 int end = j;
 int i = x+1;
 while ( i<j )
	{
	 while (a[i] <= a[x] && i<end)
		i++;
	 while (a[j] >= a[x] && j>x)
		j--;
	 if (i<j)
		swap(a[i], a[j]);
	 //cout << i << " " << j << endl;
	}
 if(a[x]>=a[j])
 swap(a[x], a[j]);
 //display (a);
 if(j!=x)
	quicksort(a, x, j-1);
 if(j!=end)
	quicksort(a, j+1, end);
}


int main()
{int array[20], i;

 for (i=0 ; i<20 ; i++)
	scanf ("%d" , &array[i]);
 
 quicksort(array, 0, 19);
 display(array);
}