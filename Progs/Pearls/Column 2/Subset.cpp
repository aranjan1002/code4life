#include <iostream>
#include <cstring>

using namespace std;

int main()
{
 int a[100][20], m[20], i, j, L, W;
 
 memset(a, 0, sizeof(a));
 cin >> L;
 for(i=0; i<L; i++)
	cin >> m[i];
 cin >> W;
 memset(a, 0, sizeof(a));
 a[0][0]=1;
 for(j=0; j<L; j++)
	for(i=1; i<=W; i++)
		if((j>0 && a[i][j-1]) || (j>0 && i>=m[j] && a[i-m[j]][j-1]) || i==m[j])
			a[i][j]=1;
		else a[i][j]=0;
	
 for(i=W; i>=0; i--)
	{for(j=0; j<L; j++)
		{
		 if(j==0)
			cout << i << "\t";
		 cout << a[i][j] << "\t";
		}
	 cout << endl;
	}
 cout << "\t";
 for(i=0; i<L; i++)
	cout << m[i]<< "\t";
 cout << endl;
 if(a[L-1][W])
	cout << "Yes" << endl;
 else cout << "No" << endl;
}
