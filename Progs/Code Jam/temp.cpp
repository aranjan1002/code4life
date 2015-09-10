#include <iostream>

int main()
{
 int N, L, D, k, j, m, multiple, flag[25];
 char words[25][10], test[10][100];
 
 cin >> L >> D >> N;
 
 for (i=0; i<D; i++)
	cin >> words[i];
 for (i=0; i<N; i++)
	cin >> test[i];

 memset ( flag, 1, 25*sizeof(int));

 for (k=0; k<N; k++)
	{j=0; multiple=0; countflag=D;
	 for (m=0; test[k][m]; m++)
	 	{if (test[k][m] == '(')
		 	multiple = 1;
	 	 else if (test[k][m] == ')')
			multiple = 0;
	 	 for (i=0; i<D; i++)
			{if (flag[i])
			 if!(word[i][j] == test[k][m])
				{flag[i]=0;
				 countflag--;
				}		
			}
		 if (multiple != 1)
			j++;
		}
	 cout << countlflag << endl;
	}
} 
		
