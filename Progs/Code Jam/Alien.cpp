#include <iostream>
#include <string>
#include <cstring>

using namespace std;

int main()
{
 int N, L, D, k, j, m, multiple, flag[25],i, countflag, temp1;
 string words[25], test[10];
 
 cin >> L >> D >> N;
 
 for (i=0; i<D; i++)
	cin >> words[i];
 for (i=0; i<N; i++)
	cin >> test[i];

 for (k=0; k<N; k++)
	{memset ( flag, 0, 25*sizeof(int));
	 j=0;multiple=0;countflag=0;
	 for (m=0; test[k][m]; m++)
	 	{if (test[k][m] == '(')
		 	multiple = 1;
	 	 else if (test[k][m] == ')')
			multiple = 0;
	 	 for (i=0; i<D; i++)
			{if (flag[i] == j)
			 if((words[i][j] == test[k][m]))
				{flag[i]++;
				 countflag++;
				}	
			}	
		 if (multiple != 1)
			{j++;
			 if(j<L)
			 countflag=0;
			}
		}
	 cout << "Case #" << k+1 << ": " << countflag << endl;
	}
}
