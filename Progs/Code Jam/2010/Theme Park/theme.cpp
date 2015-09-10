#include <iostream>
#include <cstring>

using namespace std;

int main()
{
 long int queue[1001], uqueue[1001][2], noofrounds, capacity, test, i, count, sum;
 int noofgr, start, testno;
 
 memset(queue, 0, sizeof(queue));

 cin >> test;
 
 testno = test;
 while(test--)
	{
	 start = 0;
	 cin >> noofrounds >> capacity >> noofgr;
	 for (i=0 ; i<noofgr ; i++)
		cin >> queue[i];
	 memset(uqueue, 0, sizeof(uqueue));
	 for (i=0 ; i<noofgr ; i++)
		{count = 0;
		 start = i;
		 while(1)
			{
			 uqueue[i][0] += queue[start];
			 start = (start + 1) % noofgr;
			 count++;
			 if (count > noofgr-1 || (uqueue[i][0] + queue[start]) > capacity)
				{
				 uqueue[i][1] = start;
				 break;
				}
			}
		}
 	/*for ( i=0 ; i<noofgr ; i++)
		cout << uqueue[i][0] << " " << uqueue[i][1] << endl;*/
	sum = 0;
	start = 0;
	for (i=0 ; i<noofrounds ; i++)
		{sum += uqueue[start][0];
		 start = uqueue[start][1];
		}
	cout << "Case #" << testno-test <<  ": " << sum << endl;
	}	
}