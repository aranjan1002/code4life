#include <iostream>
#include <cstring>
#include <cstdlib>

using namespace std;

int main()
{
 string a;
 int arr[30], count, totaltest, min, mini, j, i, point, mini1, flag, len;
 int test;
 cin >> test;
 totaltest = test;
 while (test--)
	{cin >> a;
	 
	 min = 10;	
	 count = 0;
	 len = a.length();
	 //cout << len << endl;
	 while(len>0)
		{arr[count++] = atoi(&a[len-1]);
		 //cout << (int)a[len-1];
		 a[len-1] = '\0';
		 len--;
		 if (arr[count-1] < min && arr[count-1] != 0)
			{min = arr[count - 1];
			 mini = count - 1;
			}
		}
	 flag = 0;
	 for (i=1 ; i<count ; i++)
		{for (j=0 ; j<i ; j++)
			{
		 	if (arr[i] < arr[j])
				{swap(arr[i],arr[j]);
			 	 flag = 1;
			 	 break;
				}
			}
		  if (flag == 1)
			break;
		}
	cout << "Case #" << totaltest - test << ": ";
	 
	 point = i-1;
	 if (i!=count)
		{
		 for(i=0; i<=point; i++)
			{mini1 = i;
			 for(j=i+1 ; j<=point ; j++)
				{if(arr[j]>arr[i])
					mini1 = j;
				}
			 if (i!=mini1)
			 	swap(arr[i],arr[mini1]);
			}
		 for(i=count-1; i>=0; i--)
			cout << arr[i];
		}
	 else 
		{cout << min;
		 cout << "0";
		 arr[mini]=10;
 		 min = 10;
	 	 for (j=0; j<count-1; j++)	
			{for (i=0; i<count; i++)
				{if (min >= arr[i])
					{min = arr[i];
				 	mini = i;
					}
				}
		 	 cout << min;
		 	 arr[mini] = 10;
			 min = 10;
			}
		}
	 cout << endl;
	}
}
