#include <iostream>
#include <cstring>
using namespace std;

int binarysearch(int *a, int low, int up, int n)
{
 int mid = (low + up)/2;
 cout << up << " " << low << endl;
 if(low > up)
	{return (-1);
	} 
 if(a[mid] > n)
	return(binarysearch(a, low, mid-1, n));
 else if(a[mid] < n)
	return(binarysearch(a, mid+1, up, n));
 else {
	return (mid);}
}

int main()
{
 int a[100], len, i, j, min;
 cout << "Enter the length of the array: ";
 cin >> len;
 for(i=0; i<len; i++)
	cin >> a[i];
 for(i=0 ; i<len; i++)
	{min = i;
	 for(j=i+1; j<len; j++)
		if(a[min] > a[j])
			min = j;
	 if(i!=min)
	 	swap(a[i],a[min]);
	cout << a[i] << " ";
	}
 cout << endl << "Enter the number you want to search: ";
 cin >> min;
 i = binarysearch(a, 0, len-1, min);
 if(i>0)
	cout << "The number is found at position " << i << endl;
 else cout << "The numebr is not present in the given array" << endl;
}